package com.xm.comment_utils.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class HttpUtils {
	
	private static final String headBoundary = "----WebKitFormBoundary3obsbWKYIIb42y5W";
	private static final String boundary = "--"+headBoundary;
	
	public static Result doGet(String url){
		return doGet(url,null,"utf-8",10000,null);
	}
	public static Result doGet(String url,int timeout){
		return doGet(url,null,"utf-8",timeout,null);
	}
	public static Result doGet(String url,String charSet){
		return doGet(url,null,charSet,10000,null);
	}
	public static Result doGet(String url,String charSet,int timeout){
		return doGet(url,null,charSet,timeout,null);
	}
	public static Result doGet(String url,Map<String,Object> header,String charSet){
		return doGet(url,header,charSet,10000,null);
	}

	public static Result doGet(String url, Map<String,Object> header, Proxy proxy){
		return doGet(url, header,"utf-8",10000,proxy);
	}
	public static Result doGet(String url, Proxy proxy){
		return doGet(url, null,proxy);
	}
	public static Result doGet(String url, Map<String,Object> header, String charSet, int timeout, Proxy proxy){
		Result result = new Result();
		BufferedReader read = null;
		try {
			URL URL = new URL(url);
			HttpURLConnection conn = null;
			if(proxy != null){
				conn = (HttpURLConnection)URL.openConnection(proxy);
			}else {
				conn = (HttpURLConnection)URL.openConnection();
			}

			if(header != null){
				for(Entry<String, Object> head:header.entrySet()){
					conn.setRequestProperty(head.getKey(), (String)head.getValue());
				}
			}
			result = printRequestHeader(conn,result);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(timeout);
			read = new BufferedReader(new InputStreamReader(conn.getInputStream(),charSet));
			StringBuffer str = new StringBuffer();
			String buf = "";
			buf = read.readLine();
			while (buf != null ) {
				str.append(buf);
				buf = read.readLine();
			}
			result.setResult(str.toString());
			result = getHttpResponseHeader(conn,result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
	            if(read!=null) read.close();
	        } catch(IOException ex){
	            ex.printStackTrace();
	        }
		}
		return null;
	}
	public static Result doPost(String url,Map<String,Object> param,Map<String,File> files,Map<String,String> header,Boolean isJson,String charSet,Proxy proxy){
		Result result = new Result();
		boolean hasFile = files == null?false:true;
		boolean json = isJson == null?false:true;
		if(charSet == null){
			charSet = "UTF-8";
		}
		DataOutputStream dos = null;
		BufferedReader read = null;
		try {
			URL URL = new URL(url);
			HttpURLConnection conn = null;
			if(proxy != null){
				conn = (HttpURLConnection)URL.openConnection(proxy);
			}else {
				conn = (HttpURLConnection)URL.openConnection();
			}
			if(hasFile)
		    	conn.setRequestProperty("Content-Type"," multipart/form-data; boundary="+headBoundary);
			if(isJson)
				conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			if(header != null){
				for(Entry<String, String> head:header.entrySet()){
					conn.setRequestProperty(head.getKey(), head.getValue());
				}
			}
			result = printRequestHeader(conn,result);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(20000);  
			conn.setReadTimeout(300000); 
			dos = new DataOutputStream(conn.getOutputStream());
            String enter = "\r\n";
            StringBuffer contentBody = new StringBuffer();
            String endBoundary = enter + boundary + "--" + enter;
			if(hasFile){
				//传输文本数据
				if(param != null && isJson == false){
					for(Entry<String,Object> entry:param.entrySet()){
						contentBody.append(enter)
	        			.append(boundary)
	        			.append(enter)
	        			.append("Content-Disposition: form-data; name=\""+entry.getKey()+"\"")
	        			.append(enter)
	        			.append(enter)
	        			.append(entry.getValue());
						dos.writeBytes(contentBody.toString());
					}
				}
				//上传文件
	            FileInputStream fis = null;
				for(Entry<String,File> entry:files.entrySet()){
					fis = new FileInputStream(entry.getValue()); 
					contentBody = new StringBuffer();
					contentBody.append(enter)
        			.append(boundary)
        			.append(enter)
        			.append("Content-Disposition: form-data; name=\""+entry.getKey()+"\"; filename=\""+entry.getValue()+"\"")
        			.append(enter)
        			.append("Content-Type: image/jpeg")
        			.append(enter)
        			.append(enter);
                    dos.writeBytes(contentBody.toString());
                    byte[] bufferOut =new byte[(int) entry.getValue().length()];
                    fis.read(bufferOut);
                    dos.write(bufferOut);
                    fis.close();
				}
				dos.writeBytes(endBoundary);
			}else{
				if(param != null){
					if(isJson == false){
						StringBuffer str = new StringBuffer();
						//传输文本数据
						for(Entry<String,Object> entry:param.entrySet()){
							str.append(entry.getKey()+"="+entry.getValue()+"&");
						}
						if(str.lastIndexOf("&") == str.length()-1){
							dos.writeBytes(str.substring(0,str.length()-1));
						}else{
							dos.writeBytes(str.toString());
						}
					}else{
						dos.writeBytes(new ObjectMapper().writeValueAsString(param));
					}
				}
			}
			dos.flush();
			result = getHttpResponseHeader(conn,result);
			InputStream in = conn.getInputStream();
			read = new BufferedReader(new InputStreamReader(in,charSet));
		    String line,resp = "";
		    while ((line = read.readLine()) != null) {
		        resp += line;
		    }
		    result.setResult(resp);
		} catch (IOException e) {
			e.printStackTrace();;
		} finally{
			try{
				if(dos!=null) dos.close();
	            if(read!=null) read.close();
	        } catch(IOException ex){
	            ex.printStackTrace();
	        }
		}
		return result;
	}
	public static boolean httpDownload(String httpUrl,String filePath){  
		File file = new File(filePath);
		return httpDownload(httpUrl,file);
	}
	public static boolean httpDownload(String httpUrl,File file){
		// 下载网络文件
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(file);
			byte[] bytes = httpDownLoad(httpUrl);
			fs.write(bytes);
			fs.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			if(fs != null){
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
   }

	public static boolean connectTest(String urlpath){
		URL url;
		InputStream in = null;
		try {
			url = new URL(urlpath);
			in = url.openStream();
			return true;
		} catch (Exception e1) {
			return false;
		} finally{
			try {
				if(in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

   public static byte[] httpDownLoad(String httpUrl) throws IOException {
	   int bytesum = 0;
	   int byteread = 0;
	   URL url = null;
	   URLConnection conn = null;
	   InputStream inStream = null;
	   ByteArrayOutputStream byteArrayOutputStream = null;
	   try {
		   url = new URL(httpUrl);
		   conn = url.openConnection();
		   inStream = conn.getInputStream();
		   byteArrayOutputStream = new ByteArrayOutputStream();
		   byte[] buffer = new byte[1204];
		   while ((byteread = inStream.read(buffer)) != -1) {
			   bytesum += byteread;
			   byteArrayOutputStream.write(buffer, 0, byteread);
		   }
	   } finally {
		   try {
			   if(byteArrayOutputStream != null)
				   byteArrayOutputStream.close();
			   if(inStream != null)
				   inStream.close();
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	   }
	   return byteArrayOutputStream.toByteArray();
   }
   
   public static void main(String[] args) throws IOException {
	   httpDownLoad("https://img.alicdn.com/imgextra/i1/2943085408/TB2NsUiCr9YBuNjy0FgXXcxcXXa_!!2943085408.jpg");

   }
	
	private static Result printRequestHeader(HttpURLConnection http,Result result) throws UnsupportedEncodingException {
		Map<String, List<String>> header = http.getRequestProperties();
		Map<String, String> headers = new HashMap<String, String>();
		StringBuffer buf = new StringBuffer();
		for (Entry<String, List<String>> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() + ":" : "";
			StringBuffer value = new StringBuffer();
			List<String> val = entry.getValue();
			for(String a:val){
				value.append(a);
			}
			buf.append(key+value+"\n");
			headers.put(key, value.toString());
		}
		result.setReqHead(headers);
		result.setReqHeadStr(buf.toString());
		return result;
	}
    private static Result getHttpResponseHeader(HttpURLConnection http,Result result) throws UnsupportedEncodingException {
        Map<String, String> header = new LinkedHashMap<String, String>();
        StringBuffer headerStr = new StringBuffer();
        for (int i = 0;; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null)
                break;
            headerStr.append(http.getHeaderFieldKey(i)+":"+mine+"\n");
            header.put(http.getHeaderFieldKey(i), mine);
        }
        result.setResHead(header);
        result.setResHeadStr(headerStr.toString());
        return result;
    }
    public static Result doPost(String url,Map<String,Object> param,Proxy proxy){
		return doPost(url,param,null,null,false,null,proxy);
	}
    public static Result doPost(String url,Map<String,String> header,Map<String,Object> param,Proxy proxy){
		return doPost(url,param,null,header,false,null,proxy);
	}
    public static Result doPost(String url,Map<String,Object> param){
		return doPost(url,param,null,null,false,null,null);
	}
	public static Result doPost(String url,Map<String,Object> param,Map<String,File> files){
		return doPost(url,param,files,null,null,null,null);
	}
	public static Result doPost(String url,Map<String,Object> param,Boolean isJson,Proxy proxy){
		return doPost(url,param,null,null,isJson,null,proxy);
	}
	public static Result doPost(String url,Map<String,String> header,Map<String,Object> param,Boolean isJson,Proxy proxy){
		return doPost(url,param,null,header,isJson,null,proxy);
	}
	public static Result doPost(String url,Map<String,Object> param,Boolean isJson){
		return doPost(url,param,null,null,isJson,null,null);
	}
	public static class Result{
		private String result = "";
		private String resHeadStr = "";
		private String reqHeadStr = "";
		private Map<String,String> resHead;
		private Map<String,String> reqHead;
		
		public String getResCookie(String name){
			String cookie = resHead.get("Set-Cookie");
			if(cookie != null && !cookie.equals("")){
				String[] cookies = cookie.split(";");
				for (int i = 0; i < cookies.length; i++) {
					String[] cook = cookies[i].split("=");
					if(cook.length == 2 && name.equals(cook[0])){
						return cook[1];
					}
				}
			}
			return null;
		}
		public String getReqCookie(String name){
			String cookie = reqHead.get("Cookie");
			if(cookie != null && !cookie.equals("")){
				String[] cookies = cookie.split(";");
				for (int i = 0; i < cookies.length; i++) {
					String[] cook = cookies[i].split("=");
					if(cook.length == 2 && name.equals(cook[0])){
						return cook[1];
					}
				}
			}
			return null;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getResHeadStr() {
			return resHeadStr;
		}
		public void setResHeadStr(String resHeadStr) {
			this.resHeadStr = resHeadStr;
		}
		public String getReqHeadStr() {
			return reqHeadStr;
		}
		public void setReqHeadStr(String reqHeadStr) {
			this.reqHeadStr = reqHeadStr;
		}
		public Map<String, String> getResHead() {
			return resHead;
		}
		public void setResHead(Map<String, String> resHead) {
			this.resHead = resHead;
		}
		public Map<String, String> getReqHead() {
			return reqHead;
		}
		public void setReqHead(Map<String, String> reqHead) {
			this.reqHead = reqHead;
		}
	}
}
