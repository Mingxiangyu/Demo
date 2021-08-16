package org.demo.接口文档;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;

/**
 * 接口文档自动生成工具
 * @author Ray。
 */
public class DocUtil {

	public static void main(String[] args) {
		DocsConfig config = new DocsConfig();
    // 项目根目录
    config.setProjectPath("F:\\gitWorkSpace\\Demo\\varietyOfTools");
		// 项目名称
		config.setProjectName("生成文档");
		// 声明该API的版本
		config.setApiVersion("V1.0.3");
		//生成API 文档所在目录
		config.setDocsPath("D:\\新建文件夹2");
		// 配置自动生成//关闭自动生成config.setAutoGenerate(Boolean.FALSE)，使用@ApiDoc 来一个个接口导出排查问题。
		config.setAutoGenerate(Boolean.TRUE);
		// 执行生成文档
		config.addPlugin(new MarkdownDocPlugin());
		//解决反射异常(空指针异常)
		config.setOpenReflection(Boolean.FALSE);
		Docs.buildHtmlDocs(config);
	}
}