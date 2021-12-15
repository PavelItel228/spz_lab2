package command;

import filesystem.Block;
import filesystem.Descriptor;
import utils.Context;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.ByteUtils.byteListToString;
import static utils.ByteUtils.intFromByteList;
import static utils.Constants.BLOCKS_SIZE;
import static utils.Constants.INTEGER_SIZE;

public class ReadCommand implements Command{
    @Override
    public void execute(List<String> args) {
        int fg;
        int offset;
        int size;
        try {
            fg = Integer.parseInt(args.get(0));
            offset = Integer.parseInt(args.get(1));
            size = Integer.parseInt(args.get(2));
        } catch (NumberFormatException exception) {
            System.out.println("incorrect argument");
            return;
        }

        System.out.println(readData(fg, offset, size));
    }

    public static String readData(int fg, int offset, int size) {
        if (!Context.openedFiles.containsKey(fg)) {
            System.out.println("File is not opened");
            return "";
        }
        Descriptor fileDescriptor = Context.descriptors.get(Context.openedFiles.get(fg));

        if(offset + size > fileDescriptor.getSize()){
            System.out.println("size and offset is bigger than descriptor size");
            return "";
        }

        int startFromBlock = offset / BLOCKS_SIZE;
        int blockOffset = offset % BLOCKS_SIZE;
        int endBlock = startFromBlock + (int) Math.ceil((double)size / (double) BLOCKS_SIZE);
        int lastBlockOffset = BLOCKS_SIZE - ((endBlock - startFromBlock)* BLOCKS_SIZE - size - blockOffset);

        List<Block> blocksToRead = readBlocksIds(fileDescriptor).subList(startFromBlock, endBlock).stream()
                .map((fileBlockId) -> new Block((int) fileBlockId, false, false))
                .collect(Collectors.toList());

        int start = blockOffset;
        int end;
        if (offset + size < BLOCKS_SIZE){
            end = size + offset;
        } else {
            end = BLOCKS_SIZE;
        }
        ArrayList<Byte> data = new ArrayList<>(blocksToRead.get(0).getMemory().subList(start, end));
        if (blocksToRead.size() > 2){
            blocksToRead.subList(1, blocksToRead.size() -1).forEach(block -> data.addAll(block.getMemory()));
            start = 0;
            end = lastBlockOffset;
            data.addAll(blocksToRead.get(blocksToRead.size() - 1).getMemory().subList(start, end));
        }
        return byteListToString(data, Charset.defaultCharset());
    }

    private static List<Short> readBlocksIds(Descriptor descriptor) {
        List<Short> result = new ArrayList<>();
        Integer fileBlockId = descriptor.getLinkedBlocks().get(descriptor.getLinkedBlocks().size() - 1);
        Block block = new Block(fileBlockId, Boolean.FALSE, Boolean.FALSE);
        for (int j = 0; j < BLOCKS_SIZE / INTEGER_SIZE; j++) {
            int start = j * INTEGER_SIZE;
            int end = start + INTEGER_SIZE;
            if (block.getMemory().subList(start, end).stream().anyMatch((k) -> k != 0)) {
                result.add(intFromByteList(block.getMemory().subList(start, end)));
            }
        }
        return result;
    }
}
