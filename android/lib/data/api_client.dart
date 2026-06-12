import 'package:dio/dio.dart';
import '../core/constants.dart';
import 'models/room_config.dart';

class ApiClient {
  final Dio _dio;

  ApiClient(String? deviceToken)
      : _dio = Dio(BaseOptions(
          baseUrl: AppConstants.apiBaseUrl,
          connectTimeout: const Duration(seconds: 10),
          receiveTimeout: const Duration(seconds: 15),
          headers: {'X-Device-Token': ?deviceToken},
        ));

  Future<RoomConfig> getRoomConfig() async {
    final response = await _dio.get<Map<String, dynamic>>('/api/v1/room/config');
    return RoomConfig.fromJson(response.data!);
  }

  Future<Map<String, dynamic>> registerDevice(String roomNumber) async {
    final response = await _dio.post<Map<String, dynamic>>(
      '/api/v1/devices/register',
      data: {'roomNumber': roomNumber},
    );
    return response.data!;
  }
}
