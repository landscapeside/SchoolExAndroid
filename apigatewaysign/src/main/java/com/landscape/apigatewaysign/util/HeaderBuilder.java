package com.landscape.apigatewaysign.util;

import com.landscape.apigatewaysign.constant.Constants;
import com.landscape.apigatewaysign.constant.HttpHeader;
import com.landscape.apigatewaysign.constant.SystemHeader;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 1 on 2016/6/24.
 */
public class HeaderBuilder {

    /**
     * 初始化基础Header
     *
     * @param headers              Http头
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param method               Http方法
     * @param url       http://host+path+query
     * @param formParam            表单参数
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 基础Header
     * @throws MalformedURLException
     */
    public static Map<String, String> initialBasicHeader(Map<String, String> headers, String appKey, String appSecret, String method, URL url, Map formParam, List<String> signHeaderPrefixList) throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<>();
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(url.getPath())) {
            stringBuilder.append(url.getPath());
        }
        if (StringUtils.isNotBlank(url.getQuery())) {
            stringBuilder.append("?");
            stringBuilder.append(url.getQuery());
        }

        headers.put(HttpHeader.HTTP_HEADER_USER_AGENT, Constants.USER_AGENT);
        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(new Date().getTime()));
        headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        headers.put(SystemHeader.X_CA_KEY, appKey);
        headers.put(SystemHeader.X_CA_SIGNATURE, SignUtil.sign(method, stringBuilder.toString(), headers, formParam, appSecret, signHeaderPrefixList));

        return headers;
    }
}
