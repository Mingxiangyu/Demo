package com.iglens.llm.token;

import cn.hutool.core.util.StrUtil;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Token计算 {
    /**
     * registry实例
     */
    private static final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    public static void main(String[] args) {
        String text = "先获取"
            // + "    2. 然后根据 app_model 和 conversation 获取 app model config, 如果是 debug 模式，则允许用户传递的 model config 覆盖 据 app_model 和 conversation 查询出来的\n"
            + "    ";
        byEncodingTest(text);
    }



    /**
     * 通过Encoding计算
     */
    public static void byEncodingTest(String text) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.P50K_BASE);
        List<Integer> encode = Token计算.encode(enc, text);
        log.info(encode.toString());
        long tokens = Token计算.tokens(enc, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
    }


    /**
     * 通过Encoding和text获取编码数组
     *
     * @param enc  Encoding类型
     * @param text 文本信息
     * @return 编码数组
     */
    public static List<Integer> encode(@NotNull Encoding enc, String text) {
        return StrUtil.isBlank(text) ? new ArrayList<>() : enc.encode(text);
    }

    /**
     * 通过Encoding计算text信息的tokens
     *
     * @param enc  Encoding类型
     * @param text 文本信息
     * @return tokens数量
     */
    public static int tokens(@NotNull Encoding enc, String text) {
        return encode(enc, text).size();
    }


    /**
     * 通过Encoding和encoded数组反推text信息
     *
     * @param enc     Encoding
     * @param encoded 编码数组
     * @return 编码数组对应的文本信息
     */
    public static String decode(@NotNull Encoding enc, @NotNull List<Integer> encoded) {
        return enc.decode(encoded);
    }

    /**
     * 获取一个Encoding对象，通过Encoding类型
     *
     * @param encodingType encodingType
     * @return Encoding
     */
    public static Encoding getEncoding(@NotNull EncodingType encodingType) {
        return registry.getEncoding(encodingType);
    }

    /**
     * 获取encode的编码数组
     *
     * @param text 文本信息
     * @return 编码数组
     */
    public static List<Integer> encode(@NotNull EncodingType encodingType, String text) {
        if (StrUtil.isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(encodingType);
        return enc.encode(text);
    }


    /**
     * 通过EncodingType和encoded编码数组，反推字符串文本
     *
     * @param encodingType encodingType
     * @param encoded      编码数组
     * @return 编码数组对应的字符串
     */
    public static String decode(@NotNull EncodingType encodingType, @NotNull List<Integer> encoded) {
        Encoding enc = getEncoding(encodingType);
        return enc.decode(encoded);
    }


}


