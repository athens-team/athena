package com.eincs.athens.db.leveldb;

import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.WriteOptions;

public class DBWrapper implements DB {

	private final DB wrppaed;
	
	public DBWrapper(DB wrapped) {
		this.wrppaed = wrapped;
	}
	
	@Override
	public void close() {
		try {
			wrppaed.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] get(byte[] key) throws DBException {
		return wrppaed.get(key);
	}

	@Override
	public byte[] get(byte[] key, ReadOptions options) throws DBException {
		return wrppaed.get(key, options);
	}

	@Override
	public DBIterator iterator() {
		return wrppaed.iterator();
	}

	@Override
	public DBIterator iterator(ReadOptions options) {
		return wrppaed.iterator(options);
	}

	@Override
	public void put(byte[] key, byte[] value) throws DBException {
		wrppaed.put(key, value);
	}

	@Override
	public void delete(byte[] key) throws DBException {
		wrppaed.delete(key);	
	}

	@Override
	public void write(WriteBatch updates) throws DBException {
		wrppaed.write(updates);
	}

	@Override
	public WriteBatch createWriteBatch() {
		return wrppaed.createWriteBatch();
	}

	@Override
	public Snapshot put(byte[] key, byte[] value, WriteOptions options)
			throws DBException {
		return wrppaed.put(key, value, options);
	}

	@Override
	public Snapshot delete(byte[] key, WriteOptions options) throws DBException {
		return wrppaed.delete(key, options);
	}

	@Override
	public Snapshot write(WriteBatch updates, WriteOptions options)
			throws DBException {
		return wrppaed.write(updates, options);
	}

	@Override
	public Snapshot getSnapshot() {
		return wrppaed.getSnapshot();
	}

	@Override
	public long[] getApproximateSizes(Range... ranges) {
		return wrppaed.getApproximateSizes(ranges);
	}

	@Override
	public String getProperty(String name) {
		return wrppaed.getProperty(name);
	}

}
