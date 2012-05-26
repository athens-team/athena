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
package net.rothlee.athens.analyzer.core;

import net.rothlee.athens.message.AthensRequest;

/**
 * @author Jung-Haeng Lee
 */
public class BlockFilter {

	private static final BlockFilter instance = new BlockFilter();
	
	public static BlockFilter getInstance() {
		return instance;
	}
	
	public boolean isBlocked(AthensRequest request) {
		return false;
	}
	
	public void addBlock(BlockInfo blockInfo) {
		
	}
	
	public void removeBlock(BlockInfo blockInfo) {
		
	}
}