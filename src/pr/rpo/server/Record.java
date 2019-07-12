package pr.rpo.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Record {
    //header
    int length;
    int keyOffset;
    int valueOffset;
    LocalDateTime timestamp;

    String key;
    String value;

    private Record() {
    }

    public static Record[] from(byte[] bytes) {
        List<Record> recordList = new ArrayList<>();

        int i = 0;

        while(i < bytes.length) {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            byte[] intByte = new byte[4];

            try {
                bais.read(intByte);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Record record = new Record();
            record.length = ByteHelper.bytes2int(intByte);

            i = i+4;

            try {
                bais.read(intByte);
            } catch (IOException e) {
                e.printStackTrace();
            }

            record.keyOffset = ByteHelper.bytes2int(intByte);
            i = i+4;

            try {
                bais.read(intByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
            record.valueOffset = ByteHelper.bytes2int(intByte);
            i = i+4;

            byte[] longByte = new byte[8];
            try {
                bais.read(longByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long nanoTime = 0;
            nanoTime = ByteHelper.bytes2long(longByte);
            i = i+8;

            record.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(nanoTime), ZoneId.systemDefault());

            record.key = new String(bytes, record.keyOffset, record.valueOffset - record.keyOffset);
            i += record.valueOffset - record.keyOffset;
            record.value = new String(bytes, record.valueOffset, record.length - record.valueOffset);
            i += record.length - record.valueOffset;
            recordList.add(record);
        }
        Record[] records = new Record[recordList.size()];
        recordList.toArray(records);
        return records;
    }

    public Record(String key, String value) {
        this.key = key;
        this.value = value;

        this.length = 20 + key.getBytes().length + value.getBytes().length;
        this.keyOffset = 20;
        this.valueOffset = keyOffset + key.getBytes().length;
        this.timestamp = LocalDateTime.now();
    }

    public byte[] toByte() {
        int keyLength = key.getBytes().length;
        int valueLength = value.getBytes().length;

        int n = 0;
        byte[] bytes = new byte[20+keyLength+valueLength];
        System.arraycopy(ByteHelper.int2Bytes(length), 0, bytes, n,  4);
        n += 4;

        System.arraycopy(ByteHelper.int2Bytes(keyOffset), 0, bytes, n,  4);
        n += 4;

        System.arraycopy(ByteHelper.int2Bytes(valueOffset), 0, bytes, n, 4);
        n += 4;

        System.arraycopy(ByteHelper.long2Bytes(timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), 0, bytes, n, 8);
        n += 8;

        System.arraycopy(key.getBytes(), 0, bytes, n, keyLength);
        n += keyLength;

        System.arraycopy(value.getBytes(), 0, bytes, n, valueLength);

        return bytes;
    }

    public static void main(String[] args) {
        Record record = new Record("a", "b");
        System.out.println(Arrays.toString(ByteHelper.int2Bytes(record.length)));
        System.out.println(Arrays.toString(ByteHelper.int2Bytes(record.keyOffset)));
        System.out.println(Arrays.toString(ByteHelper.int2Bytes(record.valueOffset)));
        System.out.println(Arrays.toString(ByteHelper.long2Bytes(record.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())));
        System.out.println(Arrays.toString(record.key.getBytes()));
        System.out.println(Arrays.toString(record.value.getBytes()));


        System.out.println(Arrays.toString(record.toByte()));
    }
}
