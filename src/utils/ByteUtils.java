package utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public abstract class ByteUtils {

    public static String byteListToString(List<Byte> l, Charset charset) {
        if (l == null) {
            return "";
        }
        byte[] array = new byte[l.size()];
        int i = 0;
        for (Byte current : l) {
            array[i] = current;
            i++;
        }
        return new String(array, charset);
    }

    public static short intFromByteList(List<Byte> bytes){
        byte[] intBytes = new byte[2];
        intBytes[0] = bytes.get(0);
        intBytes[1] = bytes.get(1);
        ByteBuffer wrapped = ByteBuffer.wrap(intBytes);
        return wrapped.getShort();
    }

    public static LinkedList<Byte> convertBytesToList(byte[] bytes) {
        final LinkedList<Byte> list = new LinkedList<>();
        for (byte b : bytes) {
            list.add(b);
        }
        return list;
    }
}
