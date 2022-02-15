package com.wmf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Class:  MockAPIUtil
 * Scope:  MOCKAPI Utility Class
 * ------------------------------------------------------------
 * Date         Resource  Description
 * ------------------------------------------------------------
 * 12/02/2021   MJOJG     INIT...
 * ------------------------------------------------------------
 */
public class MockAPIUtil {
	
	private static final Logger log = LogManager.getLogger(MockAPIUtil.class);
	
	/**
	 * Extract path from a controller mapping. /controllerUrl/** => return matched **
	 * @param request incoming request.
	 * @return extracted path
	 */
	public static String extractPath(final HttpServletRequest request){
		return (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	}
	
	public static String extractHTTPMethod(final HttpServletRequest request){
		return (String) request.getMethod();
	}
	
	public static JSONObject extractHTTPRequestBody(final HttpServletRequest request) {
		JSONObject requestBody = null;
		StringBuffer requestStringBuffer = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				requestStringBuffer.append(line);

			requestBody = HTTP.toJSONObject(requestStringBuffer.toString());
		} catch (JSONException e) {
			log.info(MockAPIUtil.class.getCanonicalName()
					+ ".extractHTTPRequestBody(): JSONException Thrown...");
		} catch (IOException e) {
			log.info(MockAPIUtil.class.getCanonicalName()
					+ ".extractHTTPRequestBody(): IOException Thrown...");
		}

		return requestBody;
	}
	
	public static String extractPathScope(final HttpServletRequest request){
		String pathScope = null;
		String mockAPIRoute = null;
		
		mockAPIRoute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		
		if(!mockAPIRoute.isEmpty()) {
			String[] pathScope_ARRY = mockAPIRoute.split("/");
			
			if(pathScope_ARRY != null && pathScope_ARRY.length > 0) {
				pathScope = pathScope_ARRY[pathScope_ARRY.length-1];
			}
		}
		
		return pathScope;
	}
	
	/**
	 * Extract path from a controller mapping. /controllerUrl/** => return matched **
	 * @param request incoming request.
	 * @return extracted path
	 */
	public static String extractPathFromPattern(final HttpServletRequest request){
	    String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	    String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

	    AntPathMatcher apm = new AntPathMatcher();
	    String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

	    return finalPath;
	}
	
	public static String convertFirstCharUpper(String argument) {
		return String.valueOf(argument).substring(0, 1).toUpperCase() + argument.substring(1);
	}

	public static String convertToLowerCase(String argument) {
		return argument.toLowerCase();
	}

	public static String currentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//Getting current date
		Calendar cal = Calendar.getInstance();

		//Displaying current date in the desired format
		//System.out.println("Current Date: "+sdf.format(cal.getTime()));

		//Date after adding the days to the current date
		String newDate = sdf.format(cal.getTime());  

		//Displaying the new Date after addition of Days to current date
		//System.out.println("Date after Addition: "+newDate);

		return newDate;
	}

	public static String currentDate_yyyyMMddHHmmss() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		//Getting current date
		Calendar cal = Calendar.getInstance();

		//Displaying current date in the desired format
		//System.out.println("Current Date: "+sdf.format(cal.getTime()));

		//Date after adding the days to the current date
		String newDate = sdf.format(cal.getTime());  

		//Displaying the new Date after addition of Days to current date
		//System.out.println("Date after Addition: "+newDate);

		return newDate;
	}

	public static String currentDate_Plus10() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//Getting current date
		Calendar cal = Calendar.getInstance();

		//Displaying current date in the desired format
		//System.out.println("Current Date: "+sdf.format(cal.getTime()));

		//Number of Days to add
	    cal.add(Calendar.DAY_OF_MONTH, 10);  

		//Date after adding the days to the current date
		String newDate = sdf.format(cal.getTime());  

		//Displaying the new Date after addition of Days to current date
		//System.out.println("Date after Addition: "+newDate);

		return newDate;
	}

	public static String convertDateFormat(String dbo, String parserPattern, String formatterPattern) {
		String retVal = "";

		SimpleDateFormat simpleDateParser = new SimpleDateFormat(parserPattern);
		SimpleDateFormat simpleDateFormater = new SimpleDateFormat(formatterPattern);

		try {
			retVal = simpleDateFormater.format(simpleDateParser.parse(dbo));
		} catch (ParseException e) {
			// unable to parse the date format
		}

		return retVal;
	}

	public static int calculateAge(String dateOfBirth) {
		int clientAge = 0;

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		if (dateOfBirth != null || !dateOfBirth.isEmpty()) {
			try {
				Date dateOfBirthDate = format1.parse(dateOfBirth);
				dateOfBirth = format1.format(dateOfBirthDate);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				formatter = formatter.withLocale( Locale.US );
				LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
				LocalDate currentDate = LocalDate.now();
				clientAge = Period.between(birthDate, currentDate).getYears();

				//log.info("The client's age has been calculated: " + clientAge);
			} catch (ParseException e) {
				//log.info("Unable to Calculate Policy Person's Age - Cannot Parse Birthdate...");
			}
		}

		return clientAge;
	}

	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static int convertToInteger(String valueToBeConverted) {
		int retVal = 0;
		
		if(valueToBeConverted != null) {
			String valueToBeConverted_Trimmed = valueToBeConverted.trim();
			
			if(isNumeric(valueToBeConverted_Trimmed)) {
				if(valueToBeConverted_Trimmed.contains(".")) {
					Double valueToBeConverted_Double = Double.valueOf(valueToBeConverted_Trimmed);
					retVal = (int) Math.round(valueToBeConverted_Double);
				} else {
					retVal = Integer.valueOf(valueToBeConverted_Trimmed);
				}
			}
		}	
		
		return retVal;
	}
	
	public static double convertToDouble(String valueToBeConverted) {
		double retVal = 0;
		
		if(valueToBeConverted != null) {
			String valueToBeConverted_Trimmed = valueToBeConverted.trim();
			
			if(isNumeric(valueToBeConverted_Trimmed)) {
				if(valueToBeConverted_Trimmed.contains(".")) {
					retVal = Double.valueOf(valueToBeConverted_Trimmed);
				} else {
					int valueIntegertype = convertToInteger(valueToBeConverted_Trimmed);
					retVal = Double.valueOf(valueIntegertype);
				}
			}
		}	
		
		return retVal;
	}
	
	//JSON PARSING - Off Possible Use - Yet to be Determined
//    public static List<FieldValue> parseAwdFieldValues(JSONObject instanceObjectJSON) throws JSONException {
//        JSONObject fieldValuesObjectJSON = null;
//        List<FieldValue> fieldValue = new ArrayList<FieldValue>();
//        JSONArray fieldValueJsonArray = null;
//        JSONObject fieldValueObject = null;
//        JSONObject fieldValueLinkObjectJSON = null;
//        Object whatsMyLinkTypeBob = null;
//        Object whatsMyObjectTypeBob = null;
//        if(instanceObjectJSON.has("fieldValues")) {
//            fieldValuesObjectJSON = (JSONObject) instanceObjectJSON.get("fieldValues");
//            if (fieldValuesObjectJSON.has("fieldValue")) {
//                whatsMyObjectTypeBob = fieldValuesObjectJSON.get("fieldValue");
//                if (whatsMyObjectTypeBob instanceof JSONObject) {
//                    fieldValueObject = (JSONObject) fieldValuesObjectJSON.get("fieldValue");
//                    whatsMyLinkTypeBob = fieldValueObject.get("link");
//                    if (whatsMyLinkTypeBob instanceof JSONObject) {
//                        fieldValueLinkObjectJSON = (JSONObject) fieldValueObject.get("link");
//                        AddFieldValue(fieldValue, fieldValueObject, fieldValueLinkObjectJSON);
//                    } else {
//                        if (whatsMyLinkTypeBob instanceof JSONArray) {
//                            JSONArray fieldValueLinkJsonArray = (JSONArray) fieldValueObject.get("link");
//                            for (int n = 0; n < fieldValueLinkJsonArray.length(); n++) {
//                                fieldValueLinkObjectJSON = (JSONObject) fieldValueLinkJsonArray.get(n);
//                                AddFieldValue(fieldValue, fieldValueObject, fieldValueLinkObjectJSON);
//                            }
//                        }
//                    }
//                } else {
//                    if (whatsMyObjectTypeBob instanceof JSONArray) {
//                        fieldValueJsonArray = (JSONArray) fieldValuesObjectJSON.get("fieldValue");
//                        for (int i = 0; i < fieldValueJsonArray.length(); i++) {
//                            fieldValueObject = (JSONObject) fieldValueJsonArray.get(i);
//                            whatsMyLinkTypeBob = fieldValueObject.get("link");
//                            if (whatsMyLinkTypeBob instanceof JSONObject) {
//                                fieldValueLinkObjectJSON = (JSONObject) fieldValueObject.get("link");
//                                AddFieldValue(fieldValue, fieldValueObject, fieldValueLinkObjectJSON);
//                            } else {
//                                if (whatsMyLinkTypeBob instanceof JSONArray) {
//                                    JSONArray fieldValueLinkJsonArray = (JSONArray) fieldValueObject.get("link");
//                                    for (int n = 0; n < fieldValueLinkJsonArray.length(); n++) {
//                                        fieldValueLinkObjectJSON = (JSONObject) fieldValueLinkJsonArray.get(n);
//                                        AddFieldValue(fieldValue, fieldValueObject, fieldValueLinkObjectJSON);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return fieldValue;
//    }	
}
