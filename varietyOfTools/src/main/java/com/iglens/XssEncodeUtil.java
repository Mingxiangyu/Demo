package com.iglens;

public class XssEncodeUtil {

  public static boolean xssEncode(String s) {
    boolean isOn = true;
    if (s == null || s.isEmpty()) {
      return false;
    }
    StringBuilder sb = new StringBuilder(s.length() + 16);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\'':
          // 全角单引号
          // sb.append('‘');
          isOn = false;
          break;
        case '\"':
          // 全角双引号
          //sb.append('“');
          isOn = false;
          break;
        case '>':
          // 全角大于号
          // sb.append('＞');
          isOn = false;
          break;
        case '<':
          // 全角小于号
          //sb.append('＜');
          isOn = false;
          break;
        case '&':
          // 全角&符号
          //sb.append('＆');
          isOn = false;
          break;
        case '\\':
          // 全角斜线
          //sb.append('＼');
          isOn = false;
          break;
        case '#':
          // 全角井号
          //sb.append('＃');
          isOn = false;
          break;
        // < 字符的 URL 编码形式表示的 ASCII 字符（十六进制格式） 是: %3c
        case '~':
          isOn = false;
          break;
        case '@':
          isOn = false;
          break;
        case '$':
          isOn = false;
          break;
        case '^':
          isOn = false;
          break;
        case '*':
          isOn = false;
          break;
        case '+':
          isOn = false;
          break;
        case '?':
          isOn = false;
          break;
        case '|':
          isOn = false;
          break;
        case '}':
          isOn = false;
          break;
        case '{':
          isOn = false;
          break;
        case '“':
          isOn = false;
          break;
        case '”':
          isOn = false;
          break;
        case '‘':
          isOn = false;
          break;
        case '’':
          isOn = false;
          break;
        case '，':
          isOn = false;
          break;
        case '、':
          isOn = false;
          break;
        case '？':
          isOn = false;
          break;
        case '；':
          isOn = false;
          break;
        case ';':
          isOn = false;
          break;
        case ':':
          isOn = false;
          break;
        case '：':
          isOn = false;
          break;
        case '%':
          isOn = false;
          processUrlEncoder(sb, s, i);
          break;
        default:
          isOn = true;
          break;

      }
      if (s.contains("[]") || s.contains("--") || s.contains("--") || s.contains("()")
          || s.contains("《》")) {
        isOn = false;
      }

    }
    return isOn;
  }

  public static void processUrlEncoder(StringBuilder sb, String s, int index) {
    if (s.length() >= index + 2) {
      // %3c, %3C
      if (s.charAt(index + 1) == '3' && (s.charAt(index + 2) == 'c'
          || s.charAt(index + 2) == 'C')) {
        sb.append('＜');
        return;
      }
      // %3c (0x3c=60)
      if (s.charAt(index + 1) == '6' && s.charAt(index + 2) == '0') {
        sb.append('＜');
        return;
      }
      // %3e, %3E
      if (s.charAt(index + 1) == '3' && (s.charAt(index + 2) == 'e'
          || s.charAt(index + 2) == 'E')) {
        sb.append('＞');
        return;
      }
      // %3e (0x3e=62)
      if (s.charAt(index + 1) == '6' && s.charAt(index + 2) == '2') {
        sb.append('＞');
        return;
      }
    }
    sb.append(s.charAt(index));
  }
}
