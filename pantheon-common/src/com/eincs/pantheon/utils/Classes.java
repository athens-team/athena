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
package com.eincs.pantheon.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author roth2520@gmail.com
 */
public class Classes {

	public static List<Field> getAllMemberFields(Class<?> clazz) {
		List<Field> result = Lists.newArrayList();
		List<Class<?>> classes = getGenealogyList(clazz);
		for(Class<?> c : classes) {
			List<Field> declaredFields = Lists.newArrayList(c.getDeclaredFields());
			for(Field f : declaredFields) {
				if(!Modifier.isStatic(f.getModifiers())) {
					result.add(f);
				}
			}
		}
		return result;
	}
	
	public static List<Class<?>> getGenealogyList(Class<?> clazz) {
		List<Class<?>> classes = Lists.newArrayList();
		while(clazz!=null) {
			classes.add(clazz);
			clazz = clazz.getSuperclass();
		}
		return classes;
	}
}
