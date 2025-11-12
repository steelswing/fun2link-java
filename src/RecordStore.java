/*
Ну вы же понимаете, что код здесь только мой?
Well, you do understand that the code here is only mine?
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 

/**
 * File: RecordStore.java
 * Created on 2025 Nov 12, 15:19:41
 *
 * @author LWJGL2
 */
public class RecordStore {
    
    private final String name;
    private final File file;
    private final List<byte[]> records = new ArrayList<>();

    private RecordStore(String name) throws IOException {
        this.name = name;
        this.file = new File(name + ".dat");
        if (file.exists()) {
            load();
        }
    }

    public static RecordStore openRecordStore(String name, boolean createIfNecessary) throws IOException {
        File file = new File(name + ".dat");
        if (!file.exists() && !createIfNecessary) {
            throw new FileNotFoundException("RecordStore " + name + " not found");
        }
        return new RecordStore(name);
    }

    public int addRecord(byte[] data, int offset, int numBytes) throws IOException {
        byte[] rec = Arrays.copyOfRange(data, offset, offset + numBytes);
        records.add(rec);
        save();
        return records.size(); // возвращаем id записи (1-based)
    }

    public void setRecord(int recordId, byte[] data, int offset, int numBytes) throws IOException {
        byte[] rec = Arrays.copyOfRange(data, offset, offset + numBytes);
        while (records.size() < recordId) {
            records.add(new byte[0]);
        }
        records.set(recordId - 1, rec);
        save();
    }

    public byte[] getRecord(int recordId) {
        if (recordId <= 0 || recordId > records.size()) return null;
        return records.get(recordId - 1);
    }

    public int getNumRecords() {
        return records.size();
    }

    public void closeRecordStore() throws IOException {
        save();
    }

    private void load() throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            records.clear();
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                int len = in.readInt();
                byte[] data = new byte[len];
                in.readFully(data);
                records.add(data);
            }
        }
    }

    private void save() throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            out.writeInt(records.size());
            for (byte[] rec : records) {
                out.writeInt(rec.length);
                out.write(rec);
            }
        }
    }
}
