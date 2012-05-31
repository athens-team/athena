package com.eincs.athens;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

/**
 * @author Jung-Haeng Lee
 */
public class HttpPostTest {

	public static void main(String[] args) throws ClientProtocolException,
			IOException, InterruptedException {

		Random rand = new Random();
		while (true) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"https://docs.google.com/spreadsheet/formResponse?formkey=dG1jSkI0SmJTT1FHNnBPMHdUWkZEQVE6MQ&embedded=true&embedded=true&ifq");

			String answer1 = String.valueOf(rand.nextInt(9) + 1);
			String answer2 = String.valueOf(rand.nextInt(9) + 1);

			List<NameValuePair> parameters = Lists.newArrayList();
			parameters.add(new BasicNameValuePair("entry.0.group", answer1));
			parameters.add(new BasicNameValuePair("entry.6.group", answer2));
			parameters.add(new BasicNameValuePair("pageNumber", ""));
			parameters.add(new BasicNameValuePair("backupCache", ""));
			parameters.add(new BasicNameValuePair("submit", "Submit"));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					"UTF-8");
			httpPost.setEntity(entity);

			String response = httpClient.execute(httpPost,
					new StringResponseHandler());
			System.out.println(String.format("%s %s", answer1, answer2));
			System.out.println(response);
			
			Thread.sleep(1000);
		}

	}

	public static class StringResponseHandler implements
			ResponseHandler<String> {

		@Override
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			return CharStreams.toString(new InputStreamReader(response
					.getEntity().getContent()));
		}

	}

}
