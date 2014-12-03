

package com.omniture.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/*
 * PLEASE BE AWARE THAT NOT ALL EXAMPLES BELOW WILL WORK WITH API 1.4 AS SOME METHODS WERE REMOVED
 * CHANGE ENDPOINT FROM 1.3 TO 1.4 if you want to use API 1.4
 * NOT ALL APIs HAVE A 1.4 VERSION
 */
public class OMTR_REST_Custom {
	private static String USERNAME = "[INPUT YOUR SECRET USERNAME]";
	private static String PASSWORD = "[INPUT YOUR SECRET PASSWORD]";

	/*Make sure to point to the right data center 
	 * Sanjose : api.omniture.com
	 * Dallas : api2.omniture.com
	 * London : api3.omniture.com
	 * Singapore : api4.omniture.com
	 * Portland : api5.omniture.com
	 * */
	private static String ENDPOINT = "https://[INPUT THE RIGHTDATA CENTER SEE ABOVE]/admin/1.3/rest/";
	
	
	private OMTR_REST_Custom() {}
		
	public static String callMethod(String method, String data) throws IOException {
		URL url = new URL(ENDPOINT + "?method=" + method);
		URLConnection connection = url.openConnection();
		connection.addRequestProperty("X-WSSE", getHeader());
		connection.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		System.out.println(data);
		wr.write(data);
		wr.flush();
		System.out.println("1");
		InputStream in = connection.getInputStream();
		BufferedReader res = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		System.out.println("2");
		StringBuffer sBuffer = new StringBuffer();
		String inputLine;
		while ((inputLine = res.readLine()) != null)
			sBuffer.append(inputLine);
		
		res.close();
		
		return sBuffer.toString();
	}
	
	private static String getHeader() throws UnsupportedEncodingException {
		byte[] nonceB = generateNonce();
		String nonce = base64Encode(nonceB);
		String created = generateTimestamp();
		String password64 = getBase64Digest(nonceB, created.getBytes("UTF-8"), PASSWORD.getBytes("UTF-8"));
		StringBuffer header = new StringBuffer("UsernameToken Username=\"");
		header.append(USERNAME);
		header.append("\", ");
		header.append("PasswordDigest=\"");
		header.append(password64.trim());
		header.append("\", ");
		header.append("Nonce=\"");
		header.append(nonce.trim());
		header.append("\", ");
		header.append("Created=\"");
		header.append(created);
		header.append("\"");
		return header.toString();
	}
	
	private static byte[] generateNonce() {
	    String nonce = Long.toString(new Date().getTime());
	    return nonce.getBytes();
	}
	
	private static String generateTimestamp() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return dateFormatter.format(new Date());
	}

	private static synchronized String getBase64Digest(byte[] nonce, byte[] created, byte[] password) {
	  try {
	    MessageDigest messageDigester = MessageDigest.getInstance("SHA-1");
	    // SHA-1 ( nonce + created + password )
	    messageDigester.reset();
	    messageDigester.update(nonce);
	    messageDigester.update(created);
	    messageDigester.update(password);
	    return base64Encode(messageDigester.digest());
	  } catch (java.security.NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	  }
	}

	private static String base64Encode(byte[] bytes) {
	  return Base64Coder.encodeLines(bytes);
	}

	public static void main(String[] args) throws IOException {
		boolean choice = true;
		while(choice){
				choice = menu();
		}
		System.out.println("/****************END****************/");
		
	}
	
	/*
	 * Menu of requests that can be run for this example
	 */
	private static boolean menu() throws IOException {
		int choice=0;
		boolean cont = false;
		do{
		Scanner sc = new Scanner(System.in);
	    System.out.println("Please type 1 for the list of report suites");
	    System.out.println("Please type 2 for the number of tokens left");
	    System.out.println("Please type 3 for the list of logins");
	    System.out.println("Please type 4 for a queue ranked request");
	    System.out.println("Please type 5 for a queue trended request");
	    System.out.println("Please type 6 for a queue Overtime request");
	    System.out.println("Please type 7 to change the Internal URL filter");
	    
	    choice = sc.nextInt();
		}while(choice != 1 && choice != 2 && choice != 3 && choice != 4 && choice != 5 && choice != 6 && choice != 7);
		
		if(choice == 1){
			getReportSuites();
		}
		
		if(choice == 2){
			getTokenNumbers();
		}
		
		if(choice == 3){
			getUserLogins();
		}
		
		if(choice == 4){
			/*Specify metrics to return
			 * Example : String[] metrics = {"pageViews","visits"};
			 * */
			String[] metrics = {""};
			/*Specify elements to return
			 * Example String[] elements = {"page"};
			 * */
			String[] elements = {""};
			/*Specify the report suite to query dfrom
			 * Example: String reportSuitreID="edirocks";
			 * */
			String reportSuitreID="";
			/*Specify start date of report
			 * Example: String startDate = "2013-08-01";
			 * */
			String startDate = "";
			/*Specify end date of report
			 * Example: String endDate = "2013-08-31";;
			 * */
			String endDate = "";
			/*Run the regetRankedRequest*/
			getRankedReport(metrics.length,metrics,elements.length,elements,reportSuitreID,startDate,endDate);
		}
		
		if(choice == 5){
			/*Specify metrics to return
			 * Example : String[] metrics = {"pageViews","visits"};
			 * */
			String[] metrics = {""};
			/*Specify elements to return
			 * Example String[] elements = {"page"};
			 * */
			String[] elements = {""};
			/*Specify the report suite to query dfrom
			 * Example: String reportSuitreID="edirocks";
			 * */
			String reportSuitreID="";
			/*Specify start date of report
			 * Example: String startDate = "2013-08-01";
			 * */
			String startDate = "";
			/*Specify end date of report
			 * Example: String endDate = "2013-08-31";;
			 * */
			String endDate = "";
			/*Run the trended request*/
			getTrendedReport(metrics.length,metrics,elements.length,elements,reportSuitreID,startDate,endDate);
		}
		
		if(choice == 6){
			/*Specify metrics to return
			 * Example : String[] metrics = {"pageViews","visits"};
			 * */
			String[] metrics = {""};
			/*Specify the report suite to query dfrom
			 * Example: String reportSuitreID="edirocks";
			 * */
			String reportSuitreID="";
			/*Specify start date of report
			 * Example: String startDate = "2013-08-01";
			 * */
			String startDate = "";
			/*Specify end date of report
			 * Example: String endDate = "2013-08-31";;
			 * */
			String endDate = "";	
			getOvertimeReport(metrics.length,metrics,reportSuitreID,startDate,endDate);
		}
		
		if(choice == 7){
			int secondchoice=0;
			Scanner sc = new Scanner(System.in);
			/*Specify the domain to add to Internal URL filters
			 * Example: String endDate = "2013-08-31";;
			 * */
			String[] filters = {""};
			/*Specify the report suite to query dfrom
			 * Example: String reportSuitreID="edirocks";
			 * */
			String[] reportSuitreID={""};
			do{
			System.out.println("Do you want to add [1] or delete [2]");
			secondchoice = sc.nextInt();
			}while(secondchoice!= 1 && secondchoice!= 2);
			
			if(secondchoice == 1){
				AddInternalURLFilters(filters, reportSuitreID);
			}else if(secondchoice == 2){
				DeleteInternalURLFilters(filters, reportSuitreID);
			}
		}
		
		do{
			choice = 0;
			Scanner sc = new Scanner(System.in);
			System.out.println("Do you want to make another request ? (Type 1 for YES and 2 for NO)");
			choice = sc.nextInt();
		}while(choice != 1 && choice != 2);
		
		if(choice == 1 ){
			cont = true;
		}
		
		if(choice == 2){
			cont = false;
		}
		return cont;
		
	}
	
	/*
	 * This will return all the report suites for the company
	 */
	private static void getReportSuites() throws IOException{
		//Get all the report suites
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("types", new String[]{"standard"});
		map.put("search", "");

		String response = OMTR_REST_Custom.callMethod("Company.GetReportSuites", JSONObject.fromObject(map).toString());
		System.out.println(response);
		JSONObject jsonObj = JSONObject.fromObject(response);
		JSONArray jsonArry = JSONArray.fromObject(jsonObj.get("report_suites"));
		
		System.out.println("List of all the report suites :");
		System.out.println("-----------------------------------------------------");
	
		for(int i = 0; i < jsonArry.size(); i++) {
			System.out.println("Report Suite ID: " + JSONObject.fromObject(jsonArry.get(i)).get("rsid"));
			System.out.println("Site Title: " + JSONObject.fromObject(jsonArry.get(i)).get("site_title"));
			System.out.println();
		}
	}
	
	/*
	 * This will return the number of tokens left.
	 */
	private static void getTokenNumbers() throws IOException{
		//Get number of tokens left
		String response2 = OMTR_REST_Custom.callMethod("Company.GetTokenCount", "");
		System.out.println("Number of tokens left :" + response2);
	}
	
	/*
	 * This will return all the users login
	 */
	private static void getUserLogins() throws IOException{
		//Get logins
		String response3 = OMTR_REST_Custom.callMethod("Permissions.GetLogins", "");
		System.out.println(response3);
		response3 = response3.replace("[{","");
		response3 = response3.replace("}]","");
		response3 = response3.replace("{","");
		String[] ary = response3.split("},");
		for(int i = 0; i < ary.length; i++) {
			System.out.println(ary[i]);
			String[] user = ary[i].split(",");
			for(int j = 0; j < user.length; j++) {
				System.out.println(user[j]);
			}
			System.out.println();
		
		}
	}
	
	/*
	 * This will return a ranked report
	 */
	private static void getRankedReport(int numMetrics, String[] metrics, int numElements, String[] elements, String reportSuiteID, String startDate, String endDate) throws IOException{
		/*This will contain the REST request sent to Adobe servers*/
		Map<String, Object> fullRanked = new HashMap<String, Object>();
		
		/*A report description that specifies the desired Ranked report contents. */
		Map<String, Object> reportDescription = new HashMap<String, Object>();
		
		/*A list of the events to include in the report. A report must specify at least one metric */
		ArrayList<Map<String,Object>> metriclist = new ArrayList<Map<String,Object>>();
		
		/*A list of elements that breaks down (organizes) the metrics data in the report.*/
		ArrayList<Map<String,Object>> elementslist = new ArrayList<Map<String,Object>>();
		
		//1.Set the report suite ID
		reportDescription.put("reportSuiteID", reportSuiteID); //put the right report suite ID
		
		//2.Set the date from and to
		reportDescription.put("dateFrom", startDate);
		reportDescription.put("dateTo", endDate);
		
		//3.Set metrics
		for(int i=0;i<numMetrics;i++){
			Map<String, Object> metric = new HashMap<String, Object>();
			metric.put("id", metrics[i]);
			metriclist.add(metric);
		}
		reportDescription.put("metrics", metriclist);
		
		//4.Set elements
		for(int i=0;i<numElements;i++){
			Map<String, Object> element = new HashMap<String, Object>();
			element.put("id",elements[i]);
			elementslist.add(element);
			
		}
		reportDescription.put("elements", elementslist);
	
		
		//5.Build the Ranked report
		fullRanked.put("reportDescription", reportDescription);
		fullRanked.put("validate", true);
		
		//6.Request the Ranked report
		
		String responseQueued = OMTR_REST_Custom.callMethod("Report.QueueRanked", JSONObject.fromObject(fullRanked).toString());
		System.out.println(responseQueued);
		
		//7.Extract reportID
		JSONObject jsonObjQueued = JSONObject.fromObject(responseQueued);
		Map<String, Object> reportID = new HashMap<String, Object>();
		reportID.put("reportID", jsonObjQueued.get("reportID"));
		
		//8.Check if report is fully processed
		JSONObject jsonObjReportStatus;
		do{
			String reportStatus = OMTR_REST_Custom.callMethod("Report.GetStatus", JSONObject.fromObject(reportID).toString());
			System.out.println(reportStatus);
			jsonObjReportStatus = JSONObject.fromObject(reportStatus);
			System.out.println(jsonObjReportStatus.get("status"));
		}while(!jsonObjReportStatus.get("status").equals("ready") && !jsonObjReportStatus.get("status").equals("done"));
		String report;
		
		//9.ONce report is ready request the report
		do{
			report = OMTR_REST_Custom.callMethod("Report.GetReport", JSONObject.fromObject(reportID).toString());				
			jsonObjReportStatus = JSONObject.fromObject(report);
			System.out.println(jsonObjReportStatus.get("status"));
			System.out.println("Final Report status: " + jsonObjReportStatus.get("status"));
		}while(!jsonObjReportStatus.get("status").equals("done"));
		
		//10.display final report
		System.out.println("Final Report: " + report);
		JSONObject jsonObj = JSONObject.fromObject(report);
		JSONObject reportDetails = JSONObject.fromObject(jsonObj.get("report"));
		System.out.println("Report Details : " +reportDetails);
		JSONArray reportSuiteDetails = JSONArray.fromObject(reportDetails.get("reportSuite"));
		System.out.println("Report Suite Details : " +reportSuiteDetails);
		
		String reportDateDetails = reportDetails.get("period").toString();
		System.out.println("Report Suite Details : " +reportDateDetails);
		
		JSONArray reportDataDetails = JSONArray.fromObject(reportDetails.get("data"));
		System.out.println("Report Suite Details : " +reportDataDetails);
		for(int i=0;i<reportDataDetails.size();i++){
			
			System.out.println( JSONObject.fromObject(reportDataDetails.get(i)).get("name"));
		}
		
	}
	
	/*
	 * This will return a trended report
	 */
	private static void getTrendedReport(int numMetrics, String[] metrics, int numElements, String[] elements, String reportSuiteID, String startDate, String endDate) throws IOException{
		/*This will contain the REST request sent to Adobe servers*/
		Map<String, Object> fullRanked = new HashMap<String, Object>();
		
		/*A report description that specifies the desired Ranked report contents. */
		Map<String, Object> reportDescription = new HashMap<String, Object>();
		
		/*A list of the events to include in the report. A report must specify at least one metric */
		ArrayList<Map<String,Object>> metriclist = new ArrayList<Map<String,Object>>();
		
		/*A list of elements that breaks down (organizes) the metrics data in the report.*/
		ArrayList<Map<String,Object>> elementslist = new ArrayList<Map<String,Object>>();
		
		//1.Set the report suite ID
		reportDescription.put("reportSuiteID", reportSuiteID); //put the right report suite ID
		
		//2.Set the date from and to
		reportDescription.put("dateFrom", startDate);
		reportDescription.put("dateTo", endDate);
		
		//3.Set metrics
		for(int i=0;i<numMetrics;i++){
			Map<String, Object> metric = new HashMap<String, Object>();
			metric.put("id", metrics[i]);
			metriclist.add(metric);
		}
		reportDescription.put("metrics", metriclist);
		
		//4.Set elements
		for(int i=0;i<numElements;i++){
			Map<String, Object> element = new HashMap<String, Object>();
			element.put("id",elements[i]);
			elementslist.add(element);
			
		}
		reportDescription.put("elements", elementslist);
		
		//5.Set granularity of the report
		/*hour 	Displays report data for the current hour.
		day 	Displays report data for the current day.
		week 	Displays report data for the current week.
		month 	Displays report data for the current month.
		quarter 	Displays report data for the current quarter.
		year 	Displays report data for the current year. */
		reportDescription.put("dateGranularity", "day");
		
		//6.Build the Ranked report
		fullRanked.put("reportDescription", reportDescription);
		fullRanked.put("validate", true);
		
		//7.Request the Ranked report
		String responseTrended = OMTR_REST_Custom.callMethod("Report.QueueTrended", JSONObject.fromObject(fullRanked).toString());
		System.out.println(responseTrended);
		
		//8.Extract reportID
		JSONObject jsonObjQueued = JSONObject.fromObject(responseTrended);
		Map<String, Object> reportID = new HashMap<String, Object>();
		reportID.put("reportID", jsonObjQueued.get("reportID"));
		
		//9.Check if report is fully processed
		JSONObject jsonObjReportStatus;
		do{
			String reportStatus = OMTR_REST_Custom.callMethod("Report.GetStatus", JSONObject.fromObject(reportID).toString());
			System.out.println(reportStatus);
			jsonObjReportStatus = JSONObject.fromObject(reportStatus);
			System.out.println(jsonObjReportStatus.get("status"));
		}while(!jsonObjReportStatus.get("status").equals("ready") && !jsonObjReportStatus.get("status").equals("done"));
		String report;
		
		//10.ONce report is ready request the report
		do{
			report = OMTR_REST_Custom.callMethod("Report.GetReport", JSONObject.fromObject(reportID).toString());				
			jsonObjReportStatus = JSONObject.fromObject(report);
			System.out.println(jsonObjReportStatus.get("status"));
			System.out.println("Final Report status: " + jsonObjReportStatus.get("status"));
		}while(!jsonObjReportStatus.get("status").equals("done"));
		
		//11.display final report
		System.out.println("Final Report: " + report);
		JSONObject jsonObj = JSONObject.fromObject(report);
		JSONObject reportDetails = JSONObject.fromObject(jsonObj.get("report"));
		System.out.println("Report Details : " +reportDetails);
		JSONArray reportSuiteDetails = JSONArray.fromObject(reportDetails.get("reportSuite"));
		System.out.println("Report Suite Details : " +reportSuiteDetails);
		
		String reportDateDetails = reportDetails.get("period").toString();
		System.out.println("Report Suite Details : " +reportDateDetails);
		
		JSONArray reportDataDetails = JSONArray.fromObject(reportDetails.get("data"));
		System.out.println("Report Suite Details : " +reportDataDetails);
		for(int i=0;i<reportDataDetails.size();i++){
			
			System.out.println( JSONObject.fromObject(reportDataDetails.get(i)).get("name"));
		}
		
	}
	
	/*
	 * This will return an overtime report
	 */
	private static void getOvertimeReport(int numMetrics, String[] metrics, String reportSuiteID, String startDate, String endDate) throws IOException{
		/*This will contain the REST request sent to Adobe servers*/
		Map<String, Object> fullRanked = new HashMap<String, Object>();
		
		/*A report description that specifies the desired Ranked report contents. */
		Map<String, Object> reportDescription = new HashMap<String, Object>();
		
		/*A list of the events to include in the report. A report must specify at least one metric */
		ArrayList<Map<String,Object>> metriclist = new ArrayList<Map<String,Object>>();
		
		//1.Set the report suite ID
		reportDescription.put("reportSuiteID", reportSuiteID); //put the right report suite ID
		
		//2.Set the date from and to
		reportDescription.put("dateFrom", startDate);
		reportDescription.put("dateTo", endDate);
		
		//3.Set metrics
		for(int i=0;i<numMetrics;i++){
			Map<String, Object> metric = new HashMap<String, Object>();
			metric.put("id", metrics[i]);
			metriclist.add(metric);
		}
		reportDescription.put("metrics", metriclist);
		
		
		//4.Set granularity of the report
		/*hour 	Displays report data for the current hour.
		day 	Displays report data for the current day.
		week 	Displays report data for the current week.
		month 	Displays report data for the current month.
		quarter 	Displays report data for the current quarter.
		year 	Displays report data for the current year. */
		reportDescription.put("dateGranularity", "day");
		
		//6.Build the Overtime report
		fullRanked.put("reportDescription", reportDescription);
		fullRanked.put("validate", true);
		
		//7.Request the Ranked report
		String responseOvertime = OMTR_REST_Custom.callMethod("Report.QueueOvertime", JSONObject.fromObject(fullRanked).toString());
		System.out.println(responseOvertime);
		
		//8.Extract reportID
		JSONObject jsonObjQueued = JSONObject.fromObject(responseOvertime);
		Map<String, Object> reportID = new HashMap<String, Object>();
		reportID.put("reportID", jsonObjQueued.get("reportID"));
		
		//9.Check if report is fully processed
		JSONObject jsonObjReportStatus;
		do{
			String reportStatus = OMTR_REST_Custom.callMethod("Report.GetStatus", JSONObject.fromObject(reportID).toString());
			System.out.println(reportStatus);
			jsonObjReportStatus = JSONObject.fromObject(reportStatus);
			System.out.println(jsonObjReportStatus.get("status"));
		}while(!jsonObjReportStatus.get("status").equals("ready") && !jsonObjReportStatus.get("status").equals("done"));
		String report;
		
		//10.ONce report is ready request the report
		do{
			report = OMTR_REST_Custom.callMethod("Report.GetReport", JSONObject.fromObject(reportID).toString());				
			jsonObjReportStatus = JSONObject.fromObject(report);
			System.out.println(jsonObjReportStatus.get("status"));
			System.out.println("Final Report status: " + jsonObjReportStatus.get("status"));
		}while(!jsonObjReportStatus.get("status").equals("done"));
		
		//11.display final report
		System.out.println("Final Report: " + report);
		JSONObject jsonObj = JSONObject.fromObject(report);
		JSONObject reportDetails = JSONObject.fromObject(jsonObj.get("report"));
		System.out.println("Report Details : " +reportDetails);
		JSONArray reportSuiteDetails = JSONArray.fromObject(reportDetails.get("reportSuite"));
		System.out.println("Report Suite Details : " +reportSuiteDetails);
		
		String reportDateDetails = reportDetails.get("period").toString();
		System.out.println("Report Suite Details : " +reportDateDetails);
		
		JSONArray reportDataDetails = JSONArray.fromObject(reportDetails.get("data"));
		System.out.println("Report Suite Details : " +reportDataDetails);
		for(int i=0;i<reportDataDetails.size();i++){
			
			System.out.println( JSONObject.fromObject(reportDataDetails.get(i)).get("name"));
		}
		
	}
	
	/*
	 * This will add an Internal URL filter 
	 */
	private static void AddInternalURLFilters(String[] filters,String[] reportSuites) throws IOException{
		Map<String, String[]> request = new HashMap<String, String[]>();
		request.put("filters",filters);
		request.put("rsid_list",reportSuites);
		String response = OMTR_REST_Custom.callMethod("ReportSuite.AddInternalURLFilters", JSONObject.fromObject(request).toString());
		System.out.println(response);
		
	}
	
	/*
	 * This will delete an Internal URL filter
	 */
	private static void DeleteInternalURLFilters(String[] filters,String[] reportSuites) throws IOException{
		Map<String, String[]> request = new HashMap<String, String[]>();
		request.put("filters",filters);
		request.put("rsid_list",reportSuites);
		String response = OMTR_REST_Custom.callMethod("ReportSuite.DeleteInternalURLFilters", JSONObject.fromObject(request).toString());
		System.out.println(response);
		
	}
	
	
	
}
