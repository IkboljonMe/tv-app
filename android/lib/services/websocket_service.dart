import 'dart:async';
import 'dart:convert';
import 'package:web_socket_channel/web_socket_channel.dart';
import '../core/constants.dart';

enum WsMessageType {
  refreshConfig,
  clearGuest,
  showAnnouncement,
  updateBackground,
  reboot,
  ping,
  pong,
  unknown,
}

class WsMessage {
  final WsMessageType type;
  final Map<String, dynamic>? payload;

  const WsMessage({required this.type, this.payload});

  static WsMessageType _typeFromString(String s) => switch (s) {
        'REFRESH_CONFIG' => WsMessageType.refreshConfig,
        'CLEAR_GUEST' => WsMessageType.clearGuest,
        'SHOW_ANNOUNCEMENT' => WsMessageType.showAnnouncement,
        'UPDATE_BACKGROUND' => WsMessageType.updateBackground,
        'REBOOT' => WsMessageType.reboot,
        'PING' => WsMessageType.ping,
        'PONG' => WsMessageType.pong,
        _ => WsMessageType.unknown,
      };

  factory WsMessage.fromJson(Map<String, dynamic> json) => WsMessage(
        type: _typeFromString(json['type'] as String? ?? ''),
        payload: json['payload'] as Map<String, dynamic>?,
      );
}

class WebSocketService {
  final String _deviceToken;
  WebSocketChannel? _channel;
  StreamSubscription? _subscription;
  final StreamController<WsMessage> _messageController =
      StreamController<WsMessage>.broadcast();
  Timer? _pingTimer;
  Timer? _pongTimeoutTimer;
  Timer? _reconnectTimer;
  int _reconnectAttempts = 0;
  bool _disposed = false;

  static const _backoffSeconds = [1, 2, 4, 8, 16, 30];

  WebSocketService(this._deviceToken);

  Stream<WsMessage> get messages => _messageController.stream;

  void connect() {
    if (_disposed) return;
    _doConnect();
  }

  void _doConnect() {
    if (_disposed) return;
    final uri = Uri.parse(
        '${AppConstants.wsUrl}/api/v1/ws?token=$_deviceToken');
    try {
      _channel = WebSocketChannel.connect(uri);
      _subscription = _channel!.stream.listen(
        _onData,
        onDone: _scheduleReconnect,
        onError: (_) => _scheduleReconnect(),
      );
      _reconnectAttempts = 0;
      _startPing();
    } catch (_) {
      _scheduleReconnect();
    }
  }

  void _onData(dynamic data) {
    _cancelPongTimeout();
    if (data is! String) return;
    try {
      final json = jsonDecode(data) as Map<String, dynamic>;
      final msg = WsMessage.fromJson(json);
      if (msg.type == WsMessageType.ping) {
        _channel?.sink.add(jsonEncode({'type': 'PONG'}));
      }
      _messageController.add(msg);
    } catch (_) {}
  }

  void _startPing() {
    _pingTimer?.cancel();
    _pingTimer = Timer.periodic(const Duration(seconds: 30), (_) {
      _channel?.sink.add(jsonEncode({'type': 'PING'}));
      _pongTimeoutTimer = Timer(const Duration(seconds: 10), _scheduleReconnect);
    });
  }

  void _cancelPongTimeout() {
    _pongTimeoutTimer?.cancel();
    _pongTimeoutTimer = null;
  }

  void _scheduleReconnect() {
    if (_disposed) return;
    _pingTimer?.cancel();
    _subscription?.cancel();
    _channel?.sink.close();

    final delay = _backoffSeconds[
        _reconnectAttempts.clamp(0, _backoffSeconds.length - 1)];
    _reconnectAttempts++;

    _reconnectTimer?.cancel();
    _reconnectTimer = Timer(Duration(seconds: delay), _doConnect);
  }

  void dispose() {
    _disposed = true;
    _pingTimer?.cancel();
    _pongTimeoutTimer?.cancel();
    _reconnectTimer?.cancel();
    _subscription?.cancel();
    _channel?.sink.close();
    _messageController.close();
  }
}
