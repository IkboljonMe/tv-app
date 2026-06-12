import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../data/api_client.dart';
import '../data/models/room_config.dart';
import '../data/repositories/room_repository.dart';
import 'device_token_provider.dart';

final _roomRepositoryProvider =
    Provider.family<RoomRepository, String>((ref, token) {
  return RoomRepository(ApiClient(token));
});

class RoomConfigNotifier extends AsyncNotifier<RoomConfig?> {
  @override
  Future<RoomConfig?> build() async {
    final token = await ref.watch(deviceTokenProvider.future);
    if (token == null) return null;
    final repo = ref.watch(_roomRepositoryProvider(token));
    try {
      return await repo.getRoomConfig();
    } catch (_) {
      return null;
    }
  }

  void clearGuest() {
    final current = state.valueOrNull;
    if (current != null) {
      state = AsyncData(current.copyWith(clearGuest: true));
    }
  }

  void updateBackground(BackgroundConfig bg) {
    final current = state.valueOrNull;
    if (current != null) {
      state = AsyncData(current.copyWith(background: bg));
    }
  }
}

final roomConfigProvider =
    AsyncNotifierProvider<RoomConfigNotifier, RoomConfig?>(
  RoomConfigNotifier.new,
);
