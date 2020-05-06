package com.perf.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.perf.utils.Utils;

public class AuthToken {
	public static String authTokenVal;
	public static String getAuthToken(String authUrl) {
		String authToken = null;
		try {
			URL url = new URL(authUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.flush();
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("AuthToken Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			authToken = Utils.getValueFromJson(sb, "access_token");
			conn.disconnect();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {		
			e.printStackTrace();		
		}
		return authToken;
	}
	
	public static void setAuthToken(String authUrl) {
		authTokenVal = getAuthToken(authUrl);
	}
}
