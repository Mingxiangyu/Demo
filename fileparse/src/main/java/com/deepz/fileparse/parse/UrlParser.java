// package com.deepz.fileparse.parse;
//
// import com.deepz.fileparse.domain.dto.FileDto;
// import com.deepz.fileparse.domain.vo.StructableTxtVo;
// import java.net.MalformedURLException;
// import java.net.URL;
//
// /**
//  * @author xming
//  * @description
//  */
// @com.deepz.fileparse.annotation.Parser(fileType = "url")
// public class UrlParser implements Parser<StructableTxtVo> {
//   public static void main(String[] args) throws MalformedURLException {
//     UrlParser parser = new UrlParser();
//     String s = parser.parseToString(new URL("https://www.baidu.com"));
//     System.out.println(s);
//   }
//
//   /**
//    * @author xming
//    * @description
//    */
//   @Override
//   public StructableTxtVo parse(String path) {
//     StructableTxtVo txtVo = new StructableTxtVo();
//     txtVo.setContent(parseToString(path));
//     return txtVo;
//   }
//
//   /**
//    * @description
//    * @author xming
//    */
//   @Override
//   public StructableTxtVo parse(FileDto fileDto) {
//     StructableTxtVo txtVo = new StructableTxtVo();
//     txtVo.setContent(parseToString(fileDto.getInputStream()));
//     return txtVo;
//   }
// }
