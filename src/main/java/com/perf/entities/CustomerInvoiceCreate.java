package com.perf.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.CustomerInvoiceInput;
import com.perf.input.params.InputEntries;
import com.perf.input.params.ShipmentCreateInput;
import com.perf.vo.ShipmentDetails;

class MultithreadInvoice extends Thread{
	CustomerInvoiceInput customerInvoiceInput = new CustomerInvoiceInput();
	List<ShipmentDetails> shipmentDetailList;
	InputEntries input = new InputEntries();
	static int count = -1;
	public void run() {
		int currCount = count+1;
		count+=1;
		int loopThread = currCount;
		//shipmentCount should be greater than invoiceThreadCount
		int iterations = ShipmentCreateInput.shipmentCount/CustomerInvoiceInput.invoiceThreadCount;
		int startIndex = loopThread * iterations;
		int endIndex = startIndex + iterations;
		System.out.println(loopThread + ", " + startIndex + ", " + endIndex);
		for (int i=startIndex ; i<endIndex; i++) {
			shipmentDetailList = CustomerInvoiceCreate.shipmentIdList;
			ShipmentDetails shipmentDetails = shipmentDetailList.get(i);
			try {
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpPostConnection(customerInvoiceInput.getUrl(shipmentDetails), "");
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						System.out.println("Auth token expired. Generating again");
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPostConnection(customerInvoiceInput.getUrl(shipmentDetails), "");
					}
				}
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				
				StringBuilder sb = new StringBuilder();
				while((output = br.readLine())!=null) {
					System.out.println(output);
					sb.append(output);
				}
			}
			catch (Exception e){				
				System.out.println("Exception.");
				e.printStackTrace();
			}
		}
	}
} 

public class CustomerInvoiceCreate {
	
	InputEntries input = new InputEntries();
	public static List<ShipmentDetails> shipmentIdList;
	
	public void connect() throws IOException {
		AuthToken.setAuthToken(input.authUrl);
		ShipmentCreate shipmentCreate = new ShipmentCreate();
		shipmentIdList = shipmentCreate.connect();
		for(int i=0; i< CustomerInvoiceInput.invoiceThreadCount; i++) {
			MultithreadInvoice thread = new MultithreadInvoice();
			thread.start();
		}
	}
	
	public static void main(String[] args) {
		CustomerInvoiceCreate invoiceCreate = new CustomerInvoiceCreate();
		try {
			invoiceCreate.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
