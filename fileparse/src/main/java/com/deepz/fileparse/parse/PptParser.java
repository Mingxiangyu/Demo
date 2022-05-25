// package com.deepz.fileparse.parse;
//
// import com.deepz.fileparse.domain.dto.FileDto;
// import com.deepz.fileparse.domain.vo.StructablePptVo;
//
// /**
//  * @author xming
//  * @description
//  */
// @com.deepz.fileparse.annotation.Parser(fileType = {"ppt", "pptx"})
// public class PptParser implements Parser<StructablePptVo> {
//
//   @Override
//   public StructablePptVo parse(String path) {
//     String text = parseToString(path);
//     StructablePptVo pptVo = new StructablePptVo();
//     pptVo.setContent(text);
//     return pptVo;
//   }
//
//   /**
//    * @description
//    * @author xming
//    */
//   @Override
//   public StructablePptVo parse(FileDto fileDto) {
//     StructablePptVo vo = new StructablePptVo();
//     String text = parseToString(fileDto.getInputStream());
//     vo.setContent(text);
//     return vo;
//   }
// }
