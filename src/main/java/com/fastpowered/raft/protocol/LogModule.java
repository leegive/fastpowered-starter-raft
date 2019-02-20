package com.fastpowered.raft.protocol;

import com.alibaba.fastjson.JSON;
import com.fastpowered.raft.protocol.dto.LogEntry;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class LogModule {

    static {
        RocksDB.loadLibrary();
    }

    private String logsPath;

    private RocksDB rocksDB;

    private final static byte[] LAST_INDEX_KEY = "LAST_INDEX_KEY".getBytes();

    private ReentrantLock lock = new ReentrantLock();

    public LogModule(String logsPath) {
        this.logsPath = logsPath;
        this.init();
    }

    private void init() {
        Options options = new Options();
        options.setCreateIfMissing(true);
        File file = new File(logsPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            rocksDB = RocksDB.open(options, logsPath);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    public void write(LogEntry logEntry) {
        try {
            lock.tryLock(3000, MILLISECONDS);
            logEntry.setIndex(getLastIndex() + 1);
            rocksDB.put(convert(logEntry.getIndex()), JSON.toJSONBytes(logEntry));
            updateLastIndex(logEntry.getIndex());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RocksDBException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public LogEntry read(Long index) {
        byte[] result = null;
        try {
            result = rocksDB.get(convert(index));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return result != null ? JSON.parseObject(result, LogEntry.class) : null;
    }

    public void remove(Long startIndex) {
        try {
            lock.tryLock(3000, MILLISECONDS);
            for (Long i = startIndex; i <= getLastIndex(); i++) {
                rocksDB.delete(convert(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RocksDBException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public LogEntry getLast() {
        byte[] result = null;
        try {
            result = rocksDB.get(convert(getLastIndex()));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return result != null ? JSON.parseObject(result, LogEntry.class) : null;
    }

    private void updateLastIndex(Long index) {
        try {
            rocksDB.put(LAST_INDEX_KEY, convert(index));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    public Long getLastIndex() {
        byte[] lastIndex = null;
        try {
            lastIndex = rocksDB.get(LAST_INDEX_KEY);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        if (lastIndex == null) {
            lastIndex = "-1".getBytes();
        }
        return Long.valueOf(new String(lastIndex));
    }

    private byte[] convert(Long key) {
        return key.toString().getBytes();
    }

}
