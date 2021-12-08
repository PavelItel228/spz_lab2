package command;

import filesystem.Block;
import filesystem.Descriptor;
import utils.Context;

import java.util.List;

import static command.CreateCommand.createFileMapping;
import static utils.DescriptorService.getDescriptorId;

public class LinkCommand implements Command {
    @Override
    public void execute(List<String> args) {
        short descriptorId = getDescriptorId(args.get(0));
        Descriptor descriptor = Context.descriptors.get(descriptorId);
        Block block = new Block(Context.rootDirectory.getLinkedBlocks().get(0), true, true);
        createFileMapping(block, descriptor, args.get(1));
        descriptor.setReferenceNumber(descriptor.getReferenceNumber() + 1);
    }
}
