package com.perf.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.perf.authentication.AuthToken;

public class HttpConnection {
public HttpURLConnection httpPostConnection(String currURL, String payload) {
		
		HttpURLConnection conn = null;
		try {
			URL url = new URL(currURL);
			conn = (HttpURLConnection) url.openConnection();
			String token = AuthToken.authTokenVal;
			token = token.replaceAll("\"", "");
			String auth = "Bearer " + token;
			
			conn.setRequestProperty("Authorization", auth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			String inputData = payload;
			
			OutputStream os = conn.getOutputStream();
			os.write(inputData.getBytes());
			os.flush();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public HttpURLConnection httpGetConnection(String currURL) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(currURL);
			conn = (HttpURLConnection) url.openConnection();
			String token = AuthToken.authTokenVal;
			token = token.replaceAll("\"", "");
			String auth = "Bearer " + token;
			
			conn.setRequestProperty("Authorization", auth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("GET");	
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public HttpURLConnection httpPutConnection(String currURL, String payload) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(currURL);
			conn = (HttpURLConnection) url.openConnection();
			String token = AuthToken.authTokenVal;
			token = token.replaceAll("\"", "");
			String auth = "Bearer " + token;
			
			conn.setRequestProperty("Authorization", auth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);
			
			String inputData = payload;
			
			OutputStream os = conn.getOutputStream();
			os.write(inputData.getBytes());
			os.flush();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
