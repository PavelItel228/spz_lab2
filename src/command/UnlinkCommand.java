package command;

import filesystem.Block;
import filesystem.Descriptor;
import filesystem.FileType;
import utils.Context;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static command.WriteCommand.readBlocksIds;
import static utils.ByteUtils.byteListToString;
import static utils.ByteUtils.convertBytesToList;
import static utils.Constants.*;
import static utils.DescriptorService.getDescriptorId;

public class UnlinkCommand implements Command {
    @Override
    public void execute(List<String> args) {
        Block block = new Block(Context.rootDirectory.getLinkedBlocks().get(0), true, true);
        short descriptorId = getDescriptorId(args.get(0));
        Descriptor descriptor = Context.descriptors.get(descriptorId);
        deleteFileMapping(args.get(0), block);
        descriptor.setReferenceNumber(descriptor.getReferenceNumber() - 1);

        if (descriptor.getReferenceNumber() == 0) {
            List<Short> blockIds = readBlocksIds((descriptor));
            blockIds.forEach(blockId -> Context.bitMap.reset((int) blockId));
            freeDescriptor(descriptor);
        }
    }

    private void deleteFileMapping(String name, Block block) {
        LinkedList<Byte> nameBytes = convertBytesToList(name.getBytes(StandardCharsets.UTF_8));
        Integer mappingSize = FILE_NAME_SIZE + INTEGER_SIZE;

        if (nameBytes.size() < FILE_NAME_SIZE) {
            for (int i = nameBytes.size(); i < FILE_NAME_SIZE; i++) {
                nameBytes.addFirst((byte) 0);
            }
        }

        for (int i = 0; i < BLOCKS_SIZE / mappingSize; i++) {
            int start = i * mappingSize;
            int end = start + mappingSize;
            if (block.getMemory().subList(start, end).stream().anyMatch((j) -> j != 0)) {
                for (int j = 0; j < FILE_NAME_SIZE; j++) {
                    if (block.getMemory().subList(start, end).get(j) != 0) {
                        String fileName = byteListToString(block.getMemory().subList(start, end).subList(j, FILE_NAME_SIZE), Charset.defaultCharset());
                        if (fileName.equals(name)) {
                            for (int k = start; k < end; k++) {
                                block.getMemory().set(k, (byte) 0);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public static void freeDescriptor(Descriptor descriptor) {
        descriptor.setUsed(false);
        descriptor.setFileType(FileType.FILE);
        descriptor.setReferenceNumber(0);
        descriptor.setSize(0);
        descriptor.setLinkedBlocks(new ArrayList<>());
        Context.bitMap.reset((int)descriptor.getId());
    }
}
