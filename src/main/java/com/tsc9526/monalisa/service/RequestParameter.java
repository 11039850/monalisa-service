package com.tsc9526.monalisa.service;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.misc.MelpException;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class RequestParameter {
	/**
	 * Ignore request parameters with prefix: _
	 */
	public static String PREFIX   = "_";
	
	/**
	 * xml/json, default is json
	 */
	public static String FORMAT   = "_format";
	
	/**
	 * jsonp callback function name
	 */
	public static String CALLBACK = "_callback";  
 	 
	/**
	 * Replace HTTP method: GET,POST,HEAD,PUT,DELETE ...
	 */
	public static String METHOD   = "method";
  
	
	/**
	 * Include/Exclude columns
	 */
	public static String COLUMN   = "column";
	
	/**
	 * Page 
	 */
	public static String PAGING    = "paging";
	
	/**
	 * Page limit
	 */
	public static String LIMIT    = "limit";
	
	/**
	 * Page offset
	 */
	public static String OFFSET   = "offset";
	
	/**
	 * Order by 
	 */
	public static String ORDER    = "order";
 	
  
	private static Set<String> fields=new HashSet<String>();
	 
	
	public static boolean contains(String name){
		if(fields.size()==0){
			synchronized (fields) {
				if(fields.size()==0){
					try{
						List<Field> fs=MelpClass.fetchFields(RequestParameter.class);
						for(Field f:fs){
							if(f.getType()==String.class){
								String v=(String)f.get(null);
								fields.add(v.toLowerCase());
							}
						}
					}catch(Exception e){
						MelpException.throwRuntimeException(e);
					}
				}
			}
		}
		
		return fields.contains(name.toLowerCase());
	}
}
