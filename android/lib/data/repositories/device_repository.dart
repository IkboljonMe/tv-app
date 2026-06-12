import '../api_client.dart';
import '../../core/storage/app_storage.dart';
import '../../core/constants.dart';

class DeviceRepository {
  final AppStorage _storage;

  DeviceRepository(this._storage);

  Future<String?> getDeviceToken() =>
      _storage.getString(AppConstants.deviceTokenKey);

  Future<String> registerDevice(String roomNumber) async {
    final client = ApiClient(null);
    final result = await client.registerDevice(roomNumber);
    final token = result['deviceToken'] as String;
    await _storage.setString(AppConstants.deviceTokenKey, token);
    await _storage.setString(AppConstants.roomNumberKey, roomNumber);
    return token;
  }

  Future<void> clearToken() =>
      _storage.remove(AppConstants.deviceTokenKey);
}
