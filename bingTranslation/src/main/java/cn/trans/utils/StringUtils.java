package cn.trans.utils;

import java.net.URLEncoder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 字符串工具类
 */
@Slf4j
public class StringUtils extends org.apache.commons.lang.StringUtils {

    private final static String REGREX = "[\\u200b-\\u200f]|[\\u200e-\\u200f]|[\\u202a-\\u202e]|[\\u2066-\\u2069]|\ufeff|\u06ec";

    private final static String BLANK = "";

    public static String replaceAll(String source) {
        return replaceAll(source, REGREX, BLANK);
    }

    public static String replaceAll(String source, String regex, String replacement) {
        if (regex == null) {
            regex = REGREX;
        }
        if (replacement == null) {
            replacement = BLANK;
        }
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(source);
        if (matcher.find()) {
            return matcher.replaceAll( Matcher.quoteReplacement(replacement));
        }
        return source;
    }

    public static String replaceAll(String source, String regex, Function<String,String> fun) {
        if (regex == null) {
            regex = REGREX;
        }

        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(source);
        StringBuilder result = new StringBuilder(source);
        while (matcher.find()) {
            String oldString = matcher.group(1);
            String newString = fun.apply(oldString);
            int start = result.indexOf(oldString);
            if(null != newString) {
                result.replace(start, start + oldString.length(), newString);
            }
        }
        return result.toString();
    }

    public static String filterXmlNonValidChar(String txt) {
        txt = txt.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", " ");
        return txt.replaceAll("\\&", "\\ ");
    }

    public static void stripXmlNonValidCharacters(char[] in, int length) {

        for (int i = 0; i < length; i++) {
            char current = in[i];
            if (((current >= 0x00) && (current <= 0x08))
                    || ((current >= 0x0b) && (current <= 0x0c))
                    || ((current >= 0x0e) && (current <= 0x1f))
                    || (current == '&' && i > 0 && in[i - 1] == '\\')) {
                in[i] = ' ';
            }
        }
    }

    /**
     * 根据变量表达式（例："{id}"或"{info.name}"）替换内容
     *
     * @param s
     * @param map
     * @return
     */
    public static String replaceVariables(String s, Map<String, String> map) {
        final String[] ss = new String[]{s};
        map.forEach((key, value) -> ss[0] = ss[0].replaceAll("\\{\\s*" + key.replaceAll("\\.", "\\\\.") + "\\s*\\}", value));
        return ss[0];
    }

    /**
     * 将给定字符串转换为Get方式请求参数
     *
     * @param value
     * @return
     */
    @SneakyThrows
    public static String toUrlParam(String value) {
        if (StringUtils.isNotBlank(value)) {
            return URLEncoder.encode(StringUtils.trim(value), "UTF-8");
        }
        return "";
    }

    /**
     * 格式化文本，删除多余换行，行首缩进
     *
     * @param text
     * @return
     */
    public static String formatText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }

        text = text.replaceAll("。 ", "。\n");

        String[] textLines = text.split("[\r\n\t]");
        return Arrays.asList(textLines)
                .stream()
                .map(line -> {
                    line = StringUtils.trim(line);
                    if (StringUtils.isNotBlank(line)) {
                        line = line
                                .replace("　　 ", "")
                                .replaceAll("[　　 ]+", "");
                    }
                    return line;
                })
                .filter(line -> StringUtils.isNotBlank(line))
                .map(line -> "　　" + line)
                .collect(Collectors.joining("\n"));
    }

    public static boolean isMatch(String text, String reg) {
        return isNotBlank(match(text, reg, 0));
    }

    public static String match(String text, String reg) {
        return match(text, reg, 1);
    }

    public static String match(String text, String reg, int groupType) {
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(groupType);
        }
        return null;
    }

    public static List<String> matchs(String text, String reg) {
        return matchs(text, reg, 1);
    }


    public static List<String> matchs(String text, String reg, int groupType) {
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        List<String> value = new ArrayList<>();
        while (m.find()) {
            value.add(m.group(groupType));
        }
        return value;
    }

    public static String findByPair(String text, String findStart) {
        Assert.isNull(text, "原始字符串不能为空");
        Assert.isNull(findStart, "搜索开始字符串不能为空");

        StringBuilder tsb = new StringBuilder(text);

        int startIndex = StringUtils.matchAndGetIndex(text,"\\{\\{" + findStart);
        if (startIndex > -1) {

            Deque<String> stack = new ArrayDeque<>();
            Pattern p = Pattern.compile("([\\{\\}]{2})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(text.substring(startIndex));
            int endIndex = -1;
            while (m.find()) {
                endIndex = m.start(0);
                String s = m.group(0);
                if ("{{".equals(s)) {
                    stack.push(s);
                } else if ("}}".equals(s)) {
                    stack.pop();
                }

                if (stack.isEmpty()) {
                    break;
                }
            }
            if (endIndex > -1) {
                String substr = text.substring(startIndex).substring(0, endIndex + 2);
                return substr;
            }
        }
        return null;
    }

    public static int matchAndGetIndex(String text, String reg) {
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.start(0);
        }
        return -1;
    }

    public static List<String> findsByPair(String text, String findStart) {
        Assert.isNull(text, "原始字符串不能为空");
        Assert.isNull(findStart, "搜索开始字符串不能为空");

        List<String> results = new ArrayList<>();
        int startIndex = StringUtils.matchAndGetIndex(text,"\\{\\{" + findStart);
        if (startIndex > -1) {
            while (true) {
                Deque<String> stack = new ArrayDeque<>();
                Pattern p = Pattern.compile("([\\{\\}]{2})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(text.substring(startIndex));
                int endIndex = -1;
                while (m.find()) {
                    endIndex = m.start(0);
                    String s = m.group(0);
                    if ("{{".equals(s)) {
                        stack.push(s);
                    } else if ("}}".equals(s)) {
                        if(!stack.isEmpty()) {
                            stack.pop();
                        } else {
                            break;
                        }
                    }

                    if (stack.isEmpty()) {
                        break;
                    }
                }
                if (endIndex > -1) {
                    String substr = text.substring(startIndex).substring(0, endIndex + 2);
                    results.add(substr);
                    startIndex += endIndex + 2;
                } else {
                    break;
                }
            }
        }
        return results;
    }

    /**
     * 根据指定正则分割，并保留分割字符
     * @param text
     * @param reg
     * @return
     */
    public static List<String> splitBehind(String text, String reg){
        List<String> result = new ArrayList<>();
        StringBuilder subString = new StringBuilder();
        for(int i = 0; i< text.length(); i++) {
            String chat = text.substring(i, i+1);
            if(chat.matches(reg)) {
                subString.append(chat);
                if(subString.length() > 0) {
                    result.add(subString.toString());
                    if(i < text.length() - 1) {
                        subString = new StringBuilder();
                    }
                }
            }
        }
        return result;
    }
}
