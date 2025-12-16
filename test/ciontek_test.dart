import 'package:flutter_test/flutter_test.dart';
import 'package:ciontek/ciontek_platform_interface.dart';
import 'package:ciontek/ciontek_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:ciontek/models/ciontek_print_line.dart';

class MockCiontekPlatform
    with MockPlatformInterfaceMixin
    implements CiontekPlatform {
  @override
  Future<String?> printLine(CiontekPrintLine line) {
    throw UnimplementedError();
  }

  @override
  Future<void> setFontPath(String path) async {
    // no-op for tests
  }

  @override
  Future<String?> printLines(List<CiontekPrintLine> lines) {
    // Simple no-op implementation (adjust return type if needed based on actual interface)
    return Future<String?>.value(
        null); // Or throw UnimplementedError() if you prefer
  }
}

void main() {
  final CiontekPlatform initialPlatform = CiontekPlatform.instance;

  test('$MethodChannelCiontek is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelCiontek>());
  });
}
