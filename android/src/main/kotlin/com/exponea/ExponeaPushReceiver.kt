package com.exponea

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.exponea.data.OpenedPush
import com.exponea.data.PushAction
import com.exponea.sdk.models.NotificationAction
import com.exponea.sdk.services.ExponeaPushReceiver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.RuntimeException

class ExponeaPushReceiver : BroadcastReceiver() {
    /**
     * We respond to all push notification actions and pass the push notification information to ExponeaPlugin.
     * For default "open app" action, we also start the application.
     * For "deeplink" action, Exponea SDK will generate intent and it's up to the developer to implement Intent handler.
     * For "web" action, Exponea SDK will generate intent that will be handled by the browser.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val action = when (intent.action) {
            ExponeaPushReceiver.ACTION_CLICKED -> PushAction.APP
            ExponeaPushReceiver.ACTION_DEEPLINK_CLICKED -> PushAction.DEEPLINK
            ExponeaPushReceiver.ACTION_URL_CLICKED -> PushAction.WEB
            else -> throw RuntimeException("Unknown push notification action ${intent.action}")
        }
        val url = (intent.getSerializableExtra(ExponeaPushReceiver.EXTRA_ACTION_INFO) as? NotificationAction)?.url
        val pushData = intent.getSerializableExtra(ExponeaPushReceiver.EXTRA_CUSTOM_DATA) as Map<String, String>
        val additionalDataType = object : TypeToken<Map<String, Any?>?>() {}.getType()
        val additionalData = Gson().fromJson(pushData["attributes"], additionalDataType) as Map<String, Any?>?
        OpenedPushStreamHandler.handle(OpenedPush(action, url, additionalData))

        if (intent.action == ExponeaPushReceiver.ACTION_CLICKED) {
            val actionIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            context.startActivity(actionIntent)
        }
    }
}