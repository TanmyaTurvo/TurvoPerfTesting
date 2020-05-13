package com.perf.input.params;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.perf.utils.Utils;

public class FindCarrierInput {
	InputEntries input = new InputEntries();
	public int threads = 1;
	public int iterations = 1;
	public String tempUrl = input.domain + "/api/network/available-assets?context=";
	public static String url;
	
	public void setUrl() throws UnsupportedEncodingException {
		url = buildUrl();
		System.out.println(url);
	}
	
	public String buildUrl() throws UnsupportedEncodingException {
		return tempUrl + buildContext() + "&filter=" + buildFilter();
	}
	
	private String buildContext() throws UnsupportedEncodingException {
		Map<String, Object> contextMap = new HashMap<>();
		contextMap.put("type", "lane");
		contextMap.put("startDate", LocalDate.now().toString());
		contextMap.put("startTimezone", "America/Los_Angeles");
		
		Map<String, Object> transportationMode = new HashMap<>();
		transportationMode.put("id", 2902366);
		transportationMode.put("key", "33600");
		transportationMode.put("value", "FTL");
		
		contextMap.put("transportationMode", transportationMode);
		
		String json = Utils.mapToJson(contextMap);
		return URLEncoder.encode(json,"UTF-8");
	}
	
	private String buildFilter() throws UnsupportedEncodingException {
		Map<String, Object> filterMap = new HashMap<>();
		List<Map<String,Object>> criteriaList = new ArrayList<>();
		Map<String, Object> criteriaMap = new HashMap<>();
		criteriaMap.put("key", "pickup_date");
		criteriaMap.put("function", "gte");
		criteriaMap.put("value", LocalDate.now().minusDays(2).toString());
		criteriaList.add(criteriaMap);
		
		criteriaMap = new HashMap<>();
		criteriaMap.put("key", "pickup_date");
		criteriaMap.put("function", "lte");
		criteriaMap.put("value", LocalDate.now().plusDays(2).toString());
		criteriaList.add(criteriaMap);
		
		criteriaMap = new HashMap<>();
		criteriaMap.put("key", "type_id");
		criteriaMap.put("function", "in");
		List<Integer> typeList = Arrays.asList(119393,119396,119395,119394);
		criteriaMap.put("values", typeList);
		criteriaList.add(criteriaMap);
		
		criteriaMap = new HashMap<>();
		criteriaMap.put("key", "route_type_id");
		criteriaMap.put("function", "in");
		List<Integer> routeList = Arrays.asList(3363747,3363748,3363749,3363750,3363751,3367501);
		criteriaMap.put("values", routeList);
		criteriaList.add(criteriaMap);
		
		filterMap.put("criteria", criteriaList);
		
		String json = Utils.mapToJson(filterMap);
		return URLEncoder.encode(json,"UTF-8");
	}
	
}
