package com.demo.jsondemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;




public class ServiceInputFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInputFilter.class);
	 public static void change(JsonNode parent, String fieldName, String newValue) {
	        if (parent.has(fieldName)) {
	            ((ObjectNode) parent).put(fieldName, newValue);
	        }

	        for (JsonNode child : parent) {
	            change(child, fieldName, newValue);
	        }
	    }
	 public static void remove(JsonNode parent, String fieldName) {
	        if (parent.has(fieldName)) {
	            ((ObjectNode) parent).remove(fieldName);
	        }

	        for (JsonNode child : parent) {
	        	remove(child, fieldName);
	        }
	    }
	 
	 
	 static String readFile(String path) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, "UTF-8");
			}
	public static void main(String arg[]){

		
		String str;
		try {
			/*LOGGER.info("START: Testing Mask Fields in input");
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json1.json");
			LOGGER.info("Original:" + str);
			String fieldsToMask1="streetAddress,city";
			String maskString="?????";
			LOGGER.info("Field to be Masked:" + fieldsToMask1);
			LOGGER.info("Masked:" + new ServiceInputFilter().maskInput(str,fieldsToMask1,maskString));
			*/
			
			LOGGER.info("");
			LOGGER.info("START : Testing Exclude Fields in input");
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json1.json");
			String excludeFields1="streetAddress,city";
			LOGGER.info("Original:" + str);
			LOGGER.info("Field to be included:" + excludeFields1);
			LOGGER.info("Included:" +new ServiceInputFilter().includeFields(str,excludeFields1));
			/*
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json2.json");
			LOGGER.info("Original:" + str);
			String[] fieldsToMask2={"streetAddress"};
			LOGGER.info("Field to be Masked:" + Arrays.toString(fieldsToMask2));
			LOGGER.info("Masked:" + new ServiceInputFilter().maskInput(str,fieldsToMask2,maskString));
			
			
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json3.json");
			LOGGER.info("Original:" + str);
			String[] fieldsToMask3={"streetAddress"};
			LOGGER.info("Field to be Masked:" + Arrays.toString(fieldsToMask3));
			LOGGER.info("Masked:" + new ServiceInputFilter().maskInput(str,fieldsToMask3,maskString));
			
			
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json4.json");
			LOGGER.info("Original:" + str);
			String[] fieldsToMask4={"streetAddress"};
			LOGGER.info("Field to be Masked:" + Arrays.toString(fieldsToMask4));
			LOGGER.info("Masked:" + new ServiceInputFilter().maskInput(str,fieldsToMask4,maskString));
			
			LOGGER.info("END: Testing Mask Fields in input");
			
			
			//Testing Exclude Fields
			LOGGER.info("");
			LOGGER.info("START : Testing Exclude Fields in input");
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json1.json");
			String[] excludeFields1={"streetAddress"};
			LOGGER.info("Original:" + str);
			LOGGER.info("Field to be excluded:" + Arrays.toString(excludeFields1));
			LOGGER.info("Excluded:" +new ServiceInputFilter().excludeInput(str,excludeFields1));
			
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json2.json");
			String[] excludeFields2={"streetAddress"};
			LOGGER.info("Original:" + str);
			LOGGER.info("Field to be excluded:" + Arrays.toString(excludeFields2));
			LOGGER.info("Excluded:" +new ServiceInputFilter().excludeInput(str,excludeFields2));
			
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json3.json");
			String[] excludeFields3={"streetAddress"};
			LOGGER.info("Original:" + str);
			LOGGER.info("Field to be excluded:" + Arrays.toString(excludeFields3));
			LOGGER.info("Excluded:" +new ServiceInputFilter().excludeInput(str,excludeFields3));
			
			str = readFile("F:\\hello\\jsondemo\\src\\main\\resources\\json4.json");
			String[] excludeFields4={"streetAddress"};
			LOGGER.info("Original:" + str);
			LOGGER.info("Field to be excluded:" + Arrays.toString(excludeFields4));
			LOGGER.info("Excluded:" +new ServiceInputFilter().excludeInput(str,excludeFields4));
			LOGGER.info("END: Testing Exclude Fields in input");*/
			
			
		} catch (IOException e) {
			LOGGER.error("Exception:",e);
		}
		
	}
	
	
	public  String includeFields(String requestString,String fieldToMask) {
		 ObjectMapper mapper = new ObjectMapper();
		 String result=null;
		 String[] fieldToExcludeArr=stringToArray(fieldToMask);
	        JsonNode tree=null;
			try {
				tree = mapper.readTree(requestString);
				if(null!=fieldToExcludeArr && fieldToExcludeArr.length>0){
					for(String field:fieldToExcludeArr){
						LOGGER.info("include field name is: " + field);
						if(null!=tree && null!=tree.get(field)){
						LOGGER.info("include: field:" + field + "::" +  tree.get(field).asText());
						}
					}
				}
				
				
			} catch (IOException e) {
				LOGGER.error("Exception:" ,e);
			}
			try {
				result=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
			} catch (JsonProcessingException e) {
				LOGGER.error("Exception:" ,e);
			}
			 return result;
	}
	public  String excludeInput(String requestString,String fieldToMask) {
		 ObjectMapper mapper = new ObjectMapper();
		 String result=null;
		 String[] fieldToExcludeArr=stringToArray(fieldToMask);
	        JsonNode tree=null;
			try {
				tree = mapper.readTree(requestString);
				if(null!=fieldToExcludeArr && fieldToExcludeArr.length>0){
					for(String field:fieldToExcludeArr){
						remove(tree, field);
					}
				}
				
			} catch (IOException e) {
				LOGGER.error("Exception:" ,e);
			}
			try {
				result=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
			} catch (JsonProcessingException e) {
				LOGGER.error("Exception:" ,e);
			}
			 return result;
	}
	
	private String[] stringToArray(String delimitedString){
		String[] strArr=new String[2];
		int i=0;
		for (String retval: delimitedString.split(",")){
	        strArr[i]=retval;
	        i=i+1;
	     }
		return strArr;
	}
	
	public  String maskInput(String requestString,String fieldToMask,String maskString) {
		 ObjectMapper mapper = new ObjectMapper();
		 String result=null;
		 String[] fieldToMaskArr=stringToArray(fieldToMask);
	        JsonNode tree=null;
			try {
				tree = mapper.readTree(requestString);
				if(null!=fieldToMaskArr && fieldToMaskArr.length>0){
					for(String field:fieldToMaskArr){
						change(tree, field, maskString);
					}
				}
				
			} catch (IOException e) {
				LOGGER.error("Exception:" ,e);
			}
			try {
				result=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
			} catch (JsonProcessingException e) {
				LOGGER.error("Exception:" ,e);
			}
			 return result;
	}
}
