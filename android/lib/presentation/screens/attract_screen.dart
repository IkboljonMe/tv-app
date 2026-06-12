import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../providers/room_config_provider.dart';
import '../widgets/clock_widget.dart';
import '../widgets/background_widget.dart';
import '../../data/models/room_config.dart';

class AttractScreen extends ConsumerWidget {
  const AttractScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final configAsync = ref.watch(roomConfigProvider);
    final config = configAsync.valueOrNull;

    final background = config?.background ??
        const BackgroundConfig(type: 'color', value: '#0d1117');
    final hotelName = config?.hotel.name ?? 'Hotel';
    final logoUrl = config?.hotel.logoUrl;

    return PopScope(
      canPop: false,
      child: BackgroundWidget(
        config: background,
        child: SafeArea(
          child: Stack(
            children: [
              // Top-right clock
              Positioned(
                top: 32,
                right: 48,
                child: const ClockWidget(),
              ),
              // Center content
              Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    if (logoUrl != null)
                      Image.network(
                        logoUrl,
                        height: 80,
                        errorBuilder: (_, _, _) =>
                            const Icon(Icons.hotel, color: Colors.white54, size: 80),
                      )
                    else
                      const Icon(Icons.hotel, color: Color(0xFFD4AF37), size: 72),
                    const SizedBox(height: 24),
                    Text(
                      hotelName,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 48,
                        fontWeight: FontWeight.w300,
                        letterSpacing: 6,
                      ),
                    ),
                    const SizedBox(height: 12),
                    Container(
                      width: 60,
                      height: 1,
                      color: const Color(0xFFD4AF37),
                    ),
                    const SizedBox(height: 16),
                    const Text(
                      'WELCOME',
                      style: TextStyle(
                        color: Color(0xFFD4AF37),
                        fontSize: 14,
                        letterSpacing: 10,
                        fontWeight: FontWeight.w300,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
