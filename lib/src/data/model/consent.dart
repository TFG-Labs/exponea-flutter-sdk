import 'package:meta/meta.dart';

@immutable
class Consent {
  final String id;
  final bool legitimateInterest;
  final ConsentSources sources;
  final Map<String, Map<String, String>> translations;

  const Consent({
    required this.id,
    required this.legitimateInterest,
    required this.sources,
    this.translations = const {},
  });
}

@immutable
class ConsentSources {
  final bool createdFromCRM;
  final bool imported;
  final bool fromConsentPage;
  final bool privateAPI;
  final bool publicAPI;
  final bool trackedFromScenario;

  const ConsentSources({
    this.createdFromCRM = false,
    this.imported = false,
    this.fromConsentPage = false,
    this.privateAPI = false,
    this.publicAPI = false,
    this.trackedFromScenario = false,
  });
}
