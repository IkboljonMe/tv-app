import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../providers/room_config_provider.dart';
import '../widgets/clock_widget.dart';
import '../widgets/weather_widget.dart';
import '../widgets/service_grid.dart';
import '../widgets/background_widget.dart';
import '../../data/models/room_config.dart';

class WelcomeScreen extends ConsumerWidget {
  const WelcomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final config = ref.watch(roomConfigProvider).valueOrNull;
    if (config == null) {
      return const Scaffold(
        backgroundColor: Colors.black,
        body: Center(child: CircularProgressIndicator(color: Color(0xFFD4AF37))),
      );
    }

    final guest = config.guest;
    final background = config.background;
    final hotel = config.hotel;

    return PopScope(
      canPop: false,
      child: BackgroundWidget(
        config: background,
        child: SafeArea(
          child: Stack(
            children: [
              // Top-left: hotel logo
              Positioned(
                top: 28,
                left: 48,
                child: _HotelLogo(logoUrl: hotel.logoUrl, name: hotel.name),
              ),
              // Top-right: clock + weather
              Positioned(
                top: 24,
                right: 48,
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    if (config.weather != null) ...[
                      WeatherWidget(weather: config.weather!),
                      const SizedBox(width: 32),
                    ],
                    const ClockWidget(),
                  ],
                ),
              ),
              // Center: welcome message
              if (guest != null)
                Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Text(
                        'WELCOME',
                        style: TextStyle(
                          color: Color(0xFFD4AF37),
                          fontSize: 16,
                          letterSpacing: 10,
                          fontWeight: FontWeight.w300,
                        ),
                      ),
                      const SizedBox(height: 12),
                      Text(
                        guest.name,
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 64,
                          fontWeight: FontWeight.w200,
                          letterSpacing: 2,
                        ),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 16),
                      _StayDates(guest: guest),
                    ],
                  ),
                ),
              // Bottom: service grid
              if (config.services.isNotEmpty)
                Positioned(
                  bottom: 40,
                  left: 0,
                  right: 0,
                  child: ServiceGrid(services: config.services),
                ),
            ],
          ),
        ),
      ),
    );
  }
}

class _HotelLogo extends StatelessWidget {
  final String? logoUrl;
  final String name;

  const _HotelLogo({this.logoUrl, required this.name});

  @override
  Widget build(BuildContext context) {
    if (logoUrl != null) {
      return Image.network(
        logoUrl!,
        height: 48,
        errorBuilder: (_, _, _) => _textLogo(),
      );
    }
    return _textLogo();
  }

  Widget _textLogo() => Text(
        name,
        style: const TextStyle(
          color: Colors.white,
          fontSize: 22,
          fontWeight: FontWeight.w300,
          letterSpacing: 3,
        ),
      );
}

class _StayDates extends StatelessWidget {
  final GuestInfo guest;

  const _StayDates({required this.guest});

  @override
  Widget build(BuildContext context) {
    final fmt = DateFormat('MMM d');
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 10),
      decoration: BoxDecoration(
        color: Colors.black.withValues(alpha: 0.3),
        borderRadius: BorderRadius.circular(24),
        border: Border.all(color: Colors.white12),
      ),
      child: Text(
        '${fmt.format(guest.checkIn)}  →  ${fmt.format(guest.checkOut)}',
        style: const TextStyle(
          color: Colors.white70,
          fontSize: 16,
          letterSpacing: 2,
        ),
      ),
    );
  }
}
