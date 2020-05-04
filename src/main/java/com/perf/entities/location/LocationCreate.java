package com.perf.entities.location;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.InputEntries;
import com.perf.input.params.LocationCreateInput;
import com.perf.utils.Utils;
import java.lang.reflect.Type;

/*
 * Creates a file LocationIDs.txt with all the location ids generated 
 */

class Multithread extends Thread{
	List<String> locationIds = Collections.synchronizedList(new ArrayList<String>());
	List<String> locationDetails = Collections.synchronizedList(new ArrayList<String>());
	List<String> finalLocationIds = Collections.synchronizedList(new ArrayList<String>());
	LocationCreateInput locationCreateInput = new LocationCreateInput();
	InputEntries input = new InputEntries();
	static ArrayList<List<String>> locationInfo;
		
	Gson gson = new Gson();
    Type gsonType = new TypeToken<HashMap>(){}.getType();
	
	static int count = 0;
	
	public void run() {
		for(int i=0 ; i< locationCreateInput.iterations ;i++) {
			count+=1;
			try {
				Random rand = new Random();
				int randIndex = rand.nextInt(locationInfo.size());
				List<String> opStream = locationInfo.get(randIndex);
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpPostConnection(locationCreateInput.url, locationCreateInput.getPostData(opStream));
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						System.out.println("Auth token expired. Generating again");
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPostConnection(locationCreateInput.url, locationCreateInput.getPostData(opStream));
					}
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				StringBuilder sb = new StringBuilder();
				while((output = br.readLine())!=null) {
					System.out.println(output);
					sb.append(output);
				}
				
				locationIds.add(Utils.getValueFromJson(sb, "locationId"));
				finalLocationIds.add(Utils.getValueFromJson(sb, "locationId"));
				
				Map<String, Object> locDetails = new HashMap<String, Object>();
				locDetails.put("locationId", Utils.getValueFromJson(sb, "locationId"));
				locDetails.put("details", Utils.getValueFromJson(sb, "details"));
				String gsonString = gson.toJson(locDetails, gsonType);
				locationDetails.add(gsonString);
				
			}
			catch (Exception e){				
				System.out.println("Exception.");
				e.printStackTrace();
			}
			if(locationIds.size() == locationCreateInput.batchSize  ||
					count == locationCreateInput.users * locationCreateInput.iterations) {
				LocationCreate.locationIdWrite(locationIds);
				locationIds = Collections.synchronizedList(new ArrayList<String>());
			}
		}
	}
}

public class LocationCreate {
	static LocationCreateInput locationCreateInput = new LocationCreateInput();
	static InputEntries inputEntries = new InputEntries();
	static boolean newFile = true;
	
	public static void locationIdWrite(List<String> locationDetails) {
		System.out.println("Location Id List:");
		for(String s : locationDetails) {
			System.out.print(s + " ");
		}
		System.out.println();
		
		if(newFile) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(locationCreateInput.newFileName));
				for(String s : locationDetails) {
					writer.write(s);
					writer.write("\n");
				}
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			newFile = false;
		}
		else {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(locationCreateInput.newFileName,true));
				for(String s : locationDetails) {
					writer.append(s);
					writer.append("\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void connect() throws IOException {
		AuthToken.setAuthToken(inputEntries.authUrl);
		Multithread.locationInfo = locationCreateInput.getInfo();
		for(int i = 0; i<locationCreateInput.users; i++) {
			Multithread multithread = new Multithread();
			multithread.start();
		}
	}
	
	public static void main(String[] args) {
		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
