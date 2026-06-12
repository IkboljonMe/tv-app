import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../core/storage/app_storage.dart';
import '../core/storage/shared_prefs_storage.dart';

final storageProvider = FutureProvider<AppStorage>((ref) async {
  return SharedPrefsStorage.create();
});
