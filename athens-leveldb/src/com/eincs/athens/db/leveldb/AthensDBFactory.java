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
package com.eincs.athens.db.leveldb;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.util.FileUtils;

/**
 * @author Jung-Haeng Lee
 */
public class AthensDBFactory {

	private final DBFactory factory;
	private final boolean reuse;
	
	public AthensDBFactory() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		this(false);
	}

	public AthensDBFactory(boolean reuse) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		ClassLoader classLoader = AthensDBFactory.class.getClassLoader();
		this.factory = (DBFactory) classLoader.loadClass(
				System.getProperty("leveldb.factory",
						"org.iq80.leveldb.impl.Iq80DBFactory")).newInstance();
		this.reuse = reuse;
	}

	public synchronized AthensDB open(String databaseDir) throws IOException {
		return AthensDB.open(this, databaseDir);
	}
	
	private DB openInternal(String databaseDir) throws IOException {
		if(!reuse) {
			FileUtils.deleteDirectoryContents(new File(databaseDir));
		}
		Options options = new Options();
		options.createIfMissing(true);
		return factory.open(new File(databaseDir), options);
	}

	public static class AthensDB extends DBWrapper {

		private static AthensDB open(AthensDBFactory dbFactory,
				String dbDirectory) throws IOException {
			return new AthensDB(dbFactory, dbDirectory);
		}

		private AthensDB(AthensDBFactory dbFactory, String dbDirectory)
				throws IOException {
			super(dbFactory.openInternal(dbDirectory));
		}

		public synchronized void closeQuietly() {
			close();
		}
	}
}
