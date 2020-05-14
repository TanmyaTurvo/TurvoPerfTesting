package com.perf.input.params;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.perf.utils.Utils;

public class LocationCreateInput {
	InputEntries input = new InputEntries();
	private String domain = input.domain;
	public String url = domain + "/api/locations/?fullResponse=true";
	public String filePath = "/Users/isaac.t/Downloads/simplemaps_uscities_basicv1.6/uscities_copy.csv";
	public String newFileName = "LocationIDs.txt";
	
	public int locationCount = 10;
	public int batchSize = 5;
	
	public ArrayList<List<String>> getInfo() throws IOException {
		String row;
		ArrayList<List<String>> list = new ArrayList<List<String>>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		while ((row = reader.readLine()) != null) {
		    String[] data = row.split(",");
		    List<String> subList = new ArrayList<String>();
		    subList = Arrays.asList(data);
		    list.add(subList);
		}
		reader.close();
		return list;
	}
	
	public List<String> getLocationIds() throws IOException{
		String row;
		List<String> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(newFileName));
		while ((row = reader.readLine()) != null) {
		    list.add(row);
		}
		reader.close();
		return list;
	}
	
	public String getPostData(List<String> opStream) {
		
		Map<String,Object> criteria = new HashMap<String,Object>();
		criteria.put("timezone",opStream.get(15).replaceAll("\"", ""));
		criteria.put("name", "testRadialSearch" + Utils.getRandomString(6));
		criteria.put("type",null);
		criteria.put("groups", new ArrayList<String>());
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("primary", true);
		
		ArrayList<Double> coordinates = new ArrayList<Double>();
		coordinates.add(Double.parseDouble(opStream.get(9).replaceAll("\"", "")));
		coordinates.add(Double.parseDouble(opStream.get(8).replaceAll("\"", "")));
		Map<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("coordinates", coordinates);
		map.put("gps", tempMap);
		
		map.put("line1", "");
		map.put("line2", "");
		
		tempMap = new HashMap<String, Object>();
		tempMap.put("name", opStream.get(0).replaceAll("\"", ""));
		map.put("city",tempMap);
		
		tempMap = new HashMap<String, Object>();
		tempMap.put("name", opStream.get(2).replaceAll("\"", ""));
		map.put("state",tempMap);
		
		map.put("zip", null);
		
		tempMap = new HashMap<String, Object>();
		tempMap.put("name", "United States");
		map.put("country", tempMap);
		
		tempMap = new HashMap<String, Object>();
		tempMap.put("id", Integer.parseInt(opStream.get(18).replaceAll("\"", "")));
		tempMap.put("key","1153");
		tempMap.put("value","Main");
		map.put("type",tempMap);
		
		ArrayList<Map<String,Object>> addList = new ArrayList<Map<String,Object>>();
		addList.add(map);
		criteria.put("addresses", addList);
		
		String json = Utils.mapToJson(criteria);
		return json;
	}
}
