class RoomConfig {
  final String roomId;
  final String roomNumber;
  final GuestInfo? guest;
  final List<HotelService> services;
  final BackgroundConfig background;
  final HotelInfo hotel;
  final WeatherData? weather;

  const RoomConfig({
    required this.roomId,
    required this.roomNumber,
    this.guest,
    required this.services,
    required this.background,
    required this.hotel,
    this.weather,
  });

  bool get isOccupied => guest != null;

  factory RoomConfig.fromJson(Map<String, dynamic> json) => RoomConfig(
        roomId: json['roomId'] as String,
        roomNumber: json['roomNumber'] as String,
        guest: json['guest'] != null
            ? GuestInfo.fromJson(json['guest'] as Map<String, dynamic>)
            : null,
        services: (json['services'] as List<dynamic>? ?? [])
            .map((e) => HotelService.fromJson(e as Map<String, dynamic>))
            .toList(),
        background: json['background'] != null
            ? BackgroundConfig.fromJson(
                json['background'] as Map<String, dynamic>)
            : const BackgroundConfig(type: 'color', value: '#1a1a2e'),
        hotel: HotelInfo.fromJson(json['hotel'] as Map<String, dynamic>),
        weather: json['weather'] != null
            ? WeatherData.fromJson(json['weather'] as Map<String, dynamic>)
            : null,
      );

  RoomConfig copyWith({
    GuestInfo? guest,
    bool clearGuest = false,
    BackgroundConfig? background,
    List<HotelService>? services,
    WeatherData? weather,
  }) =>
      RoomConfig(
        roomId: roomId,
        roomNumber: roomNumber,
        guest: clearGuest ? null : (guest ?? this.guest),
        services: services ?? this.services,
        background: background ?? this.background,
        hotel: hotel,
        weather: weather ?? this.weather,
      );
}

class GuestInfo {
  final String name;
  final DateTime checkIn;
  final DateTime checkOut;
  final String language;

  const GuestInfo({
    required this.name,
    required this.checkIn,
    required this.checkOut,
    this.language = 'en',
  });

  factory GuestInfo.fromJson(Map<String, dynamic> json) => GuestInfo(
        name: json['name'] as String,
        checkIn: DateTime.parse(json['checkIn'] as String),
        checkOut: DateTime.parse(json['checkOut'] as String),
        language: json['language'] as String? ?? 'en',
      );
}

class HotelService {
  final String id;
  final String name;
  final String? iconUrl;
  final String? description;

  const HotelService({
    required this.id,
    required this.name,
    this.iconUrl,
    this.description,
  });

  factory HotelService.fromJson(Map<String, dynamic> json) => HotelService(
        id: json['id'] as String,
        name: json['name'] as String,
        iconUrl: json['icon'] as String?,
        description: json['description'] as String?,
      );
}

class BackgroundConfig {
  final String type; // 'image' | 'color'
  final String value;

  const BackgroundConfig({required this.type, required this.value});

  factory BackgroundConfig.fromJson(Map<String, dynamic> json) =>
      BackgroundConfig(
        type: json['type'] as String? ?? 'color',
        value: json['value'] as String? ?? '#1a1a2e',
      );
}

class HotelInfo {
  final String name;
  final String? logoUrl;
  final String timezone;
  final String language;

  const HotelInfo({
    required this.name,
    this.logoUrl,
    this.timezone = 'UTC',
    this.language = 'en',
  });

  factory HotelInfo.fromJson(Map<String, dynamic> json) => HotelInfo(
        name: json['name'] as String,
        logoUrl: json['logoUrl'] as String?,
        timezone: json['timezone'] as String? ?? 'UTC',
        language: json['language'] as String? ?? 'en',
      );
}

class WeatherData {
  final double temperatureCelsius;
  final String description;
  final String iconCode;
  final String city;

  const WeatherData({
    required this.temperatureCelsius,
    required this.description,
    required this.iconCode,
    required this.city,
  });

  factory WeatherData.fromJson(Map<String, dynamic> json) => WeatherData(
        temperatureCelsius:
            (json['temperatureCelsius'] as num).toDouble(),
        description: json['description'] as String,
        iconCode: json['icon'] as String? ?? '01d',
        city: json['city'] as String,
      );

  String get iconUrl =>
      'https://openweathermap.org/img/wn/$iconCode@2x.png';
  String get display => '${temperatureCelsius.round()}°C';
}
