package com.xm.comment_utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HttpUtils2 {

	private static final PoolingHttpClientConnectionManager cm;
	private static final CloseableHttpClient HTTP_CLIENT;

	static{
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(100);
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(5000)
				.setSocketTimeout(10000)
				.build();
		HTTP_CLIENT = HttpClientBuilder.create()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(config)
				.build();
	}

	public static HttpUtils.Result doGet(String url){
		return doGet(url,null,null,"utf-8",10000);
	}
	public static HttpUtils.Result doGet(String url, int timeout){
		return doGet(url,null,null,"utf-8",timeout);
	}
	public static HttpUtils.Result doGet(String url, String charSet){
		return doGet(url,null,null,charSet,10000);
	}
	public static HttpUtils.Result doGet(String url, String charSet, int timeout){
		return doGet(url,null,null,charSet,timeout);
	}
	public static HttpUtils.Result doGet(String url, Map<String,Object> header, String charSet){
		return doGet(url,null,header,charSet,10000);
	}

	public static HttpUtils.Result doGet(String url, Map<String,String> param, Map<String,Object> header, String charSet, Integer timeout){
		charSet = charSet == null?"UTF-8":charSet;
		timeout = timeout== null?10000:timeout;

		//构建文本参数
		if(param != null) {
			List<NameValuePair> nameValuePairs = param.entrySet().stream().map((item) -> {
				return new BasicNameValuePair(item.getKey(), item.getValue().toString());
			}).collect(Collectors.toList());
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,charSet);
				url = url + "?" + EntityUtils.toString(entity,charSet);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(RequestConfig.custom().setConnectTimeout(timeout).build());
		//配置header
		if(header != null)
			header.entrySet().stream().forEach(item->{
				httpGet.addHeader(item.getKey(),item.getValue().toString());
			});
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createResult(httpGet,response,charSet);
	}
	public static HttpUtils.Result doPost(String url, Map<String,Object> param, Map<String,File> files, Map<String,String> header, Boolean isJson, String charSet){

		charSet = charSet == null?"UTF-8":charSet;
		isJson = isJson == null?false:true;

		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entity = null;

		if(files != null){
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.setCharset(Charset.forName(charSet));
			if(isJson) {
				multipartEntityBuilder.setContentType(ContentType.APPLICATION_JSON);
			}else {
				multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
			}
			//构建文件参数
			files.entrySet().stream().forEach((item)->{
				multipartEntityBuilder.addBinaryBody(item.getKey(),item.getValue(),ContentType.MULTIPART_FORM_DATA,item.getValue().getName());
			});
			//构建文件参数
			param.entrySet().stream().forEach(item->{
				multipartEntityBuilder.addTextBody(item.getKey(),item.getValue().toString());
			});
			httpPost.setEntity(multipartEntityBuilder.build());
		}else {
			try {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				entity = new UrlEncodedFormEntity(formParams,charSet);
				entity.setContentEncoding(charSet);
				if(isJson) {
					entity.setContentType(ContentType.APPLICATION_JSON.toString());
				}else {
					entity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.toString());
				}
				//构建文本参数
				List<NameValuePair> nameValuePairs = param.entrySet().stream().map((item)->{
					return new BasicNameValuePair(item.getKey(),item.getValue().toString());
				}).collect(Collectors.toList());
				formParams.addAll(nameValuePairs);
				httpPost.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//构建header
		if(header != null)
			header.entrySet().stream().forEach(item->{
				httpPost.addHeader(item.getKey(),item.getValue());
			});
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createResult(httpPost,response,charSet);

	}

	private static HttpUtils.Result createResult(HttpRequest request, HttpResponse response, String charSet){
		String responseContent = null;
		if(response.getStatusLine().getStatusCode() == 200){
			HttpEntity resEntity = response.getEntity();
			try {
				responseContent = EntityUtils.toString(resEntity,charSet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			throw new RuntimeException("请求失败,状态码:"+response.getStatusLine().getStatusCode());
		}
		//构建返回值
		HttpUtils.Result result = new HttpUtils.Result();
		Map<String,String> reqHeaders = new HashMap<>();
		for (Header reqHeader : request.getAllHeaders()) {
			reqHeaders.put(reqHeader.getName(),reqHeader.getValue());
		}
		result.setReqHead(reqHeaders);
		result.setReqHeadStr(JSON.toJSONString(reqHeaders));
		Map<String,String> resHeaders = new HashMap<>();
		for (Header resHeader : response.getAllHeaders()) {
			resHeaders.put(resHeader.getName(),resHeader.getValue());
		}
		result.setResHead(resHeaders);
		result.setResHeadStr(JSON.toJSONString(resHeaders));
		result.setResult(responseContent);
		return result;
	}

   public static void main(String[] args) throws IOException {
		String url = "http://47.92.6.56:5010/get/";
		System.out.println(doGet(url).getResult());
   }
}
