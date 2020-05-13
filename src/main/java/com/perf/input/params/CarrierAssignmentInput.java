package com.perf.input.params;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perf.utils.Utils;

public class CarrierAssignmentInput {
	
	InputEntries input = new InputEntries();
	public String url = input.domain + "/api/shipments/";
	public String carrierListUrl = input.domain + "/api/accounts/carrier/list?filter=";
	
	public String getCarrierListUrl() throws UnsupportedEncodingException {
		System.out.println(carrierListUrl + buildFilter());
		return carrierListUrl + buildFilter();
	}
	public String getUrl(String shipmentId) {
		return url + shipmentId + "?types=%5B%22details%22%2C%22exceptions%22%5D&fullResponse=true";
	}
	
	private String buildFilter() throws UnsupportedEncodingException {
		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("key", "status.code");
		criteria.put("function", "eq");
		criteria.put("value", 100180);
		List<Map<String, Object>> criteriaList = new ArrayList<>();
		criteriaList.add(criteria);
		
		mainMap.put("criteria", criteriaList);
		mainMap.put("pageSize", 24);
		mainMap.put("start", 0);
		mainMap.put("sortBy", "name");
		mainMap.put("sortDirection", "asc");
		
		String json = Utils.mapToJson(mainMap);
		return URLEncoder.encode(json,"UTF-8");
	}
	
	public List<String> fetchCarrierIdList(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(json,
				new TypeReference<Map<String, Object>>(){});
		List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("accounts");
		List<String> carrierIdList = new ArrayList<>();
		for(Map<String, Object> item : items) {
			carrierIdList.add(item.get("id").toString());
		}
		System.out.println(carrierIdList);
		return carrierIdList;
	}
	
	public String buildPayload(String carrierId) {
		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> carrier_orders = new HashMap<>();
		Map<String, Object> carrier = new HashMap<>();
		carrier.put("id", carrierId);
		carrier_orders.put("carrier", carrier);
		carrier_orders.put("_operation", 0);
		List<Map<String, Object>> carrierList = new ArrayList<>();
		carrierList.add(carrier_orders);
		mainMap.put("carrier_orders", carrierList);
		mainMap.put("componentKey", 11674);
		
		String json = Utils.mapToJson(mainMap);
		return json;
	}
	
}
