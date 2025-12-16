import 'package:ciontek/models/ciontek_print_line.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'ciontek_method_channel.dart';

abstract class CiontekPlatform extends PlatformInterface {
  /// Constructs a CiontekPlatform.
  CiontekPlatform() : super(token: _token);

  static final Object _token = Object();

  static CiontekPlatform _instance = MethodChannelCiontek();

  /// The default instance of [CiontekPlatform] to use.
  ///
  /// Defaults to [MethodChannelCiontek].
  static CiontekPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [CiontekPlatform] when
  /// they register themselves.
  static set instance(CiontekPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> printLine(CiontekPrintLine line) {
    throw UnimplementedError('printLine() has not been implemented.');
  }

  Future<String?> printLines(List<CiontekPrintLine> lines) {
    throw UnimplementedError('printLines() has not been implemented.');
  }

  Future<void> setFontPath(String path) {
    throw UnimplementedError('setFontPath() has not been implemented.');
  }
}
