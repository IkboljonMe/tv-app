import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'device_token_provider.dart';
import 'room_config_provider.dart';
import 'websocket_provider.dart';

enum AppScreen { splash, provisioning, attract, welcome }

final appScreenProvider = FutureProvider<AppScreen>((ref) async {
  // Activate WebSocket listener whenever we have a screen
  ref.watch(websocketListenerProvider);

  final token = await ref.watch(deviceTokenProvider.future);
  if (token == null) return AppScreen.provisioning;

  final config = await ref.watch(roomConfigProvider.future);
  if (config == null) return AppScreen.attract;

  return config.isOccupied ? AppScreen.welcome : AppScreen.attract;
});
