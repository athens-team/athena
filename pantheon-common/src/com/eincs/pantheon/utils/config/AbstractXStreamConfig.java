/*
 * Copyright 2012 Athens Team
 * 
 * This file to you under the Apache License, version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eincs.pantheon.utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.ISO8601DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author roth2520@gmail.com
 */
public abstract class AbstractXStreamConfig {

	private final String fileName;
	private final XStream xStream;

	public AbstractXStreamConfig(String fileName) {
		this.fileName = fileName;
		this.xStream = new XStream(new DomDriver("UTF-8"));
		this.xStream.registerConverter(new ISO8601DateConverter());
	}

	public String getFileName() {
		return fileName;
	}

	public void save() {
		try {
			xStream.toXML(this, new FileOutputStream(new File(fileName)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void load() {
		try {
			xStream.fromXML(new FileInputStream(fileName), this);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

}
