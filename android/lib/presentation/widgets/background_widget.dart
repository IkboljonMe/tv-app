import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import '../../data/models/room_config.dart';

class BackgroundWidget extends StatelessWidget {
  final BackgroundConfig config;
  final Widget child;

  const BackgroundWidget({
    super.key,
    required this.config,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    return Stack(
      fit: StackFit.expand,
      children: [
        _buildBackground(),
        // Dark gradient overlay for readability
        DecoratedBox(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Colors.black.withValues(alpha: 0.25),
                Colors.black.withValues(alpha: 0.55),
              ],
            ),
          ),
        ),
        child,
      ],
    );
  }

  Widget _buildBackground() {
    if (config.type == 'image') {
      return CachedNetworkImage(
        imageUrl: config.value,
        fit: BoxFit.cover,
        width: double.infinity,
        height: double.infinity,
        errorWidget: (_, _, _) => _colorBox(),
        placeholder: (_, _) => _colorBox(),
      );
    }
    return _colorBox();
  }

  Widget _colorBox() {
    Color color = const Color(0xFF0d1117);
    if (config.type == 'color') {
      try {
        final hex = config.value.replaceAll('#', '');
        color = Color(int.parse('FF$hex', radix: 16));
      } catch (_) {}
    }
    return ColoredBox(color: color);
  }
}
