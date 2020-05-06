package com.perf.input.params;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.perf.utils.Utils;

public class FindShipmentInput {
	
	InputEntries input = new InputEntries();
	public String url = input.domain + "/api/network/shipments";
	public String filePath = "/Users/isaac.t/Downloads/city_coordinates.txt";
	public int users = 1;
	public int iterations = 10;
	
	public static LocalDate getRandDate() {	
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(15);
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		while(!startDate.isAfter(endDate)) {
			dates.add(startDate);
			startDate = startDate.plusDays(1);
		}
		Random rand = new Random();
		int randInt = rand.nextInt(dates.size());
		return dates.get(randInt);
	}
	
	public static String decode(String url)  
    {  
		try {
			String prevURL="";  
		    String decodeURL = url;  
		    while(!prevURL.equals(decodeURL))  
		    {
		    	prevURL = decodeURL;  
		        decodeURL = URLDecoder.decode( decodeURL, "UTF-8" );
		    }
		    return decodeURL;
		}catch (UnsupportedEncodingException e) {
			return "Issue while decoding" +e.getMessage();  
		}  
    }  
	
	public static URI buildContextURI(String uri,String appendQuery) throws URISyntaxException{
		URI oldUri = new URI(uri);
		String newQuery = oldUri.getQuery();
		if(newQuery == null) {
			newQuery = appendQuery;
		} else {
			newQuery += appendQuery;
		}
		URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery, oldUri.getFragment());
        return newUri;
	}
	
	public AbstractMap.SimpleEntry<String, LocalDate> fetchParams(ArrayList<ArrayList<Double>> coordinates){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", "lane");
		Random rand = new Random();
		int rand_int1 = rand.nextInt(coordinates.size());
		int rand_int2 = rand.nextInt(coordinates.size());
		LocalDate date = getRandDate();
		map.put("startCoordinates", coordinates.get(rand_int1));
		map.put("endCoordinates", coordinates.get(rand_int2));
		map.put("startDate", date.toString());
		String json = Utils.mapToJson(map);
		AbstractMap.SimpleEntry<String, LocalDate> pair = new AbstractMap.SimpleEntry<String, LocalDate>(json, date);
		return pair;
	
	}
	
	public String fetchFilters(LocalDate date) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("start",0);
		map.put("pageSize", 50);
		ArrayList<HashMap<String,Object>> criteria = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("key","pickupDate");
		tempMap.put("function","gte");
		tempMap.put("value",date.minusDays(5).toString());
		criteria.add(tempMap);
		
		tempMap = new HashMap<String,Object>();
		tempMap.put("key","pickupDate");
		tempMap.put("function","lte");
		tempMap.put("value",date.plusDays(5).toString());
		criteria.add(tempMap);
		
		tempMap = new HashMap<String,Object>();
		tempMap.put("key","fromDestination");
		tempMap.put("function","lte");
		tempMap.put("value",150);
		criteria.add(tempMap);
		
		tempMap = new HashMap<String,Object>();
		tempMap.put("key","fromOrigin");
		tempMap.put("function","lte");
		tempMap.put("value",150);
		criteria.add(tempMap);
		
		tempMap = new HashMap<String,Object>();
		tempMap.put("key","sourceId");
		tempMap.put("function","in");
		tempMap.put("value",Arrays.asList(8520071));
		criteria.add(tempMap);
		
		map.put("criteria",criteria);

		return Utils.mapToJson(map);
	}
}
