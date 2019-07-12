package pr.rpo.server;

import pr.rpo.tree.btree.BTree;
import pr.rpo.tree.btree.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Block {
    byte start = 0b0110_0110;
    int recordTotalByte;
    Record[] records;

    public void from(File file) {
        if(file.exists()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(file,"r");
                if(raf.readByte() == start) {
                    byte[] totalByte = new byte[4];
                    raf.read(totalByte);
                    recordTotalByte = ByteHelper.bytes2int(totalByte);
                    byte[] recordBytes = new byte[recordTotalByte];
                    raf.read(recordBytes);

                    records = Record.from(recordBytes);
                }else {
                    System.out.println("error reading file");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            return;
        }
    }

    public void from(BTree bTree) {
        List<Record> recordList = new ArrayList<>();
        bTree.travelNode((Node e) -> {
            for(int i = 0; i < e.size(); i++) {
                recordList.add(new Record(e.keys[i], (String)e.vals[i]));
            }
        });

        records = new Record[recordList.size()];
        recordList.toArray(records);
    }

    public BTree getIndex() {
        BTree bTree = new BTree();
        for(int i = 0; i < records.length; i++) {
            bTree.put(records[i].key, records[i].value);
        }
        return bTree;
    }

    public void writeToFile(FileOutputStream fos) {
        try {
            fos.write(start);
            for(int i = 0; i < records.length; i++) {
                recordTotalByte += records[i].toByte().length;
            }
            fos.write(ByteHelper.int2Bytes(recordTotalByte));

            for(int i = 0; i < records.length; i++) {
                fos.write(records[i].toByte());
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeObjectToFile(FileOutputStream fos, Object object) {
        try(
                        ObjectOutputStream oos = new ObjectOutputStream(fos)
                ) {
            fos.write(start);
            fos.flush();
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] toByte() {
        System.out.println(start);
        System.out.println(recordTotalByte);
        for(Record r : records) {
            System.out.println(r.toByte());
        }
        return null;
    }

    public static void main(String[] args) {

    }
}
