class AppConstants {
  static const String apiBaseUrl = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: 'http://192.168.1.100:3000',
  );
  static const String wsUrl = String.fromEnvironment(
    'WS_BASE_URL',
    defaultValue: 'ws://192.168.1.100:3000',
  );
  static const String deviceTokenKey = 'device_token';
  static const String roomNumberKey = 'room_number';
}
