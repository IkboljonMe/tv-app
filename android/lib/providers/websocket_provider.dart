import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../services/websocket_service.dart';
import '../data/models/announcement_data.dart';
import '../data/models/room_config.dart';
import 'device_token_provider.dart';
import 'room_config_provider.dart';
import 'announcement_provider.dart';

final websocketServiceProvider = Provider<WebSocketService?>((ref) {
  final tokenAsync = ref.watch(deviceTokenProvider);
  return tokenAsync.maybeWhen(
    data: (token) {
      if (token == null) return null;
      final service = WebSocketService(token);
      service.connect();
      ref.onDispose(service.dispose);
      return service;
    },
    orElse: () => null,
  );
});

final websocketListenerProvider = Provider<void>((ref) {
  final service = ref.watch(websocketServiceProvider);
  if (service == null) return;

  final sub = service.messages.listen((msg) {
    switch (msg.type) {
      case WsMessageType.refreshConfig:
        ref.invalidate(roomConfigProvider);
      case WsMessageType.clearGuest:
        ref.read(roomConfigProvider.notifier).clearGuest();
      case WsMessageType.showAnnouncement:
        if (msg.payload != null) {
          ref.read(announcementProvider.notifier).show(
                AnnouncementData.fromJson(msg.payload!),
              );
        }
      case WsMessageType.updateBackground:
        if (msg.payload != null) {
          ref.read(roomConfigProvider.notifier).updateBackground(
                BackgroundConfig.fromJson(msg.payload!),
              );
        }
      case WsMessageType.reboot:
        // Android reboot handled via platform channel if needed
        break;
      default:
        break;
    }
  });

  ref.onDispose(sub.cancel);
});
