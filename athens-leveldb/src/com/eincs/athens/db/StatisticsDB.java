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
package com.eincs.athens.db;

import org.iq80.leveldb.DBException;

import com.eincs.athens.db.data.Statistics;
import com.eincs.athens.db.data.StatisticsKey;

/**
 * @author Jung-Haeng Lee
 */
public interface StatisticsDB {

	public Statistics getStatistics(StatisticsKey key) throws DBException;

	public void setStatistics(StatisticsKey key, Statistics block)
			throws DBException;

	public void removeStatistics(StatisticsKey key) throws DBException;

	public void clear() throws DBException;
}