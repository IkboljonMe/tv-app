import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../data/models/announcement_data.dart';

class AnnouncementNotifier extends StateNotifier<AnnouncementData?> {
  AnnouncementNotifier() : super(null);

  void show(AnnouncementData data) => state = data;
  void dismiss() => state = null;
}

final announcementProvider =
    StateNotifierProvider<AnnouncementNotifier, AnnouncementData?>(
  (_) => AnnouncementNotifier(),
);
