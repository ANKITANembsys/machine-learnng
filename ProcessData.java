package com.ezops.crawlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.ezops.db.conn.OraConn;
import com.ezops.db.mongo.MongoEZ;

public class ProcessData {
	OraConn oConn;
	MongoEZ mConn;

	public ProcessData() {
		oConn = new OraConn();
		mConn = new MongoEZ();
	}

	@SuppressWarnings("rawtypes")
	public String dataManipulate(Map tabData) {
		String tablesOrgInfo = (String) ((Map) ((Map) tabData.get("TablesData"))
				.get("report")).get("Organization");
		String tabType = (String) ((Map) ((Map) tabData.get("TablesData"))
				.get("report")).get("Type");

		if (tabType == null) {
			tabType = "Invoice";
		}

		boolean status = saveTableToMongo(tabData);
		return tablesOrgInfo + ", " + tabType;
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public boolean saveTableToMongo(Map tabData) {
		Map tablesOrg = (Map) tabData.get("TablesData");
		String tablesOrgInfo = ((Map) tablesOrg.get("report")).get(
				"Organization").toString();

		String query = "select * from orginfo where orgname like '%"
				+ tablesOrgInfo + "%'"; 

		Map getOrgInfo = mConn.getOrganizationInfo(tablesOrgInfo);
		Map tablesInfo = (Map) ((Map) tabData.get("TablesData")).get("report");
		if (!(getOrgInfo.isEmpty()))
			tablesInfo.putAll(getOrgInfo);

		// Map getOrgInfo = new HashMap();
		// getOrgInfo.put("Organization", tablesOrgInfo);
		if (null == tabData)
			tabData = getSameData();
		JSONObject reportData = createJSON((Map) tabData.get("TablesData"));

		String tabType = (String) ((Map) ((Map) tabData.get("TablesData"))
				.get("report")).get("Type");
		if (tabType == null) {
			tabType = "Invoice"; // Check Later                                   
		}
		mConn.InsertToMongo(tablesOrgInfo, tabType, reportData);
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getSameData() {

		Map temp = new HashMap();
		temp.put("Organization", "JPM");
		Map temp1 = new HashMap();
		temp1.put("Type", "BalanceSheet");
		temp1.put("Year", "2016");
		temp1.put("Frequency", "Q1");
		temp1.put("Asset", "");
		temp1.put("Current assets", "14,523");
		temp1.put("Cash and Cash equivalents", "18,566");
		temp1.put("Accounts receivable", "10,985");
		temp1.put("Inventories", "8,184");
		temp1.put("Deferred taxes on income", "3,567");
		temp1.put("Prepaid expenses and other receivables", "3,486");
		temp1.put("Total current assets", "59,311");
		temp.put("BalanceSheet", temp1);

		return temp;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSONObject createJSON(Map tabData) {
		Map tempMap = new LinkedHashMap();
		tempMap.putAll(tabData);
		// tempMap.putAll(getOrgInfo);
		JSONObject jz = new JSONObject(tempMap);
		return jz;
	}
	@SuppressWarnings({ "rawtypes" })
	public JSONObject createJSON_list(ArrayList tabData) {
		
		
		// tempMap.putAll(getOrgInfo);
		JSONObject jz = new JSONObject(tabData);
		return jz;
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ProcessData call = new ProcessData();
		// call.saveTableToMongo(call.getSameData());
		// System.out.println(call.dataManipulate());
	}
}
