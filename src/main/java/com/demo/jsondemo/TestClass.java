package com.demo.jsondemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;




public class TestClass {
	
	private static final Logger LOG = Logger.getLogger(TestClass.class);
	
	public static final String FOLDER_PATH="D:\\jsondemo\\src\\main\\resources\\";	
	
	
	
	TestClass(){
		
		Configuration.setDefaults(new Configuration.Defaults() {

		    private final JsonProvider jsonProvider = new JacksonJsonProvider();
		    private final MappingProvider mappingProvider = new JacksonMappingProvider();

		    public JsonProvider jsonProvider() {
		        return jsonProvider;
		    }

		    public MappingProvider mappingProvider() {
		        return mappingProvider;
		    }

		    public Set<Option> options() {
		        return EnumSet.noneOf(Option.class);
		    }
		});

		
	}
	
	
	public String findAndUpdateFieldByKey(String jsonString, String key,String repValue){
		
		LOG.debug("Entering");
		
		String fieldNodeStr=null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Configuration config= Configuration.builder()
				    .jsonProvider(new JacksonJsonNodeJsonProvider())
				    .mappingProvider(new JacksonMappingProvider())
				    .build();

			JsonNode newJson=JsonPath.using(config).parse(jsonString).set("$." +key,repValue).json();
			fieldNodeStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newJson);
		} catch (JsonProcessingException e) {
			LOG.error("Exception:",e);
		}
		LOG.debug("Leaving.");
		return fieldNodeStr;
	}


public String getValues(String key,String jsonString){
	String fieldNodeStr =null;
	LOG.debug("Entering");
	
	try {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode testNode = JsonPath.parse(jsonString)
                .read("$." + key, JsonNode.class);
		fieldNodeStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testNode);
		
	} catch (IOException e) {
		LOG.error("IOException:",e);
	}catch (Exception e) {
		LOG.error("Exception:",e);
	}
	LOG.debug("Leaving.");
	return fieldNodeStr;
}


/*
 * 
 * Prefix all fields with $.(DONE)
 * If array field need to suffix [*] (Can be first field are anywhere in-between the hierarchy of fields
 * 
 */

	public static void main(String arg[]){
		
		TestClass testObj=new TestClass();
		try {
		
				System.out.println("***********START: Demo for Including Selected Fields...");
				
				String str = readFile(FOLDER_PATH + "Test_Json.json");
				
				System.out.println("Original...");
				System.out.println(str);
				
				
				
				//TODO: Convert requester to $.requester[*] if requester is an array field
				System.out.println("Including Requester...");
				System.out.println(testObj.getValues("requester[*]",str));
				
				//TODO: Convert manager to $.manager
				System.out.println("Including Manager...");
				System.out.println(testObj.getValues("manager",str));
				
				
				//TODO: Convert requester.demographic.firstName to $.requester[*].demographic.firstName
				System.out.println("Including requester.demographic.firstName...");
				System.out.println(testObj.getValues("requester[*].demographic.firstName",str));
				
				System.out.println("***********END: Demo for Including Selected Fields...");
				System.out.println("");
		
				System.out.println("");
				System.out.println("***********START: Demo for Masking...");
			
				System.out.println("Original...");
				System.out.println(str);
				
				
				//TODO: Convert administrator.userId to $.administrator.userId
				String fieldsToMask1="administrator.userId";
				String maskString="?????";
				System.out.println("Masking...administrator.userId as :" + maskString);
				str=testObj.findAndUpdateFieldByKey(str,fieldsToMask1,maskString);
				
				System.out.println("Masked...administrator.userId as :" + maskString);
				System.out.println(str);
				
				
				//TODO: Convert requester.demographic.firstName to $.requester[*].demographic.firstName
				String firstNameField="requester[*].demographic.firstName";
				String maskString2="*****";
				System.out.println("Masking...demographic.firstName as :" + maskString2);
				System.out.println(testObj.findAndUpdateFieldByKey(str,firstNameField,maskString2));
				str=testObj.findAndUpdateFieldByKey(str,firstNameField,maskString2);
				System.out.println("Masked...demographic.firstName as :" + maskString2);
				System.out.println(str);
				
				
				System.out.println("***********END:Demo for Masking.");
				System.out.println("");
				
				
		}catch (IOException e) {
				LOG.error("IOException:",e);
		}catch (Exception e) {
				LOG.error("Exception:",e);
		}
		
	}
	
	
	

		
	private static String readFile(String path)  throws IOException 
	{
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, "UTF-8");
	}
}
