package com.demo.jsondemo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;



public class RequestLoggingFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
	  @Value("${mask.field.names}")
	  private String maskFieldNames;
	  
	  @Value("${exclude.field.names}")
	  private String excludeFieldNames;
	  
	  @Value("${mask.string}")
	  private String maskString;
	  
	  @Value("${exclude.fields}")
	  private String excludeFields;
	  
	  @Value("${mask.fields}")
	  private String maskFields;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException{
		
		HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		LOGGER.info("RequestInterceptor: REQUEST Intercepted for URI: "
				+ httpServletRequest.getRequestURI());
		RequestWrapper requestWrapper;
		
		try {
			requestWrapper = new RequestWrapper(httpServletRequest);
			
			String payload=requestWrapper.getPayload();
			LOGGER.info("Prop Value: mask.field.names:" + maskFieldNames);
			LOGGER.info("Prop Value: exclude.field.names:" + excludeFieldNames);
			LOGGER.info("Prop Value: maskString:" + maskString);
			LOGGER.info("Prop Value: maskFields:" + maskFields);
			LOGGER.info("Prop Value: excludeFields:" + excludeFields);
			
			if(null!=maskFields && maskFields.equalsIgnoreCase("true") && null!=maskFieldNames && maskFieldNames.trim().length()>0){
				payload=new ServiceInputFilter().maskInput(payload,maskFieldNames,maskString);
			}
			
			if(null!=excludeFields && excludeFields.equalsIgnoreCase("true") && null!=excludeFieldNames && excludeFieldNames.trim().length()>0){
				payload=new ServiceInputFilter().excludeInput(payload,excludeFieldNames);
			}
		
			
			LOGGER.info("Request Payload:" + payload);
			chain.doFilter(requestWrapper, response);
		} catch (Exception e) {
			LOGGER.error("Exception:" ,e);
		}
	}

	public void destroy() {
	}
	
	
	
	}

