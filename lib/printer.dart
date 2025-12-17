import 'package:ciontek/models/ciontek_print_line.dart';
import 'ciontek_platform_interface.dart';
import 'dart:typed_data';

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

  Future<String?> printBitmap(Uint8List bytes,
      {CiontekTextAlignment alignment = CiontekTextAlignment.center}) {
    return printLine(
      line: CiontekPrintLine(
        type: CiontekPrintLineType.image,
        image: bytes,
        alignment: alignment,
      ),
    );
  }
}
