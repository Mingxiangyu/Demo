package org.demo.word.写word;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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
}