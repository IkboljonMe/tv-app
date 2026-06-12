import '../api_client.dart';
import '../models/room_config.dart';

class RoomRepository {
  final ApiClient _client;

  RoomRepository(this._client);

  Future<RoomConfig> getRoomConfig() => _client.getRoomConfig();
}
