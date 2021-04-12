package org.demo.word.html转word;


import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

  /**
   * 自定义 XWPFDocument，并重写 createPicture()方法
   */
  public class CustomXWPFDocument extends XWPFDocument {
    public CustomXWPFDocument(InputStream in) throws IOException {
      super(in);
    }

    public CustomXWPFDocument() {
      super();
    }

    public CustomXWPFDocument(OPCPackage pkg) throws IOException {
      super(pkg);
    }

    /**
     * @param ind
     * @param width 宽
     * @param height 高
     * @param paragraph  段落
     */
    public void createPicture(String blipId, int ind, int width, int height,XWPFParagraph paragraph) {
      final int EMU = 9525;
      width *= EMU;
      height *= EMU;
      CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
      String picXml = ""
          + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
          + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
          + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
          + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
          + ind
          + "\" name=\"Generated\"/>"
          + "            <pic:cNvPicPr/>"
          + "         </pic:nvPicPr>"
          + "         <pic:blipFill>"
          + "            <a:blip r:embed=\""
          + blipId
          + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
          + "            <a:stretch>"
          + "               <a:fillRect/>"
          + "            </a:stretch>"
          + "         </pic:blipFill>"
          + "         <pic:spPr>"
          + "            <a:xfrm>"
          + "               <a:off x=\"0\" y=\"0\"/>"
          + "               <a:ext cx=\""
          + width
          + "\" cy=\""
          + height
          + "\"/>"
          + "            </a:xfrm>"
          + "            <a:prstGeom prst=\"rect\">"
          + "               <a:avLst/>"
          + "            </a:prstGeom>"
          + "         </pic:spPr>"
          + "      </pic:pic>"
          + "   </a:graphicData>" + "</a:graphic>";

      inline.addNewGraphic().addNewGraphicData();
      XmlToken xmlToken = null;
      try {
        xmlToken = XmlToken.Factory.parse(picXml);
      } catch (XmlException xe) {
        xe.printStackTrace();
      }
      inline.set(xmlToken);

      inline.setDistT(0);
      inline.setDistB(0);
      inline.setDistL(0);
      inline.setDistR(0);

      CTPositiveSize2D extent = inline.addNewExtent();
      extent.setCx(width);
      extent.setCy(height);

      CTNonVisualDrawingProps docPr = inline.addNewDocPr();
      docPr.setId(ind);
      docPr.setName("图片" + ind);
      docPr.setDescr("测试");
    }
  }

