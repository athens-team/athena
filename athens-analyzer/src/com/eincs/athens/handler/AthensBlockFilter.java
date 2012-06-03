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
package com.eincs.athens.handler;

import java.io.IOException;

import org.apache.hadoop.util.bloom.CountingBloomFilter;
import org.apache.hadoop.util.bloom.Key;
import org.apache.hadoop.util.hash.Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eincs.athens.db.BlockDB;
import com.eincs.athens.db.data.Block;
import com.eincs.athens.db.data.BlockKey;
import com.eincs.athens.message.AnalyzeResultType;
import com.eincs.athens.message.AthensReport;
import com.eincs.pantheon.AddressProviders;
import com.eincs.pantheon.message.PanteonRequest;
import com.google.inject.Inject;

/**
 * @author Jung-Haeng Lee
 */
public class AthensBlockFilter {

	private static final Logger logger = LoggerFactory
			.getLogger(AthensBlockFilter.class);
	
	private static final int BLOOM_FILTER_VECTOR_SIZE = 32*16;
	private static final int BLOOM_FILTER_NUMBER_HASH = 1;
	
	private final BlockDB blockDB;
	private final CountingBloomFilter bloomFilter;
	
	@Inject
	public AthensBlockFilter(BlockDB blockDB) {
		this.blockDB = blockDB;
		this.bloomFilter = new CountingBloomFilter(BLOOM_FILTER_VECTOR_SIZE,
				BLOOM_FILTER_NUMBER_HASH, Hash.MURMUR_HASH);
	}
	
	public boolean isBlocked(PanteonRequest request) throws IOException {
		
		byte[] address = AddressProviders.getRemoteOrOriginAddress(request)
				.getAddress();
		byte[] userId = (byte[]) request.getTags().get(
				AthensNames.TAG_USER_NAME);

		
		if (address != null) {
			BlockKey blockKey = BlockKey.createKeyByAddress(address,
					request.getMethod().getName(), request.getPath());
			if(isBlocked(blockKey)) {
				return true;
			}
		}

		if (userId != null) {
			BlockKey blockKey = BlockKey.createKeyByUserId(address, request
					.getMethod().getName(), request.getPath());
			if(isBlocked(blockKey)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isBlocked(BlockKey blockKey) throws IOException {
		Key key = blockKey.getBloomFilterKey();
		
		synchronized (this) {
			if (bloomFilter.membershipTest(key)) {
				Block block = blockDB.getBlock(blockKey);
				if(block!=null) {
					long current = System.currentTimeMillis();
					if(block.getExpiry() > current) {
						return true;
					} else {
						blockDB.removeBlock(blockKey);
						bloomFilter.delete(key);
					}
				}
			}	
		}
		return false;
	}
	
	public void apply(AthensReport report) throws IOException {
		BlockKey blockKey = BlockKey.createKey(report.getTargetKey());
		Block newBlock = Block.create(report.getResult());
		Key bloomKey = blockKey.getBloomFilterKey();
		
		synchronized (this) {
			Block oldBlock = blockDB.getBlock(blockKey);
			if (report.getResult().getType() == AnalyzeResultType.PANALTY) {
				if (oldBlock == null) {
					blockDB.setBlock(blockKey, newBlock);
					bloomFilter.add(bloomKey);

				} else {
					blockDB.setBlock(blockKey, newBlock);
				}
			} else if(report.getResult().getType() == AnalyzeResultType.RELEASE) {
				if (oldBlock != null) {
					blockDB.removeBlock(blockKey);
					bloomFilter.delete(bloomKey);
				}
			}
		}
	}
}
