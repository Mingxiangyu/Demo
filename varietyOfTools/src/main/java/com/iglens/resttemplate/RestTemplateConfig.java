
package com.iglens.resttemplate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean(value = "clientHttpRequestFactory")
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() throws InterruptedException {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(1000 * 60 * 60 * 2);//单位为ms
        factory.setConnectTimeout(1000 * 60 * 60 * 2);//单位为ms
        return factory;
    }

    //    @Bean(name = "preRestTemplate")
    @Bean
    public RestTemplate restTemplate(@Qualifier("clientHttpRequestFactory") ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        // restTemplate.setErrorHandler(new CustomErrorHandler());
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        mediaTypeList.add(MediaType.TEXT_XML);
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        mediaTypeList.add(MediaType.parseMediaType(MediaType.TEXT_HTML_VALUE + ";charset=ISO-8859-1"));
        // restTemplate.getInterceptors().add(new CustomClientHttpRequestInterceptor());
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypeList);
        restTemplate.getMessageConverters().set(1, mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        List<HttpMessageConverter<?>> converters = builder.build().getMessageConverters();
//        for (HttpMessageConverter<?> converter : converters) {
//            if (converter instanceof MappingJackson2HttpMessageConverter) {
//                try {
//                    List<MediaType> mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
//                    mediaTypeList.add(MediaType.APPLICATION_JSON);
//                    mediaTypeList.add(MediaType.TEXT_XML);
//                    mediaTypeList.add(MediaType.TEXT_PLAIN);
//                    ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypeList);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return builder.build();
//    }

}

