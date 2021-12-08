package command;

import utils.BitMap;
import utils.Context;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.BLOCKS_NUMBER;

public class UnmountCommand implements Command{
    @Override
    public void execute(List<String> args) {
        Context.bitMap = new BitMap(BLOCKS_NUMBER);
        Context.memory = new ArrayList<>();
        Context.rootDirectory = null;
        Context.descriptors = new ArrayList<>();
        System.out.println("unmounted");
    }
}
