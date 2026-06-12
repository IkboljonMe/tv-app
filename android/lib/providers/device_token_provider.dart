import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../data/repositories/device_repository.dart';
import 'storage_provider.dart';

final deviceRepositoryProvider = FutureProvider<DeviceRepository>((ref) async {
  final storage = await ref.watch(storageProvider.future);
  return DeviceRepository(storage);
});

final deviceTokenProvider = FutureProvider<String?>((ref) async {
  final repo = await ref.watch(deviceRepositoryProvider.future);
  return repo.getDeviceToken();
});
