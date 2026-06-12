import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'providers/app_screen_provider.dart';
import 'presentation/screens/splash_screen.dart';
import 'presentation/screens/provisioning_screen.dart';
import 'presentation/screens/attract_screen.dart';
import 'presentation/screens/welcome_screen.dart';
import 'presentation/widgets/announcement_overlay.dart';

class HotelTvApp extends StatelessWidget {
  const HotelTvApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Hotel TV',
      debugShowCheckedModeBanner: false,
      theme: ThemeData.dark().copyWith(
        scaffoldBackgroundColor: Colors.black,
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFFD4AF37),
          surface: Colors.black,
        ),
      ),
      home: const AnnouncementHost(child: AppRouter()),
    );
  }
}

class AppRouter extends ConsumerWidget {
  const AppRouter({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final appScreen = ref.watch(appScreenProvider);
    return appScreen.when(
      data: (screen) => switch (screen) {
        AppScreen.splash => const SplashScreen(),
        AppScreen.provisioning => const ProvisioningScreen(),
        AppScreen.attract => const AttractScreen(),
        AppScreen.welcome => const WelcomeScreen(),
      },
      loading: () => const SplashScreen(),
      error: (_, _) => const ProvisioningScreen(),
    );
  }
}
