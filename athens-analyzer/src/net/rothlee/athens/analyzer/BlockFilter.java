package net.rothlee.athens.analyzer;

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
