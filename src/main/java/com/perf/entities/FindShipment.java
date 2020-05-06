package com.perf.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.FindShipmentInput;
import com.perf.input.params.InputEntries;

class Multithread extends Thread{
	FindShipmentInput findShipmentInput = new FindShipmentInput();
	InputEntries input = new InputEntries();
	public void run() {
		
		for(int i=0; i< findShipmentInput.iterations ;i++) {
			
			AbstractMap.SimpleEntry<String, LocalDate> pair = findShipmentInput.fetchParams(FindShipment.coordinates);
			String jsonParams = pair.getKey();
			LocalDate date = pair.getValue();
			String jsonFilters = findShipmentInput.fetchFilters(date);
			
			try {
				
				URL url = new URL(findShipmentInput.url);
				URI uri = FindShipmentInput.buildContextURI(url.toString(),"context=");
				uri = FindShipmentInput.buildContextURI(uri.toString(),jsonParams);
				uri = FindShipmentInput.buildContextURI(uri.toString(),"&filter=");
				uri = FindShipmentInput.buildContextURI(uri.toString(),jsonFilters);
				System.out.println("URI : " + FindShipmentInput.decode(uri.toString()));
				
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpGetConnection(uri.toString());
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						System.out.println("Auth token expired. Generating again");
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpGetConnection(uri.toString());
					}
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

public class FindShipment {
	FindShipmentInput findShipmentInput = new FindShipmentInput();
	InputEntries input = new InputEntries();
	public static ArrayList<ArrayList<Double>> coordinates;
	
	public ArrayList<ArrayList<Double>> fetchCoordinates() {
		coordinates = new ArrayList<ArrayList<Double>>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(findShipmentInput.filePath));
			String line = reader.readLine();
			while(line != null) {
				String[] str = line.split(",");
				ArrayList<Double> coordinatePair = new ArrayList<Double>();
				coordinatePair.add(Double.parseDouble(str[3]));
				coordinatePair.add(Double.parseDouble(str[2]));
				coordinates.add(coordinatePair);
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e) {
			System.out.println("Problem reading file.");
			e.printStackTrace();
		}
		return coordinates;
	}
	
	public void connect() {
		AuthToken.setAuthToken(input.authUrl);
		coordinates = fetchCoordinates();
		for(int i = 0; i < findShipmentInput.users; i++) {
			Multithread obj = new Multithread();
			obj.start();
		}
	}
	public static void main(String[] args) {
		FindShipment findShipment = new FindShipment();
		findShipment.connect();
	}
}
