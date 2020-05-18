package com.perf.entities.location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.perf.input.params.LocationCreateInput;

public class LocationDetailsUtils {
	LocationCreateInput locationCreateInput = new LocationCreateInput();
	public String coordsFileName;
	public Gson g;

	public LocationDetailsUtils() {
		coordsFileName = "LocationCoords.txt";
		g = new Gson();
	}

	//Taking first numRows records
	public List<String> getLocationRows(int numRows) throws IOException{
		String row;
		List<String> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(locationCreateInput.locationFileName));
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
		BufferedReader reader = new BufferedReader(new FileReader(locationCreateInput.locationFileName));
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
			System.out.println("STRING: " + s);
			locationMap = g.fromJson(s, locationMap.getClass());
			locationIdList.add(locationMap.get("locationId").toString());
		}
		return locationIdList;
	}
	
	public List<List<Double>> getCoordinates(int num){
		List<List<Double>> coordinates = getCoordinates();
		List<List<Double>> coordinatesNum = new ArrayList<List<Double>>();
		while(num > 0) {
			coordinatesNum.add(coordinates.get(num));
			num--;
		}
		return coordinatesNum;
	}
	
	public List<List<Double>> getCoordinates() {
		List<List<Double>> coordinates = new ArrayList<List<Double>>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(coordsFileName));
			String line = reader.readLine();
			while(line != null) {
				String[] str = line.split(",");
				ArrayList<Double> tempArr = new ArrayList<Double>();
				tempArr.add(Double.parseDouble(str[3]));
				tempArr.add(Double.parseDouble(str[2]));
				coordinates.add(tempArr);
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e) {
			System.out.println("Problem reading file -> LocationCoords.txt");
			e.printStackTrace();
		}
		
		return coordinates;
	}

}
