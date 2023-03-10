package cn.trans.services.impl;

import cn.trans.eunms.TransLangEnum;
import cn.trans.feign.MicrosoftTransClient;
import cn.trans.model.MicrosoftTransParamItem;
import cn.trans.model.MicrosoftTransResultItem;
import cn.trans.model.TransContrastResult;
import cn.trans.model.TransParam;
import cn.trans.services.MicrosoftTransService;
import cn.trans.utils.StringUtils;
import com.carrotsearch.labs.langid.LangIdV3;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微软文本翻译服务实现
 *
 * @author mxy
 * @Date 2022-12-22
 */
@Service
@Slf4j
public class MicrosoftTransServiceImpl implements MicrosoftTransService {

    @Autowired
    private MicrosoftTransClient microsoftTransClient;

    @Override
    public String transForString(TransParam param) {

        this.initLangDetect(param);

        /**
         * 将查询内容拆解为支持最大字符以内
         */
        List<String> lines = this.splitQueryString(param.getQ());

        List<MicrosoftTransParamItem> itemsParam = lines.stream().map(line -> {
            MicrosoftTransParamItem item = new MicrosoftTransParamItem();
            item.setText(line);
            return item;
        }).collect(Collectors.toList());
        List<MicrosoftTransResultItem> itemResults = this.translationAutoSplitCharLength(itemsParam, param);
        String result = itemResults.stream().map(item -> {
            String toText = item.getTranslations().get(0).getText();
            return toText;
        }).collect(Collectors.joining("\n"));
        return result;
    }

    /**
     * 公网对照翻译
     *
     * @param param 翻译参数
     * @return
     */
    @SneakyThrows
    @Override
    public TransContrastResult translationForContrast(TransParam param) {

        StringBuilder srcBuilder = new StringBuilder();
        StringBuilder dstBuilder = new StringBuilder();
        AtomicInteger identity = new AtomicInteger(-1);

        this.initLangDetect(param);

        /**
         * 将查询内容拆解为支持最大字符以内
         */
        List<String> lines = this.splitQueryString(param.getQ());

        List<MicrosoftTransParamItem> itemsParam = lines.stream().map(line -> {
            MicrosoftTransParamItem item = new MicrosoftTransParamItem();
            item.setText(line);
            return item;
        }).collect(Collectors.toList());
        List<MicrosoftTransResultItem> itemResults = this.translationAutoSplitCharLength(itemsParam, param);
        for (int i = 0, size = itemResults.size(); i < size; i++) {
            int id = identity.incrementAndGet();

            String srcText = lines.get(i);
            srcBuilder.append("<span identity='").append(id).append("'>").append(srcText).append("</span>");
            srcBuilder.append("\n");

            String toText = itemResults.get(i).getTranslations().get(0).getText();
            dstBuilder.append("<span identity='").append(id).append("'>").append(toText).append("</span>");
            dstBuilder.append("\n");
        }

        TransContrastResult result = new TransContrastResult();
        result.setFrom(param.getFrom());
        result.setTo(param.getTo());
        result.setSrc(StringUtils.formatText(srcBuilder.toString()));
        result.setDst(StringUtils.formatText(dstBuilder.toString()));
        return result;
    }

    private List<MicrosoftTransResultItem> translationAutoSplitCharLength(List<MicrosoftTransParamItem> itemsParam, TransParam param) {

        List<List<MicrosoftTransParamItem>> itemsParams = new ArrayList<>();
        List<MicrosoftTransParamItem> newItemsParam = null;
        int currentLength = 0;
        for (MicrosoftTransParamItem item : itemsParam) {

            String text = item.getText();
            int length = text.length();

            if (currentLength + length > 1000) {
                newItemsParam = null;
                currentLength = 0;
            }

            if (null == newItemsParam) {
                newItemsParam = new ArrayList<>();
                itemsParams.add(newItemsParam);
                currentLength = 0;
            }

            newItemsParam.add(item);
            currentLength += length;
        }

        List<MicrosoftTransResultItem> itemResults = itemsParams
                .parallelStream()
                .map(subItemsParam ->
                        microsoftTransClient.translation(subItemsParam, param.getFrom(), param.getTo(), "3.0", true)
                ).flatMap(subItemsResult ->
                        subItemsResult.stream()
                ).collect(Collectors.toList());
        return itemResults;
    }

    /**
     * 语种识别
     *
     * @param text
     * @return
     */
    @Override
    public String detectedLanguage(String text) {
        LangIdV3 langid = new LangIdV3();
        langid.append(StringUtils.substring(text, 0, 1000));
        return langid.classify(true).langCode;
    }

    /**
     * 初始化文本语种
     *
     * @param param
     */
    @SneakyThrows
    private void initLangDetect(TransParam param) {
        // 若没指定语种，则先查询语种
        if (StringUtils.isBlank(param.getFrom()) || TransLangEnum.AUTO.getKey().equals(param.getFrom())) {
            String text = param.getQ();
            if (StringUtils.isNotBlank(text)) {
                try (StringReader sr = new StringReader(text);
                     BufferedReader br = new BufferedReader(sr)) {
                    text = br.lines().map(line -> StringUtils.trim(line)).filter(line -> StringUtils.isNotBlank(line)).collect(Collectors.joining("\n"));
                }
            }
            String lang = this.detectedLanguage(text);
            param.setFrom(lang);
        }

        if (StringUtils.isBlank(param.getTo())) {
            param.setTo("zh-CN");
        }
    }

    /**
     * 将查询内容拆解为支持最大字符以内
     */
    @SneakyThrows
    private List<String> splitQueryString(String text) {
        BufferedReader br = new BufferedReader(new StringReader(text));
        List<String> lines = new ArrayList<>();
        String line;
        while (null != (line = br.readLine())) {
            lines.add(line);
        }
        return lines;
    }

}
