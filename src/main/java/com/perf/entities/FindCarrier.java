package com.perf.entities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.FindCarrierInput;
import com.perf.input.params.InputEntries;

class MultithreadCarrier extends Thread{
	FindCarrierInput findCarrierInput = new FindCarrierInput();
	InputEntries input = new InputEntries();
	public void run() {
		for(int i=0 ;i<findCarrierInput.iterations; i++) {
			try {
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpGetConnection(FindCarrierInput.url);
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						System.out.println("Auth token expired. Generating again");
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpGetConnection(FindCarrierInput.url);
					}
				}
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				
				StringBuilder sb = new StringBuilder();
				while((output = br.readLine())!=null) {
					System.out.println(output);
					sb.append(output);
				}
			}
			catch (Exception e){				
				System.out.println("Exception.");
				e.printStackTrace();
			}
		}
	}
}

public class FindCarrier {
	FindCarrierInput findCarrierInput = new FindCarrierInput();
	InputEntries input = new InputEntries();
	
	public void connect() throws UnsupportedEncodingException {
		AuthToken.setAuthToken(input.authUrl);
		findCarrierInput.setUrl();
		for(int i=0; i<findCarrierInput.threads; i++) {
			MultithreadCarrier thread = new MultithreadCarrier();
			thread.start();
		}
	}

	public static void main(String[] args) {
		FindCarrier findCarrier = new FindCarrier();
		try {
			findCarrier.connect();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
