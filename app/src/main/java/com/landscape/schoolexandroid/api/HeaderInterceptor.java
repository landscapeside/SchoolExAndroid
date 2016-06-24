package com.landscape.schoolexandroid.api;

import com.landscape.apigatewaysign.constant.ContentType;
import com.landscape.apigatewaysign.constant.HttpHeader;
import com.landscape.apigatewaysign.constant.SystemHeader;
import com.landscape.apigatewaysign.util.HttpUtil;
import com.landscape.apigatewaysign.util.MessageDigestUtil;
import com.landscape.schoolexandroid.common.AppConfig;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by 1 on 2016/6/24.
 */
public class HeaderInterceptor implements Interceptor {
    //自定义参与签名Header前缀（可选,默认只有"X-Ca-"开头的参与到Header签名）
    private final static List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<String>();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    static {
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add(SystemHeader.X_CA_KEY);
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add(SystemHeader.X_CA_TIMESTAMP);
        CUSTOM_HEADERS_TO_SIGN_PREFIX.add(SystemHeader.X_CA_NONCE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = buildReq(chain.request());
        return chain.proceed(request);
    }

    private Request buildReq(Request request) {
        if (request.method().equals("GET")) {
            Map<String, String> headers = new HashMap<String, String>();
            //（可选）响应内容序列化格式,默认application/json,目前仅支持application/json
            headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
            try {
                HttpUtil.httpGet(request.url().url(),headers, AppConfig.AppKey,AppConfig.AppSecret,CUSTOM_HEADERS_TO_SIGN_PREFIX);
                Request.Builder builder = request.newBuilder();
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    if (e.getKey().equals(HttpHeader.HTTP_HEADER_USER_AGENT)) {
                        builder.header(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
                    } else {
                        builder.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
                    }
                }
                request = builder.build();
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        } else if (request.method().equals("POST")) {
            /*取BODY部分*/
            String bodyStr = "";
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = request.body().contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                bodyStr = buffer.readString(charset);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, String> headers = new HashMap<String, String>();
            //（可选）响应内容序列化格式,默认application/json,目前仅支持application/json
            headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
            //（可选）Body MD5,服务端会校验Body内容是否被篡改,建议Body非Form表单时添加此Header
            headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(bodyStr/*request.body().toString()*/));
            //（POST/PUT请求必选）请求Body内容格式
            headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_TEXT);
            try {
                HttpUtil.httpPost(request.url().url(),headers,bodyStr, AppConfig.AppKey,AppConfig.AppSecret,CUSTOM_HEADERS_TO_SIGN_PREFIX);
                Request.Builder builder = request.newBuilder();
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    if (e.getKey().equals(HttpHeader.HTTP_HEADER_USER_AGENT)) {
                        builder.header(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
                    } else {
                        builder.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
                    }
                }
                request = builder.build();
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        } else if (request.method().equals("PUT")) {
            // TODO: 2016/6/24 暂不处理
        } else if (request.method().equals("DELETE")) {
            // TODO: 2016/6/24 暂不处理
        }
        return request;
    }
}
