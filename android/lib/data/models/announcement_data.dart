enum AnnouncementPriority { info, warning, promo }

class AnnouncementData {
  final String id;
  final String message;
  final AnnouncementPriority priority;
  final int durationSeconds;

  const AnnouncementData({
    required this.id,
    required this.message,
    this.priority = AnnouncementPriority.info,
    this.durationSeconds = 10,
  });

  factory AnnouncementData.fromJson(Map<String, dynamic> json) =>
      AnnouncementData(
        id: json['id'] as String? ??
            DateTime.now().millisecondsSinceEpoch.toString(),
        message: json['message'] as String,
        priority: _priorityFromString(json['priority'] as String?),
        durationSeconds: json['duration'] as int? ?? 10,
      );

  static AnnouncementPriority _priorityFromString(String? value) =>
      switch (value) {
        'warning' => AnnouncementPriority.warning,
        'promo' => AnnouncementPriority.promo,
        _ => AnnouncementPriority.info,
      };
}
