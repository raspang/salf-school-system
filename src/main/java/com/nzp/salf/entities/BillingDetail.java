package com.nzp.salf.entities;

public enum BillingDetail {

	ENTRANCE("Entrance"), 
	UNITS("No of Units"), 
	MISCELLANEOUS("Miscellaneous"), 
	LABORATORY("Laboratory"), 
	EVALUATION("Evaluation Fee"), 
	LESS("Less"), 
	BALANCE("Balance"), 
	PAYMENT_PER_EXAM("Payment Per Exam");

	BillingDetail(String detail) {
		this.detail = detail;
	}

	private String detail;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
