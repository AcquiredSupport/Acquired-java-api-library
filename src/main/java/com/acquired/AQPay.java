package com.acquired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * public function
 * 	setParam -- set need parameters
 * 	setBasicParam -- set config parameters and generate request hash code
 * 	clearParam -- clear all seted param
 * 	generateResHash -- generate response hash code
 * 	isSignatureValid -- verify signature is valid
 * 	postJson -- post json data to api url
 * 
 * request function
 * 	auth_
 * 	rebill
 *  postToACS
 *  postSettleACS
 * 	auth_only
 * 	auth_capture
 * 	void_deal
 * 	refund
 * 	credit
 * 	update_billing
 * 	
 */

public class AQPay {
	
	protected AQPayCommon util = new AQPayCommon();
	protected Map<String, String> param = new HashMap<String, String>();	
	protected int connectTimeout;
	
	public AQPay() {
				
		this.connectTimeout = 120;
		
	}
	
	public void setParam(String key, String paramVal) throws Exception {		
		key = this.util.trimString(key);
		if(key != null) {
			this.param.put(key, paramVal);
		}		
	}
	
	public void clearParam() throws Exception {
		this.param.clear();
	}
	
	private void setBasicParam() throws Exception {
		
		this.param.put("company_id", this.param.get("company_id"));
		this.param.put("company_pass", this.param.get("company_pass"));
		this.param.put("company_mid_id", this.param.get("company_mid_id"));
		
		this.param.put("timestamp", this.util.now());
		String hashcode = this.param.get("hash_code");
		this.param.put("request_hash", this.util.requestHash(this.param, hashcode));
	}
	
	public String generateResHash(JsonObject data, String hashcode) throws Exception {		
		return this.util.responseHash(data, hashcode);
	}
	
	public Boolean isSignatureValid(JsonObject response, String hashcode) throws Exception{
		
		String key = response.get("response_hash").getAsString();
		String response_hash = this.generateResHash(response, hashcode);
		if(key.equals(response_hash)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	private JsonObject postJson() throws Exception{
		
		String[] transactionParam = {
				"transaction_type",
				"merchant_order_id",				
				"subscription_type",
				"amount",
				"currency_code_iso3",
				"merchant_customer_id",
				"merchant_custom_1",
				"merchant_custom_2",
				"merchant_custom_3",
				"original_transaction_id",
				"amount"
		};
		String[] customerParam = {
				"customer_title",
				"customer_fname",
				"customer_mname",
				"customer_lname",
				"customer_gender",
				"customer_dob",
				"customer_ipaddress",
				"customer_company"
		};
		String[] billingParam = {
				"cardholder_name",
				"cardnumber",
				"card_type",
				"cardcvv",
				"cardexp",
				"billing_street",
				"billing_street2",
				"billing_city",
				"billing_state",
				"billing_zipcode",
				"billing_country_code_iso2",
				"billing_phone",
				"billing_email"
		};
		String[] tdsParam = {
				"action",
				"pares",
				"ipaddress"
		};
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, String> transactionData = new HashMap<String, String>();
		Map<String, String> customerData = new HashMap<String, String>();
		Map<String, String> billingData = new HashMap<String, String>();
		Map<String, String> tdsData = new HashMap<String, String>();
		
		for(String key: this.param.keySet()) {
			String value = this.param.get(key);
			if(Arrays.asList(transactionParam).contains(key)) {
				transactionData.put(key, value);
				continue;
			}
			if(Arrays.asList(customerParam).contains(key)) {
				customerData.put(key, value);
				continue;
			}
			if(Arrays.asList(billingParam).contains(key)) {
				billingData.put(key, value);
				continue;
			}
			if(Arrays.asList(tdsParam).contains(key)) {
				tdsData.put(key, value);
				continue;
			}
			data.put(key, value);
		}
		
		if(!transactionData.isEmpty()) {
			data.put("transaction", transactionData);
		}
		if(!customerData.isEmpty()) {
			data.put("customer", customerData);
		}
		if(!billingData.isEmpty()) {
			data.put("billing", billingData);
		}
		if(!tdsData.isEmpty()) {
			data.put("tds", tdsData);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
		String url = this.param.get("request_url");
		
		String response = this.util.http_request(url, json, this.connectTimeout, "json");
		
		JsonObject result = new JsonParser().parse(response).getAsJsonObject();				
		
		this.clearParam();
		
		return result;
		
	}
	
	public JsonObject auth_() throws Exception{
				
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public JsonObject rebill() throws Exception{
		
		this.setParam("subscription_type", "REBILL");
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public String postToACS() throws Exception{
		
		String url = this.param.get("ACS_url");
		String pareq = this.param.get("pareq");
		String termurl = this.param.get("termurl");
		String mdstr = this.param.get("md");
		
		String post_data = "pareq=" + pareq + "&termurl=" + termurl + "&md=" + mdstr;
		String response = this.util.http_request(url, post_data, this.connectTimeout);
		
		this.clearParam();
		
		return response;
		
	}
	
	public JsonObject postSettleACS() throws Exception{
		
		this.setParam("action", "SETTLEMENT");
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public JsonObject auth_only() throws Exception{
		
		this.setParam("transaction_type", "AUTH_ONLY");
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public JsonObject auth_capture() throws Exception{
		
		this.setParam("transaction_type", "AUTH_CAPTURE");
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public JsonObject capture() throws Exception{
		
		this.setParam("transaction_type", "CAPTURE");
		this.setBasicParam();
		return this.postJson();
		
	} 
	
	public JsonObject void_deal() throws Exception{
		
		this.setParam("transaction_type", "VOID");
		this.setBasicParam();						
		return this.postJson();
		
	}
	
	public JsonObject refund() throws Exception{
		
		this.setParam("transaction_type", "REFUND");
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public JsonObject credit() throws Exception{
		
		this.setParam("transaction_type", "CREDIT");
		this.setBasicParam();
		return this.postJson();
		
	}
	
	public JsonObject update_billing() throws Exception{
		
		this.setParam("transaction_type", "SUBSCRIPTION_MANAGE");
		this.setParam("subscription_type", "UPDATE_BILLING");
		this.setBasicParam();
		return this.postJson();
		
	}
	
}