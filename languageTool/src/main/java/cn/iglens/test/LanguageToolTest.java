package cn.iglens.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.language.Chinese;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;

/**
 * https://blog.51cto.com/u_13161667/4982369
 *
 * @author xming
 */
public class LanguageToolTest {

  private static void testEnglish() throws IOException {
    JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
    // comment in to use statistical ngram data:
    // langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
    List<RuleMatch> matches =
        langTool.check("A sentence with a error in the Hitchhiker's Guide tot he Galaxy");
    for (RuleMatch match : matches) {
      System.out.println(
          "Potential error at characters "
              + match.getFromPos()
              + "-"
              + match.getToPos()
              + ": "
              + match.getMessage());
      System.out.println("Suggested correction(s): " + match.getSuggestedReplacements());
    }

    for (Rule rule : langTool.getAllActiveRules()) {
      if (rule instanceof SpellingCheckRule) {
        List<String> wordsToIgnore = Arrays.asList("specialword", "myotherword");
        ((SpellingCheckRule) rule).addIgnoreTokens(wordsToIgnore);
      }
    }

    for (Rule rule : langTool.getAllActiveRules()) {
      if (rule instanceof SpellingCheckRule) {
        ((SpellingCheckRule) rule).acceptPhrases(Arrays.asList("foo bar", "producct namez"));
      }
    }
  }

  private static void testChinese() throws IOException {
    JLanguageTool langTool = new JLanguageTool(new Chinese());
    List<RuleMatch> matches =
        langTool.check(
            "如果您需要拼写检查并且您正在使用的语言有变体，您将需要在构造函数中指定该变体JLanguageTool，例如， new AmericanEnglish()而不仅仅是new English().\n"
                + "\n"
                + "要忽略单词，即从拼写检查中排除它们，请调用addIgnoreTokens(...) 您正在使用的拼写检查规则的方法。您首先必须通过迭代所有活动规则来找到规则。例子：！");
    for (RuleMatch match : matches) {
      System.out.println(
          "可能拼写错误 " + match.getFromPos() + "-" + match.getToPos() + ": " + match.getMessage());
      System.out.println("建议修正(s): " + match.getSuggestedReplacements());
    }
  }

  public static void main(String[] args) {
    try {
      long startTime = System.currentTimeMillis();

      // testEnglish();
      // System.out.println("testEnglish() cost=" + (System.currentTimeMillis() - startTime));

      testChinese();
      System.out.println("testChinese() cost=" + (System.currentTimeMillis() - startTime));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
