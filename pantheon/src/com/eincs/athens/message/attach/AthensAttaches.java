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
package com.eincs.athens.message.attach;

import java.io.File;
import java.io.IOException;
import java.util.List;


import org.jboss.netty.handler.codec.http.FileUpload;

import com.eincs.athens.message.attach.AthensAttaches.AthensAttach;
import com.eincs.athens.utils.collections.ListWrapper;

/**
 * @author roth2520@gmail.com
 */
public class AthensAttaches extends ListWrapper<AthensAttach> {

	public static AthensAttaches create(List<FileUpload> uploads) throws IOException {
		AthensAttaches result = new AthensAttaches();
		for(FileUpload upload : uploads) {
			result.add(AthensAttach.create(upload));
		}
		return result;
	}
	
	private AthensAttaches() { super(); }
	
	public static class AthensAttach {

		public static AthensAttach create(FileUpload upload) throws IOException {
			AthensAttach result = new AthensAttach();
			result.setFileName(upload.getFilename());
			result.setFile(upload.getFile());
			return result;
		}

		private String fileName;
		private File file;

		public String getFileName() {
			return fileName;
		}

		void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public File getFile() {
			return file;
		}

		void setFile(File file) {
			this.file = file;
		}
	}
}
