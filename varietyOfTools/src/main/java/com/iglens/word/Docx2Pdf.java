package com.iglens.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Docx2Pdf {
    public static void main(String[] args) {
        try {
            // 加载docx文件
            FileInputStream fis = new FileInputStream("G:\\WeChat Files\\WeChat Files\\aion_my_god\\FileStorage\\File\\2024-01\\FyRlXVveIu_Report.docx");
            XWPFDocument document = new XWPFDocument(fis);

            // 设置PDF选项
            PdfOptions options = PdfOptions.create();

            // 将文件转换为PDF
            FileOutputStream fos = new FileOutputStream("output.pdf");
            PdfConverter.getInstance().convert(document, fos, options);

            // 关闭文档和输出流
            document.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}