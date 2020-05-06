package com.perf.entities.location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

public class LocationDetailsUtils {

	public String fileName = "LocationIDs.txt";
	public Gson g = new Gson();

	//Taking first numRows records
	public List<String> getLocationRows(int numRows) throws IOException{
		String row;
		List<String> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		while ((row = reader.readLine()) != null && numRows > 0) {
			list.add(row);
			numRows--; //Assuming numRows <= Number of rows in the File
		}
		reader.close();
		return list;
	}

	//Returns all the records from the File
	public List<String> getLocationRows() throws IOException{
		String row;
		List<String> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		while ((row = reader.readLine()) != null) {
			list.add(row);
		}
		reader.close();
		return list;
	}
	
	public List<String> getLocationIds() throws IOException{
		List<String> locationRecords = getLocationRows();
		return getLocationIds(locationRecords);
	}
	

	//Returns the list of LocationIds
	public List<String> getLocationIds(List<String> locationRecords){
		List<String> locationIdList = new ArrayList<>();
		for(String s : locationRecords) {
			Map<String, Object> locationMap = new HashMap<String, Object>();
			locationMap = g.fromJson(s, locationMap.getClass());
			locationIdList.add(locationMap.get("locationId").toString());
		}
		return locationIdList;
	}
	
	public List<List<Double>> getLocationCoords(int numRows) throws IOException{
		List<String> locationRecords = getLocationRows(numRows);
		return getLocationCoords(locationRecords);
	}

	//Returns the list of Coordinates
	public List<List<Double>> getLocationCoords(List<String> locationRecords){
		List<List<Double>> coordinatesList = new ArrayList<>();
		for(String s : locationRecords) {
			try {
				Map<String, Object> locationMap = new HashMap<String, Object>();
				locationMap = g.fromJson(s, locationMap.getClass());
				Map<String, Object> details = (Map<String, Object>) locationMap.get("details");
				Map<String, Object> basicDetails = (Map<String, Object>) details.get("Basic");
				List<HashMap<String, Object>> addresses = (List<HashMap<String, Object>>) basicDetails.get("addresses");
				for(HashMap<String, Object> addr : addresses) {
					if ((boolean) addr.get("primary")) {
						Map<String, Object> gps = (Map<String, Object>) details.get("gps");
						List<Double> coordinates = (List<Double>) gps.get("coordinates");
						if(coordinates != null && coordinates.size() > 0) {
							coordinatesList.add(coordinates);
						}
					}
				}
			}
			catch(Exception e) {
				System.out.println("Exception while parsing location coordinates");
				e.printStackTrace();
			}

		}
		return coordinatesList;
	}

}
