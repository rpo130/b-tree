package pr.rpo.server;

public class ByteHelper {

    public static byte[] int2Bytes(int x) {
        byte[] bytes = new byte[4];
        for(int i = 0; i < 4; i++) {
            bytes[i] = (byte) (0xff & (x));
            x = x >> 8;
        }
        return bytes;
    }

    public static int bytes2int(byte[] bytes) {
        return (bytes[0] & 0xff) | (bytes[1] & 0xff) << 8 | (bytes[2] & 0xff) << 16 | (bytes[3] & 0xff) << 24;
    }

    public static long bytes2long(byte[] bytes) {
        long re = 0;
        for(int i = 0; i < bytes.length; i++) {
            long tmp = bytes[i] & 0xff;
            tmp = tmp << i*8;
            re |= tmp;
        }
        return re;
    }

    public static byte[] long2Bytes(long x) {
        byte[] bytes = new byte[8];
        for(int i = 0; i < 8; i++) {
            bytes[i] = (byte) (0xff & (x >> (i * 8)));
        }
        return bytes;
    }

}
