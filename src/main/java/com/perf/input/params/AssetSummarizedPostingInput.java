package com.perf.input.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.perf.utils.Utils;

public class AssetSummarizedPostingInput {
	InputEntries input = new InputEntries();
	public String url = input.domain + "/api/";
	public String inputFile = "LocationIDs.txt";
	public int shipmentPostingLoop = 5;
	
	public String getShipmentPostingPostData(String shipmentId) {
		
		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> subMap = new HashMap<>();
		subMap.put("id", 8829531);
		subMap.put("key", "priceOption.shipmentPosting.minCarrierPay");
		subMap.put("value", "Min pay");
		map.put("type", subMap);
		
		Map<String,Object> unitsMap = new HashMap<>();
		unitsMap.put("id", 100065);
		unitsMap.put("key","1550");
		unitsMap.put("value", "USD");
		
		Random rand = new Random();
		subMap = new HashMap<>();
		subMap.put("units", unitsMap);
		subMap.put("value", rand.nextInt(1901)+100);
		map.put("value", subMap);
		
		
		mainMap.put("requestPrice", map);
		
		List<Map<String,Object>> list = new ArrayList<>();
		mainMap.put("networks",list);
		
		map=new HashMap<>();
		map.put("type", "SHIPMENT");
		map.put("value", Integer.parseInt(shipmentId));
		mainMap.put("context",map);
		
		mainMap.put("carriers", new ArrayList<>());
		
		String json = Utils.mapToJson(mainMap);
		return json;
		
	}
	
}
