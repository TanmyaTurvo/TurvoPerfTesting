package com.perf.input.params;

import java.util.HashMap;
import java.util.Map;

import com.perf.utils.Utils;

public class CarrierQuoteInput {
	
	InputEntries input = new InputEntries();
	String url = input.domain + "/api/shipments/";
	
	public String getUrl(String shipmentId) {
		return url + shipmentId + "?fullResponse=true&types=%5B%22details%22%2C%22exceptions%22%5D";
	}
	
	public String buildPayload() {
		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> carrier_bid_ask = new HashMap<>();
		carrier_bid_ask.put("notes", "");
		mainMap.put("carrier_bid_ask", carrier_bid_ask);
		
		String json = Utils.mapToJson(mainMap);
		return json;
	}
	
}
