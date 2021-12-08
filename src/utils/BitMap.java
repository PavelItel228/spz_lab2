package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BitMap {
    private Integer size;
    private final List<Integer> bits = new ArrayList<>();

    public BitMap(Integer size) {
        IntStream.range(0, size).forEach((i) -> {
            bits.add(0);
        });
    }

    public Boolean test(Integer position) {
        return bits.get(position) == 1;
    }

    public void set(Integer position) {
        bits.set(position, 1);
    }

    public void reset(Integer position) {
        bits.set(position, 0);
    }
}
