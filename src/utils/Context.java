package utils;

import filesystem.Descriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static utils.Constants.BLOCKS_NUMBER;

public class Context {
    public static BitMap bitMap = new BitMap(BLOCKS_NUMBER);
    public static List<Descriptor> descriptors = new ArrayList<>();
    public static Descriptor rootDirectory = null;
    public static List<Byte> memory = new ArrayList<>();
    public static HashMap<Integer, Short> openedFiles = new HashMap<>();
}
