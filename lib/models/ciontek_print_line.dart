import 'dart:typed_data';

// nLevel：
// density level, value 1~5
// 1:Lowest 3：medium 5：Highest
enum TextGray { lowest, low, medium, high, highest }

enum CiontekPrintLineType {
  text,
  code128,
  code39,
  ean8,
  qrCode,
  pdf417,
  itf,
  image,
}

enum CiontekTextAlignment {
  left,
  center,
  right,
}

// Extension to easily get the integer value for the native side
extension CiontekTextAlignmentExtension on CiontekTextAlignment {
  int get value {
    switch (this) {
      case CiontekTextAlignment.left:
        return 0; // Common value for Left in printer SDKs
      case CiontekTextAlignment.center:
        return 1; // Common value for Center in printer SDKs
      case CiontekTextAlignment.right:
        return 2; // Common value for Right in printer SDKs
    }
  }
}

class CiontekPrintLine {
  final String text;
  final TextGray textGray;
  final bool bold;
  final bool underline;
  final CiontekPrintLineType type;
  final CiontekTextAlignment? alignment;
  final Uint8List? image;
  final double fontSize;

  CiontekPrintLine({
    this.text = '',
    this.textGray = TextGray.medium,
    this.bold = false,
    this.underline = false,
    this.type = CiontekPrintLineType.text,
    this.alignment,
    this.image,
    this.fontSize = 24.0,
  });

  // bitMap factory
  factory CiontekPrintLine.bitmap(Uint8List bytes,
      {CiontekTextAlignment alignment = CiontekTextAlignment.center}) {
    return CiontekPrintLine(
      type: CiontekPrintLineType.image,
      image: bytes,
      alignment: alignment,
    );
  }

  // QR Code factory
  factory CiontekPrintLine.qrCode(String data,
      {CiontekTextAlignment alignment = CiontekTextAlignment.center}) {
    return CiontekPrintLine(
      text: data,
      type: CiontekPrintLineType.qrCode,
      alignment: alignment,
    );
  }

  // Feed factory
  factory CiontekPrintLine.feedPaper({int lines = 1}) {
    return CiontekPrintLine(
      text: '\n ' * lines,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'text': text,
      'fontSize': fontSize,
      'textGray': textGray.index + 1,
      'bold': bold,
      'underline': underline,
      'alignment': alignment?.value,
      'image': image,
      'type': switch (type) {
        CiontekPrintLineType.text => 'TEXT',
        CiontekPrintLineType.code128 => 'CODE_128',
        CiontekPrintLineType.code39 => 'CODE_39',
        CiontekPrintLineType.ean8 => 'EAN_8',
        CiontekPrintLineType.qrCode => 'QR_CODE',
        CiontekPrintLineType.pdf417 => 'PDF_417',
        CiontekPrintLineType.itf => 'ITF',
        CiontekPrintLineType.image => 'IMAGE',
      },
    };
  }
}

extension PrintLineListExtension on List<CiontekPrintLine> {
  Map<String, dynamic> toMap() {
    return {
      "lines": map((e) => e.toMap()).toList(),
    };
  }
}
