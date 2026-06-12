// Set IS_TIZEN=true via --dart-define when building the Tizen TPK.
const bool kIsTizen = bool.fromEnvironment('IS_TIZEN', defaultValue: false);

class AppPlatform {
  static const bool isTizen = kIsTizen;
  static bool get isAndroidTV => !kIsTizen;
}
