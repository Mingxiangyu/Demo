package org.demo.word;

import fr.opensagres.poi.xwpf.converter.core.BasicURIResolver;
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

//  public class POIUtils {
public class poi317word转html带图片格式 {

    private static final Logger log = LoggerFactory.getLogger(poi317word转html带图片格式.class.getName());

    /**
     * docx to html
     *
     * @param in 输入流
     * @return
     * @throws IOException
     */
    public static InputStream docxToHtml(InputStream in, String filePath) {
      XWPFDocument document = null;
      try {
        document = new XWPFDocument(in);
        XHTMLOptions options = XHTMLOptions.create();
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);
        filePath = filePath+"image"+File.separator;
        options.setExtractor(new FileImageExtractor(new File(filePath)));
        options.URIResolver(new BasicURIResolver("image"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, out, options);
        return new ByteArrayInputStream(out.toByteArray());
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      return in;
    }

    /**
     * doc to html
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream docToHtml(InputStream in) {
      try {
        HWPFDocument wordDocument = new HWPFDocument(in);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
            DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        return new ByteArrayInputStream(outStream.toByteArray());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return in;
    }

    public static InputStream docToHtml(Map<String, Object> request, InputStream in) {
      String fileName = (String) request.get("name");
      if (StringUtils.isEmpty(fileName)) {
        return in;
      }
      String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
      String prevName = fileName.substring(0, fileName.lastIndexOf("."));
      if ("html".equalsIgnoreCase(extensionName)) {
        return in;
      }
      if (extensionName.equalsIgnoreCase("doc")) {
        request.put("name", prevName + ".html");
        return docToHtml(in);
      }
      if (extensionName.equalsIgnoreCase("docx")) {
        request.put("name", prevName + ".html");
        return docxToHtml(in, (String)request.get("filePath"));
      }
      return in;
    }

    public static void inputStreamToFile(InputStream inputStream, String newPath) {
//		InputStream inputStream = null;
      OutputStream outputStream = null;
      try {
        /**
         String userDir = System.getProperty("user.dir");
         String path = userDir + "/bin/file.xml";
         inputStream = new FileInputStream(path);
         */

//			String newPath = userDir + "/bin/file-new.xml";
        File file = new File(newPath);
        outputStream = new FileOutputStream(file);

        int bytesWritten = 0;
        int byteCount = 0;

        byte[] bytes = new byte[1024];

        while ((byteCount = inputStream.read(bytes)) != -1) {
          outputStream.write(bytes, bytesWritten, byteCount);
//				bytesWritten += byteCount;
        }

        System.out.println("Done!");
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (inputStream != null) {
          try {
            inputStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (outputStream != null) {
          try {
            outputStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }

        }
      }

    }

    public static void main(String[] args) {
      try {
        String fileName = "展示型网站开发培训方案.docx";
        String newfileName = "展示型网站开发培训方案.html";
        String filePath = "D:\\Temp\\";
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("name", fileName);
        request.put("filePath", filePath);
        InputStream in = new FileInputStream(filePath + fileName);// 读取文件的数据。
        InputStream result = docToHtml(request, in);
        inputStreamToFile(result, filePath+newfileName);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
}
