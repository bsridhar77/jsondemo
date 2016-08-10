package com.demo.jsondemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger LOG = LoggerFactory.getLogger(TestClass.class);
	
	
	
	
	
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
	
	
	public String findAndUpdateFieldByKey(String jsonString, List keyList,String repValue){
		
		//LOG.debug("Entering");
		StringBuffer strBuf=new StringBuffer();
		
		String fieldNodeStr=null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Configuration config= Configuration.builder()
				    .jsonProvider(new JacksonJsonNodeJsonProvider())
				    .mappingProvider(new JacksonMappingProvider())
				    .build();

			fieldNodeStr=jsonString;
			for(Object str:keyList){
				String s=(String) str;
			
			JsonNode newJson=JsonPath.using(config).parse(fieldNodeStr).set("$."+s,repValue).json();
			
			fieldNodeStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newJson);
			
		
			}
		} catch (JsonProcessingException e) {
			LOG.error("Exception:",e);
		}
	
		return fieldNodeStr;
		
	}


public static String getValues(String key,String jsonString){
	String fieldNodeStr =null;
//	LOG.debug("Entering");
	
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
	//LOG.debug("Leaving.");
	return fieldNodeStr;
}


	public static void main(String arg[]){
		
		TestClass testObj=new TestClass();
		try {
		
			String str1 = readFile("Test_Json.json");
				
				System.out.println("***********START:Demo for Masking.");
				String maskString="?????";

				String[] fieldsToMask={"employeeId","requester[*].demographic.firstName"};
				List fieldsToMaskList=Arrays.asList(fieldsToMask);
				System.out.println("Masking..." + Arrays.toString(fieldsToMask) + " Using: maskString" + maskString);
				str1=testObj.findAndUpdateFieldByKey(str1,fieldsToMaskList,maskString);
		
				
				System.out.println("Masked..." + Arrays.toString(fieldsToMask) + " Using: maskString" + maskString);
			
				System.out.println("***********END:Demo for Masking.");
				
				
				System.out.println("***********START: Demo for Including Selected Fields...");
				


				String[] fieldsToInclude={"employeeId","requester[*].demographic.firstName","manager","administrator.isSuperUser","requester[*].demographic.lastName","salesId","administrator.userId"};
				List fieldsToIncludeList=Arrays.asList(fieldsToInclude);
				System.out.println("Including..." + Arrays.toString(fieldsToInclude) );
				
				System.out.println(testObj.includeFieldsFromPayload(fieldsToIncludeList,str1));
				
				System.out.println("Included..." + Arrays.toString(fieldsToInclude) );
				System.out.println("***********END: Demo for Including Selected Fields...");
				System.out.println("");
				
				
		}catch (IOException e) {
				LOG.error("IOException:",e);
		}catch (Exception e) {
				LOG.error("Exception:",e);
		}
		
	}
	
	
	
	

	public String removeTrailingCharacter(String str) {
	    if (str != null && str.length() > 0 && str.charAt(str.length()-1)==',') {
	      str = str.substring(0, str.length()-1);
	    }
	    return str;
	}
		
	public  String includeFieldsFromPayload(List fieldsToIncludeList, String str1) throws JsonProcessingException {
		StringBuffer strBuf=new StringBuffer();
		strBuf.append("{");
		for(Object str:fieldsToIncludeList){
			String s=(String) str;
			
			strBuf.append("\r")
				 .append('"')
				 .append(removeCharacters(s))
				 .append('"')
				 .append(":")
				 .append(getValues(s,str1))
				 .append(",");
			
		}
		String finalStr=strBuf.toString();
		finalStr=removeTrailingCharacter(finalStr) + "\r" +"}";
		return finalStr;
	}

	private String removeCharacters(String str){
		str = str.replace("[", "");
		str = str.replace("]", "");
		str = str.replace("*", "");
		return str;
	}
	

	private static String readFile(String filename)  throws IOException 
	{
		
		
		String thisLine=null;
		StringBuffer strBuf=new StringBuffer();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
		       TestClass.class.getResourceAsStream("/" +filename), "UTF-8"));
		
		while ((thisLine = br.readLine()) != null) {
            strBuf.append(thisLine);
         }    
		return strBuf.toString();
	}
}
