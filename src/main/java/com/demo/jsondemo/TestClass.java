package com.demo.jsondemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;




public class TestClass {
	
	private static final Logger LOG = Logger.getLogger(TestClass.class);
	
	public static final String FOLDER_PATH="D:\\jsondemo\\src\\main\\resources\\";	
	
	public static final String PATTERN_STRING="[.]";
	
	public static final Pattern pattern= Pattern.compile(PATTERN_STRING);
	
	public static final String NEWLINE="\r";
	
	public static final String OPEN_CURLY_BRACES="{";
	
	public static final String CLOSE_CURLY_BRACES="}";
	
	public static final String FORWARD_SLASH="/";
	
	public static final String COLON=":";
	
	public static final Character DOUBLE_QUOTES='"';
	
	public static final String COMMA=",";
	
	
	public String findAndUpdateFieldByKey(String key,JsonNode jsonNode,String repValue){
		
		LOG.debug("Entering");
		String fieldNodeStr =null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode fieldNode = jsonNode.at(key);
			((ObjectNode) fieldNode).put(key,repValue);
			fieldNodeStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fieldNode);
			
		} catch (IOException e) {
			LOG.error("IOException:",e);
		}catch (Exception e) {
			LOG.error("Exception:",e);
		}
		
		LOG.debug("Leaving.");
		return fieldNodeStr;
	}

private String getFieldByKey(String key,JsonNode jsonNode){
	
	LOG.debug("Entering");
	String fieldNodeStr =null;
	try {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode fieldNode = jsonNode.at(key);
		fieldNodeStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fieldNode);
		
	} catch (IOException e) {
		LOG.error("IOException:",e);
	}catch (Exception e) {
		LOG.error("Exception:",e);
	}
	
	LOG.debug("Leaving.");
	return fieldNodeStr;
}


public String findAndUpdateFieldsByConfig(String fields,String jsonString,String repText){
	LOG.debug("Entering");
	StringBuffer strBuf=new StringBuffer(255);
	ObjectMapper mapper = new ObjectMapper();
	JsonNode node;
	String retString=null;
	
	strBuf.append(OPEN_CURLY_BRACES).append(NEWLINE);
	
	List<String> fieldToIncludeListr=stringToList(fields);
	
	try {
		
		node = mapper.readTree(jsonString);
		for(String str:fieldToIncludeListr){
		       Matcher m = pattern.matcher(str); 
		       findAndUpdateFieldByKey(FORWARD_SLASH + m.replaceAll(FORWARD_SLASH),node,repText);
			}
		
	} catch (JsonProcessingException e) {
		LOG.error("JsonProcessingException:" ,e);
	} catch (IOException e) {
		LOG.error("IOException:" ,e);
	}catch (Exception e) {
		LOG.error("Exception:" ,e);
	}
	
	if(null!=strBuf){
		
	retString=strBuf.toString();
	
	if(null!=retString){
		//Remove trailing comma
		retString = retString.replaceAll(",$", "");
		//Append a New Line Character
		retString=retString +  NEWLINE + NEWLINE + CLOSE_CURLY_BRACES;
	   }
	}
	LOG.debug("Leaving");
	return retString;
}


public String includeFieldsByConfig(String fields,String jsonString){
		LOG.debug("Entering");
		StringBuffer strBuf=new StringBuffer(255);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		String retString=null;
		
		strBuf.append(OPEN_CURLY_BRACES).append(NEWLINE);
		
		List<String> fieldToIncludeListr=stringToList(fields);
		
		try {
			node = mapper.readTree(jsonString);
			
			//there should be a better approach than what I have done below... For now, I am doing like this...
			for(String str:fieldToIncludeListr){
			       Matcher m = pattern.matcher(str); 
			       strBuf.append(NEWLINE)
			       		 .append(DOUBLE_QUOTES)
			       		 .append(str)
			       		 .append(DOUBLE_QUOTES)
			       		 .append(COLON)
			       		 .append(getFieldByKey(FORWARD_SLASH + m.replaceAll(FORWARD_SLASH),node)).append(COMMA);
				}
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException:" ,e);
		} catch (IOException e) {
			LOG.error("IOException:" ,e);
		}catch (Exception e) {
			LOG.error("Exception:" ,e);
		}
		
		if(null!=strBuf){
			
		retString=strBuf.toString();
		
		if(null!=retString){
			//Remove trailing comma
			retString = retString.replaceAll(",$", "");
			//Append a New Line Character
			retString=retString +  NEWLINE + NEWLINE + CLOSE_CURLY_BRACES;
		   }
		}
		LOG.debug("Leaving");
		return retString;
}
	public static void main(String arg[]){
		
		TestClass testObj=new TestClass();
		try {
			
				//Include specific Fields in Json
				String str = readFile(FOLDER_PATH + "Test_Json.json");
				
				LOG.info("START: Testing Fields to Include while logging Payload");
				LOG.info("Original:" + str);
				String fieldstoInclude="manager.userId,administrator.isSuperUser,salesId,requester.id";
				
				System.out.println(testObj.includeFieldsByConfig(fieldstoInclude,str));
				
				
				//Mask Specific Fields in Json
				LOG.info("START: Testing Mask Fields in input");
				String fieldsToMask1="manager.userId";
				String maskString="?????";
				LOG.info("Field to be Masked:" + fieldsToMask1);
				
				testObj.findAndUpdateFieldsByConfig(fieldsToMask1,str,maskString);
				
				LOG.info("END: Testing Mask Fields in input");
				LOG.info("");
				
		}catch (IOException e) {
				LOG.error("IOException:",e);
		}catch (Exception e) {
				LOG.error("Exception:",e);
		}
		
	}
	
	
	private static List<String> stringToList(String delimitedString){
		List<String> newList=new ArrayList<String>();
		StringTokenizer strtokenizer=new StringTokenizer(delimitedString,COMMA);
		while(strtokenizer.hasMoreTokens()){
			newList.add(strtokenizer.nextToken());
		}
		return newList;
	}

		
	private static String readFile(String path)  throws IOException 
	{
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, "UTF-8");
	}
}
