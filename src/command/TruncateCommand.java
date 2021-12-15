package command;

import filesystem.Descriptor;
import utils.Context;

import java.util.List;

import static command.MkdirCommand.resolvePath;
import static command.WriteCommand.readBlocksIds;
import static utils.Constants.BLOCKS_SIZE;
import static utils.DescriptorService.*;

public class TruncateCommand implements Command {
    @Override
    public void execute(List<String> args) {
        int size;
        try {
            size = Integer.parseInt(args.get(1));
        } catch (NumberFormatException exception) {
            System.out.println("incorrect argument");
            return;
        }

        Descriptor descriptor = resolvePath(args.get(0), true).getDescriptor();

        if (size > descriptor.getSize()) {
            int blockToAdd = ((size - descriptor.getSize()) / BLOCKS_SIZE) + 1;
            for (int i = 0; i < blockToAdd; i++) {
                addBlocksToDescriptor(descriptor);
            }
            System.out.printf("%d blocks added%n", blockToAdd);
            descriptor.setSize(readBlocksIds(descriptor).size() * BLOCKS_SIZE);
        } else if (size < descriptor.getSize()) {
            int newBlockNumber = size / BLOCKS_SIZE + 1;
            int blockToDelete = readBlocksIds(descriptor).size() - newBlockNumber;
            deleteBlockFromDescriptor(descriptor, blockToDelete);
            descriptor.setSize(readBlocksIds(descriptor).size() * BLOCKS_SIZE);
        } else {
            System.out.println("new size is equals to descriptor size");
        }

    }
}
