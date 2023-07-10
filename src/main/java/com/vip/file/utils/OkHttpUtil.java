package com.vip.file.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OkHttpUtil {
	
	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
	
	@Autowired
	private OkHttpClient okHttpClient;
	
	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
				.connectTimeout(300, TimeUnit.SECONDS)// 设置连接超时
				.readTimeout(300, TimeUnit.SECONDS)// 设置读超时
				.writeTimeout(300, TimeUnit.SECONDS)// 设置写超时
				.retryOnConnectionFailure(true).connectionPool(new ConnectionPool())// 是否自动重连
//				.addNetworkInterceptor(new TokenHeaderInterceptor())
				.build();
	}
	
	public String postSend(String url, String jsonParams) {
		Headers headers = setHeaders(null);
		RequestBody body = RequestBody.create(JSON, jsonParams);
		return send(RequestMethod.POST, url, headers, body);
	}
	
	public String postSend(String url, Map<String, String> header, Map<String, Object> params) {
		Headers headers = setHeaders(header);
		RequestBody body = RequestBody.create(JSON, JSONObject.toJSONString(params));
		return send(RequestMethod.POST, url, headers, body);
	}
	
	public String getSend(String url, Map<String, String> header, Map<String, Object> params) {
		Headers headers = setHeaders(header);
		String getUrl = setParamsByGetUrl(url,params);
		RequestBody body = RequestBody.create(JSON, JSONObject.toJSONString(params));
		return send(RequestMethod.GET, getUrl, headers, body);
	}
	
	public String send(RequestMethod requestMethod, String url, Headers headers, RequestBody body) {
		log.info("okhttp send start -> requestMethod {} url {} headers {} ", requestMethod, url, headers);
		Request request = null;
		if(requestMethod == RequestMethod.POST)
			request = new Request.Builder().url(url)
				.headers(headers)
				.post(body)
				.build();
		else 
			request = new Request.Builder().url(url)
				.headers(headers)
				.build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
			     String result = response.body().string();
			     log.info("okhttp send end -> result {} ", result);
			     return result;
			}
		} catch (IOException e) {
			log.error(e.toString());
		}

		return null;
	}
	
	public Headers setHeaders(Map<String, String> header) {
		Headers.Builder headers = new Headers.Builder();
		if(header == null || header.isEmpty())
			return headers.build();
		
		for (Map.Entry<String, String> entry : header.entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}
		
		return headers.build();
	}
	
	public String setParamsByGetUrl(String url, Map<String, Object> params) {
        Assert.notNull(url, "'url' must not be null.");
        String reqUrl = url + "?";
        for (Map.Entry<String, Object> map : params.entrySet()) {
            reqUrl += map.getKey() + "=" + map.getValue() + "&";
        }
        reqUrl = reqUrl.substring(0, reqUrl.lastIndexOf("&"));
        return reqUrl;
    }
	
	public RequestBody setParamsJson(Map<String, String> params) {
		RequestBody body = RequestBody.create(null, new byte[0]);
		if(params == null || params.isEmpty())
			return body;
		
		return RequestBody.create(JSON, JSONObject.toJSONString(params));
	}
	
	public RequestBody setParamsForm(Map<String, String> params) {
		RequestBody body = RequestBody.create(null, new byte[0]);
		if(params == null || params.isEmpty())
			return body;
		
		FormBody.Builder formBodyBuilder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			formBodyBuilder.add(entry.getKey(), entry.getValue());
		}
		
	    return formBodyBuilder.build();
	}
}
