import 'package:ciontek/models/ciontek_print_line.dart';
import 'ciontek_platform_interface.dart';

class CiontekPrinter {
  const CiontekPrinter();

  Future<String?> printLine({required CiontekPrintLine line}) {
    return CiontekPlatform.instance.printLine(line);
  }

  Future<void> setFontPath(String path) {
    return CiontekPlatform.instance.setFontPath(path);
  }

  Future<String?> printLines({required List<CiontekPrintLine> lines}) {
    return CiontekPlatform.instance.printLines(lines);
  }
}
