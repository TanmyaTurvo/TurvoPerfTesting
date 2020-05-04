package com.perf.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDetails {

	private String accountName;
	private String accountId;
	private String shipmentId;
	private String customerOrderId;
	
	public AccountDetails(String accountName, String accountId, String shipmentId, String customerOrderId) {
		this.accountName = accountName;
		this.accountId = accountId;
		this.shipmentId = shipmentId;
		this.customerOrderId = customerOrderId;
	}
	
}
