package com.perf.input.params;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.perf.utils.Utils;
import com.perf.vo.ShipmentDetails;


public class CustomerInvoiceInput {
	InputEntries input = new InputEntries();
	public static int invoiceThreadCount = 5;
	public String url = input.domain + "/api/documents?attributes=";
	
	public String getUrl(ShipmentDetails shipmentDetails) throws UnsupportedEncodingException {
		return url + invoiceAttributes(shipmentDetails) + "&fullResponse=true&context=" + invoiceContext(shipmentDetails);
	}
	
	public String invoiceAttributes(ShipmentDetails shipmentDetails) throws UnsupportedEncodingException {
		Map<String, Object> mainMap = new HashMap<>();
		mainMap.put("lookupId", "3361751");
		mainMap.put("create", true);
		Map<String, Object> map = new HashMap<>();
		map.put("id", Integer.parseInt(shipmentDetails.getCustomer_order_id()));
		map.put("accountId", Integer.parseInt(shipmentDetails.getCustomerId()));
		map.put("type", "CUSTOMERORDER");
		mainMap.put("account", map);
		mainMap.put("invoice_date", "2020-02-25");
		mainMap.put("invoice_amount", "100");
		mainMap.put("due_date", "2020-03-26");
		mainMap.put("invoice_no", Utils.getRandomString(10));
		map = new HashMap<>();
		map.put("id", 114629);
		map.put("key", "2804");
		map.put("value", "Approved");
		mainMap.put("documentStatus", map);
		
		String uriString = Utils.mapToJson(mainMap);
		return URLEncoder.encode(uriString,"UTF-8");
	}
	
	public String invoiceContext(ShipmentDetails shipmentDetails) throws UnsupportedEncodingException {
		Map<String, Object> mainMap = new HashMap<>();
		mainMap.put("id", Integer.parseInt(shipmentDetails.getShipmentId()));
		mainMap.put("type", "SHIPMENT");
		
		String uriString = Utils.mapToJson(mainMap);
		return URLEncoder.encode(uriString, "UTF-8");
	}
	
}
