package command;

import utils.BitMap;
import utils.Context;

import java.util.List;
import java.util.stream.IntStream;

import static utils.Constants.BLOCKS_NUMBER;
import static utils.Constants.BLOCKS_SIZE;

public class MountCommand implements Command{
    @Override
    public void execute(List<String> args) {
        Context.bitMap = new BitMap(BLOCKS_NUMBER);
        IntStream.range(0, BLOCKS_NUMBER * BLOCKS_SIZE).forEach((i) -> {
            Context.memory.add((byte) 0);
        });
        System.out.println("mounted file system");
    }
}
