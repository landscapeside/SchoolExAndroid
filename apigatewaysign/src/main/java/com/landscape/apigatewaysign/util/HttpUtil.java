/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.landscape.apigatewaysign.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.landscape.apigatewaysign.constant.Constants;
import com.landscape.apigatewaysign.constant.ContentType;
import com.landscape.apigatewaysign.constant.HttpHeader;
import com.landscape.apigatewaysign.constant.HttpMethod;
import com.landscape.apigatewaysign.constant.SystemHeader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.params.CoreConnectionPNames;

/**
 * Http工具类
 */
public class HttpUtil {
    /**
     * HTTP GET
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static void httpGet(URL url, Map<String, String> headers, String appKey, String appSecret, List<String> signHeaderPrefixList) throws Exception {
        headers = HeaderBuilder.initialBasicHeader(headers, appKey, appSecret, HttpMethod.GET, url, null, signHeaderPrefixList);
    }

    /**
     * HTTP POST表单
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param formParam            表单参数
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static void httpPost(String url, Map<String, String> headers, Map<String, String> formParam,String appKey, String appSecret, List<String> signHeaderPrefixList)
            throws Exception {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_FORM);

        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.POST, url, formParam, signHeaderPrefixList);

        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPost post = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        UrlEncodedFormEntity formEntity = buildFormEntity(formParam);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }

        return httpClient.execute(post);
    }

    /**
     * Http POST 字符串
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param body                 字符串请求体
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static void httpPost(URL url, Map<String, String> headers, String body, String appKey, String appSecret, List<String> signHeaderPrefixList) throws Exception {
        headers = HeaderBuilder.initialBasicHeader(headers, appKey, appSecret, HttpMethod.POST, url, null, signHeaderPrefixList);
    }

    /**
     * HTTP POST 字节数组
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param bytes                字节数组请求体
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param timeout              超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPost(String url, Map<String, String> headers, byte[] bytes, String appKey, String appSecret, int timeout, List<String> signHeaderPrefixList) throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.POST, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPost post = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bytes != null) {
            post.setEntity(new ByteArrayEntity(bytes));

        }

        return httpClient.execute(post);
    }

    /**
     * HTTP PUT 字符串
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param body                 字符串请求体
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param timeout              超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPut(String url, Map<String, String> headers, String body, String appKey, String appSecret, int timeout, List<String> signHeaderPrefixList) throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.PUT, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPut put = new HttpPut(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return httpClient.execute(put);
    }

    /**
     * HTTP PUT字节数组
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param bytes                字节数组请求体
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param timeout              超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPut(String url, Map<String, String> headers, byte[] bytes, String appKey, String appSecret, int timeout, List<String> signHeaderPrefixList) throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.PUT, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPut put = new HttpPut(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bytes != null) {
            put.setEntity(new ByteArrayEntity(bytes));

        }

        return httpClient.execute(put);
    }

    /**
     * HTTP DELETE
     *
     * @param url                  http://host+path+query
     * @param headers              Http头
     * @param appKey               APP KEY
     * @param appSecret            APP密钥
     * @param timeout              超时时间（毫秒）
     * @param signHeaderPrefixList 自定义参与签名Header前缀
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpDelete(String url, Map<String, String> headers, String appKey, String appSecret, int timeout, List<String> signHeaderPrefixList) throws Exception {
        headers = initialBasicHeader(headers, appKey, appSecret, HttpMethod.DELETE, url, null, signHeaderPrefixList);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpDelete delete = new HttpDelete(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            delete.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return httpClient.execute(delete);
    }

    /**
     * 构建FormEntity
     * @param formParam
     * @return
     * @throws UnsupportedEncodingException
     */
    private static UrlEncodedFormEntity buildFormEntity(Map<String, String> formParam) throws UnsupportedEncodingException {
        if (formParam != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : formParam.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, formParam.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList,Constants.ENCODING);
            formEntity.setContentType(ContentType.CONTENT_TYPE_FORM);
            return formEntity;
        }

        return null;
    }

    /**
     * 读取超时时间
     * @param timeout
     * @return
     */
    private static int getTimeout(int timeout) {
        if (timeout == 0) {
            return Constants.DEFAULT_TIMEOUT;
        }

        return timeout;
    }
}
