package command;

import filesystem.Block;
import filesystem.Descriptor;
import utils.Context;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.ByteUtils.convertBytesToList;
import static utils.ByteUtils.intFromByteList;
import static utils.Constants.BLOCKS_SIZE;
import static utils.Constants.INTEGER_SIZE;
import static utils.DescriptorService.addBlocksToDescriptor;

public class WriteCommand implements Command {
    @Override
    public void execute(List<String> args) {
        int fg;
        int offset;
        try {
            fg = Integer.parseInt(args.get(0));
            offset = Integer.parseInt(args.get(1));
        } catch (NumberFormatException exception) {
            System.out.println("incorrect argument");
            return;
        }

        if (!Context.openedFiles.containsKey(fg)) {
            System.out.println("File is not opened");
            return;
        }
        Descriptor fileDescriptor = Context.descriptors.get(Context.openedFiles.get(fg));
        LinkedList<Byte> data = convertBytesToList(args.get(2).getBytes(StandardCharsets.UTF_8));
        if (offset + data.size() > fileDescriptor.getSize()) {
            System.out.println("allocating new blocks");
            int blocksNeeded = (offset + data.size() - fileDescriptor.getSize()) / BLOCKS_SIZE + 1;
            for (int i = 0; i < blocksNeeded; i++) {
                addBlocksToDescriptor(fileDescriptor);
            }
        }

        int startFromBlock = offset / BLOCKS_SIZE;
        List<Block> blocksToWrite = readBlocksIds(fileDescriptor).stream()
                .skip(startFromBlock)
                .map((fileBlockId) -> new Block((int) fileBlockId, false, false))
                .collect(Collectors.toList());
        List<List<Byte>> dividedData = divideDataToBlocks(data, offset);
        for (int i = 0; i < blocksToWrite.size(); i++) {
            Block blockToWrite = blocksToWrite.get(i);
            List<Byte> dataToWrite = dividedData.get(i);
            for (int j = 0; j < dataToWrite.size(); j++) {
                blockToWrite.getMemory().set(offset + j, dataToWrite.get(j));
            }
            offset = 0;
        }
        fileDescriptor.setSize(readBlocksIds(fileDescriptor).size() * BLOCKS_SIZE);
        System.out.println("Written successfully");
    }

    public static List<Short> readBlocksIds(Descriptor descriptor) {
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

    private List<List<Byte>> divideDataToBlocks(List<Byte> data, int offset) {
        List<List<Byte>> result = new ArrayList<>();
        int blockIdOffset = offset % BLOCKS_SIZE;
        int start = 0;
        int end = BLOCKS_SIZE - blockIdOffset;
        if (end > data.size()) {
            end = data.size();
        }
        result.add(data.subList(start, end));
        start = end;
        end += BLOCKS_SIZE;
        while (start < data.size()) {
            if (end > data.size()) {
                end = data.size();
            }
            result.add(data.subList(start, end));
            start = end;
            end += BLOCKS_SIZE;
        }
        return result;
    }
}
