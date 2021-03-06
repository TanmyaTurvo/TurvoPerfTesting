package com.perf.input.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.perf.utils.Utils;

public class ShipmentPostingInput {
	InputEntries input = new InputEntries();
	public String url = input.domain + "/api/shipment-postings";
	public String inputFile = "LocationIDs.txt";
	public int shipmentPostingLoop = 1;
	
	public String getShipmentPostingPostData(String shipmentId) {
		
		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> subMap = new HashMap<>();
		subMap.put("id", 10713251);
		subMap.put("key", "priceOption.minCarrierPay");
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
		Map<String, Object> networks = new HashMap<>();
		networks.put("id", 10713254L);
		networks.put("key", "shipmentSource.network");
		networks.put("value", "My network");
		list.add(networks);
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
