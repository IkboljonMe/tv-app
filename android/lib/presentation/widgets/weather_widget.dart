import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import '../../data/models/room_config.dart';

class WeatherWidget extends StatelessWidget {
  final WeatherData weather;

  const WeatherWidget({super.key, required this.weather});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisSize: MainAxisSize.min,
      children: [
        Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            CachedNetworkImage(
              imageUrl: weather.iconUrl,
              width: 40,
              height: 40,
              errorWidget: (_, _, _) =>
                  const Icon(Icons.wb_cloudy, color: Colors.white54, size: 40),
            ),
            const SizedBox(width: 4),
            Text(
              weather.display,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 32,
                fontWeight: FontWeight.w200,
              ),
            ),
          ],
        ),
        Text(
          weather.description,
          style: const TextStyle(color: Colors.white54, fontSize: 13),
        ),
      ],
    );
  }
}
