package com.perf.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.entities.location.LocationCreate;
import com.perf.entities.location.LocationDetailsUtils;
import com.perf.entities.posting.shipment.ShipmentPostingCreate;
import com.perf.input.params.InputEntries;
import com.perf.input.params.LocationCreateInput;
import com.perf.input.params.ShipmentCreateInput;
import com.perf.utils.Utils;
import com.perf.vo.ShipmentDetails;
import com.perf.vo.ShipmentFunctionEnum;

public class ShipmentCreate extends Thread{
	
	private static List<String> customerAccountIdList;
	private static List<ShipmentDetails> shipmentDetailsList = new ArrayList<>();
	
	ShipmentCreateInput shipmentCreateInput = new ShipmentCreateInput();
	InputEntries input = new InputEntries();
	ShipmentPostingCreate shipmentPostingCreate = new ShipmentPostingCreate();
	
	
	public void run() {
		shipmentCreate();
	}
	
	public void shipmentCreate() {
		
		HttpConnection httpConnection = new HttpConnection();
		Random rand = new Random();
		for(int i=0; i<ShipmentCreateInput.shipmentCount; i++) {
			String currShipmentId = null;
			String currCustomerOrderId = null;
			String customerId =  customerAccountIdList.get(rand.nextInt(customerAccountIdList.size()));
			String currUrl = shipmentCreateInput.url + customerId;
			
			try {
				HttpURLConnection conn = httpConnection.httpPostConnection(currUrl, shipmentCreateInput.getShipmentPayload());
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPostConnection(currUrl, shipmentCreateInput.getShipmentPayload());
					}
				}
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				
				StringBuilder sb = new StringBuilder();
				while((output = br.readLine())!=null) {
					System.out.println("SHIPMENT: " + output);
					sb.append(output);
				}
				currShipmentId = Utils.getValueFromJson(sb, "shipmentId");
				currCustomerOrderId = Utils.getValueFromJson(sb, "customer_order_id");
				
				//ShipmentCreateLineItem shipmentCreateLineItem = new ShipmentCreateLineItem();
				//shipmentCreateLineItem.setShipmentCreateLineItem(currShipmentId, currCustomerOrderId);
				
				ShipmentDetails shipmentDetails = new ShipmentDetails();
				shipmentDetails.setCustomer_order_id(currCustomerOrderId);
				shipmentDetails.setCustomerId(customerId);
				shipmentDetails.setShipmentId(currShipmentId);
				shipmentDetailsList.add(shipmentDetails);
				
				if(ShipmentCreateInput.shipmentFunction == ShipmentFunctionEnum.SHIPMENT_POSTING) {
					shipmentPostingCreate.postShipment(currShipmentId);
				}
				
				
			} catch (Exception e){				
				System.out.println("Exception.");
				e.printStackTrace();
			}
		}
	}
	
	public List<ShipmentDetails> connect() throws IOException {
		AuthToken.setAuthToken(input.authUrl);
		//LocationCreate.connect();
		LocationDetailsUtils locationDetailsUtils = new LocationDetailsUtils();
		ShipmentCreateInput.locationIdList = locationDetailsUtils.getLocationIds();
		CustomerCreate customerCreate = new CustomerCreate();
		customerAccountIdList = customerCreate.connect();
		for(int i=0; i<ShipmentCreateInput.shipmentThreadCount ;i++) {
			ShipmentCreate shipmentCreate = new ShipmentCreate();
			shipmentCreate.start();
		}
		return shipmentDetailsList;
	}
	
	public static void main(String[] args) {
		ShipmentCreate shipmentCreate = new ShipmentCreate();
		try {
			shipmentCreate.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
