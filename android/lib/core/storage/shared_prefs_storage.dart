import 'package:shared_preferences/shared_preferences.dart';
import 'app_storage.dart';

class SharedPrefsStorage implements AppStorage {
  final SharedPreferences _prefs;

  SharedPrefsStorage(this._prefs);

  static Future<SharedPrefsStorage> create() async {
    final prefs = await SharedPreferences.getInstance();
    return SharedPrefsStorage(prefs);
  }

  @override
  Future<String?> getString(String key) async => _prefs.getString(key);

  @override
  Future<void> setString(String key, String value) async =>
      _prefs.setString(key, value);

  @override
  Future<void> remove(String key) async => _prefs.remove(key);
}
