import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../providers/announcement_provider.dart';
import '../../data/models/announcement_data.dart';

class AnnouncementHost extends ConsumerStatefulWidget {
  final Widget child;

  const AnnouncementHost({super.key, required this.child});

  @override
  ConsumerState<AnnouncementHost> createState() => _AnnouncementHostState();
}

class _AnnouncementHostState extends ConsumerState<AnnouncementHost>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;
  late final Animation<Offset> _slide;
  Timer? _dismissTimer;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 350),
    );
    _slide = Tween<Offset>(
      begin: const Offset(0, 1),
      end: Offset.zero,
    ).animate(CurvedAnimation(parent: _controller, curve: Curves.easeOut));
  }

  @override
  Widget build(BuildContext context) {
    ref.listen<AnnouncementData?>(announcementProvider, (_, next) {
      _dismissTimer?.cancel();
      if (next != null) {
        _controller.forward(from: 0);
        _dismissTimer = Timer(Duration(seconds: next.durationSeconds), () {
          _controller.reverse().then((_) {
            if (mounted) ref.read(announcementProvider.notifier).dismiss();
          });
        });
      } else {
        _controller.reverse();
      }
    });

    final announcement = ref.watch(announcementProvider);

    return Stack(
      children: [
        widget.child,
        if (announcement != null)
          Positioned(
            bottom: 0,
            left: 0,
            right: 0,
            child: SlideTransition(
              position: _slide,
              child: _AnnouncementBar(data: announcement),
            ),
          ),
      ],
    );
  }

  @override
  void dispose() {
    _dismissTimer?.cancel();
    _controller.dispose();
    super.dispose();
  }
}

class _AnnouncementBar extends StatelessWidget {
  final AnnouncementData data;

  const _AnnouncementBar({required this.data});

  Color get _color => switch (data.priority) {
        AnnouncementPriority.info => const Color(0xFF1565C0),
        AnnouncementPriority.warning => const Color(0xFFE65100),
        AnnouncementPriority.promo => const Color(0xFFD4AF37),
      };

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(horizontal: 48, vertical: 18),
      color: _color.withValues(alpha: 0.92),
      child: Row(
        children: [
          Icon(
            switch (data.priority) {
              AnnouncementPriority.info => Icons.info_outline,
              AnnouncementPriority.warning => Icons.warning_amber_outlined,
              AnnouncementPriority.promo => Icons.local_offer_outlined,
            },
            color: Colors.white,
            size: 24,
          ),
          const SizedBox(width: 16),
          Expanded(
            child: Text(
              data.message,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 18,
                fontWeight: FontWeight.w400,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
