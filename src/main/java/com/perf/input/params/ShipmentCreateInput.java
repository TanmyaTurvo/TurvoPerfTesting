package com.perf.input.params;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.perf.utils.Utils;

public class ShipmentCreateInput {
	
	InputEntries input = new InputEntries();
	public String url = input.domain+"/api/shipments?customerId=";
	public static int shipmentCount = 5;
	
	public static List<String> locationIdList;
	public String getLineItemUrl(String shipmentId) {
		return input.domain+"/api/shipments/"+ shipmentId +"?fullResponse=true";
	}
	
	public String getShipmentPayload() {
		LocalDate date = LocalDate.now();
		Map<String, Object> mainMap = new HashMap<>();
		mainMap.put("ltlShipment", false);
		Random random = new Random();
		mainMap.put("lane_start", locationIdList.get(random.nextInt(locationIdList.size())));
		mainMap.put("lane_end", locationIdList.get(random.nextInt(locationIdList.size())));
		mainMap.put("start_date", date);
		mainMap.put("end_date", date.plusDays(1));
		List<String> list = new ArrayList<>();
		list.add("100026");
		mainMap.put("equipment_type", list);
		Map<String, Object> map = new HashMap<>();
		map.put("id", 100156);
		map.put("key", "2001");
		map.put("value", "Plan");
		mainMap.put("phase", map);
		
		return Utils.mapToJson(mainMap);
	}
	
	public String getShipmentLineItemPayload(String customerOrderId) {
		Map<String, Object> mainMap = new HashMap<>();
		mainMap.put("componentKey", 11622);
		
		Map<String, Object> lineItemsType = new HashMap<>();
		lineItemsType.put("id", 3361703);
		lineItemsType.put("key", "1600");
		lineItemsType.put("value", "Freight - flat");
		Map<String, Object> lineItems = new HashMap<>();
		lineItems.put("price", 100);
		lineItems.put("billable", true);
		lineItems.put("type", lineItemsType);
		lineItems.put("qty", 1);
		lineItems.put("amount", 100);
		lineItems.put("_operation", 0);
		
		List<Map<String, Object>> lineItemsList = new ArrayList<>();
		lineItemsList.add(lineItems);
		
		Map<String, Object> costsMap = new HashMap<>();
		costsMap.put("line_items", lineItemsList);
		costsMap.put("contract", null);
		costsMap.put("amount", 100);
		costsMap.put("totalTax", 0);
		costsMap.put("subTotal", 100);
		
		Map<String, Object> customerMap = new HashMap<>();
		customerMap.put("costs", costsMap);
		customerMap.put("customer_order_id", customerOrderId);
		
		List<Map<String, Object>> customerList = new ArrayList<>();
		customerList.add(customerMap);
		
		mainMap.put("customer_orders", customerList);
		
		return Utils.mapToJson(mainMap);
	}
	
}
