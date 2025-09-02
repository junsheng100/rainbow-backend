package com.rainbow.system.utils;//package com.rainbow.sys.utils;
//
//public class WordToPdfConverter {
//
//
//  public static String toPdd(String inputPath) {
//    WordprocessingMLPackage wordMLPackage =
//            WordprocessingMLPackage.load(new File(inputPath));
//
//    // 中文字体映射
//    Mapper fontMapper = new BestMatchingMapper();
//    fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
//    fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
//    wordMLPackage.setFontMapper(fontMapper);
//
//    // 高性能转换配置
//    Docx4J.toPDF(wordMLPackage, new FileOutputStream(outputPath));
//
//  }
//
//}
