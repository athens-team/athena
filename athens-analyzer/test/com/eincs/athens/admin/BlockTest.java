package com.eincs.athens.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.eincs.athens.HttpPostTest.StringResponseHandler;
import com.google.common.collect.Lists;

public class BlockTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"https://rothlee.net/athens/admin/releaseBlock");

		//ip_addr=127.0.0.1&path=/timeline&method=GET
		
		List<NameValuePair> parameters = Lists.newArrayList();;
		parameters.add(new BasicNameValuePair("ip_addr", "/211.51.26.82"));
		parameters.add(new BasicNameValuePair("path", "/timeline"));
		parameters.add(new BasicNameValuePair("method", "GET"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
				"UTF-8");
		httpPost.setEntity(entity);

		String response = httpClient.execute(httpPost,
				new StringResponseHandler());
		System.out.println(response);
	}
}
