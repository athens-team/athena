/*
 * Copyright 2012 Athens Team
 *
 * This file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.rothlee.athens.message;

import java.util.Map;


/**
 * @author roth2520@gmail.com
 */
public class AthensContext {
	
	private static final ThreadLocal<AthensContext> REQUEST_CONTEXT = new ThreadLocal<AthensContext>();
	
	public static AthensContext getCurrent(){
		return REQUEST_CONTEXT.get();
	}
	
	static void setCurrnet(AthensContext requestContent){
		REQUEST_CONTEXT.set(requestContent);
	}
	
	static void clearCurrent(){
		REQUEST_CONTEXT.remove();
	}
	
	public static AthensContext create(AthensRequest request){
		AthensContext context = new AthensContext();
		return context;
	}
	
	private Map<String, Object> values; 
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name){
	  return (T)values.get(name);
	}
	
	public void setValue(String name, Object value){
		values.put(name, value);
	}
	
	public boolean contains(String name) {
		return values.containsKey(name);
	}
}
