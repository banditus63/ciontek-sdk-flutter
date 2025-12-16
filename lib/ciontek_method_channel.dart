import 'package:ciontek/models/ciontek_print_line.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'ciontek_platform_interface.dart';

/// An implementation of [CiontekPlatform] that uses method channels.
class MethodChannelCiontek extends CiontekPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ciontek/printer');

  @override
  Future<String?> printLine(CiontekPrintLine line) async {
    final result =
        await methodChannel.invokeMethod<String>('print', line.toMap());
    return result;
  }

  @override
  Future<void> setFontPath(String path) async {
    await methodChannel.invokeMethod<void>(
      'setFontPath',
      {
        'path': path,
      },
    );
  }

  @override
  Future<String?> printLines(List<CiontekPrintLine> lines) async {
    final List<Map<String, dynamic>> lineMaps =
        lines.map((line) => line.toMap()).toList();
    return await methodChannel.invokeMethod('print', {'lines': lineMaps});
  }
}
