package com.iglens.pdf;

import com.spire.pdf.PdfDocument;

/**
 * @author xming
 * 链接：https://www.jianshu.com/p/1c875ff5e3f5
 */
public class Spire删除pdf指定页 {

  public static void main(String[] args) {

    // 创建PdfDocument对象并加载示例文档
    PdfDocument document = new PdfDocument();

    String filename = "C:\\Users\\zhouhuilin\\Desktop\\blog.csdn.net-Gogs-搭建自己的Git服务器.pdf";
    document.loadFromFile(filename);

    // 删除第二页
    document.getPages().removeAt(1);

    //添加一个空白页，目的为了删除jar包添加的水印，后面再移除这一页
    document.getPages().add();

    //为了删除jar包添加的水印 移除第一个页和最后一页
    // document.getPages().remove(document.getPages().get(0));
    document.getPages().remove(document.getPages().get(document.getPages().getCount()-1));

    // 保存文档
    document.saveToFile(filename);
  }
}
