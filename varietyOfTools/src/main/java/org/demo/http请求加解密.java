//package org.demo;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.security.MessageDigest;
//import java.security.SecureRandom;
//import java.util.HashMap;
//import java.util.Map;
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Hex;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//public class http请求加解密 {
//  @RestController
//  public  class  Controller1  {
//
//        @Autowired
//        private ObjectMapper objectMapper;
//
//        @PostMapping(value  =  "/order/save",
//                                consumes  =  MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//                                produces  =  MediaType.APPLICATION_JSON_UTF8_VALUE)
//        public  ResponseEntity  saveOrder(@RequestParam(name  =  "sign")  String  sign,
//                                                                                                    @RequestParam(name  =  "timestamp")  Long  timestamp,
//                                                                                                    @RequestParam(name  =  "data")  String  data)  throws  Exception  {
//                EncryptModel  model  =  new  EncryptModel();
//                model.setData(data);
//                model.setTimestamp(timestamp);
//                model.setSign(sign);
//                String  inRawSign  =  String.format("data=%s&timestamp=%d",  model.getData(),  model.getTimestamp());
//                String  inSign  =  EncryptUtils.SINGLETON.sha(inRawSign);
//                if  (!inSign.equals(model.getSign())){
//                        throw  new  IllegalArgumentException("验证参数签名失败!");
//                }
//                //这里忽略实际的业务逻辑,简单设置返回的data为一个map
//                Map result  =  new HashMap<>(8);
//                result.put("code",  "200");
//                result.put("message",  "success");
//                EncryptModel  out  =  new  EncryptModel();
//                out.setTimestamp(System.currentTimeMillis());
//                out.setData(EncryptUtils.SINGLETON.encryptByAes(objectMapper.writeValueAsString(result)));
//                String  rawSign  =  String.format("data=%s&timestamp=%d",  out.getData(),  out.getTimestamp());
//                out.setSign(EncryptUtils.SINGLETON.sha(rawSign));
//                return  ResponseEntity.ok(out);
//        }
//
//        @PostMapping(value  =  "/order/query",
//                                consumes  =  MediaType.APPLICATION_JSON_VALUE,
//                                produces  =  MediaType.APPLICATION_JSON_UTF8_VALUE)
//        public  ResponseEntity    queryOrder(@RequestBody User  user){
//                Order  order  =  new  Order();
//                //这里忽略实际的业务逻辑
//                return  ResponseEntity.ok(order);
//        }
//  }
//
//}
//
//enum EncryptUtils {
//
//        /**
//      * SINGLETON
//      */
//        SINGLETON;
//
//        private static final String SECRET = "throwable";
//        private static final String CHARSET = "UTF-8";
//
//        public String sha(String raw) throws Exception {
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//            messageDigest.update(raw.getBytes(CHARSET));
//            return Hex.encodeHexString(messageDigest.digest());
//        }
//
//        private Cipher createAesCipher() throws Exception {
//            return Cipher.getInstance("AES");
//        }
//
//        public String encryptByAes(String raw) throws Exception {
//            Cipher aesCipher = createAesCipher();
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128, new SecureRandom(SECRET.getBytes(CHARSET)));
//            SecretKey secretKey = keyGenerator.generateKey();
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
//            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//            byte[] bytes = aesCipher.doFinal(raw.getBytes(CHARSET));
//            return Hex.encodeHexString(bytes);
//        }
//
//        public String decryptByAes(String raw) throws Exception {
//            byte[] bytes = Hex.decodeHex(raw);
//            Cipher aesCipher = createAesCipher();
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128, new SecureRandom(SECRET.getBytes(CHARSET)));
//            SecretKey secretKey = keyGenerator.generateKey();
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
//            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//            return new String(aesCipher.doFinal(bytes), CHARSET);
//        }
//    }
//
//
