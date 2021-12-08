package command;

import exception.NoFreeDescriptorsException;
import filesystem.Descriptor;
import filesystem.FileType;
import utils.BitMap;
import utils.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static utils.Constants.BLOCKS_NUMBER;

public class MkfsCommand implements Command {
    @Override
    public void execute(List<String> args) {
        int descriptorsNumber;
        try {
            descriptorsNumber = Integer.parseInt(args.get(0));
        } catch (NumberFormatException exception) {
            System.out.println("incorrect argument");
            return;
        }
        Context.bitMap = new BitMap(BLOCKS_NUMBER);
        Context.descriptors = IntStream.range(0, descriptorsNumber)
                .mapToObj((i) -> new Descriptor((short) i, false, FileType.FILE, 0, 0, new ArrayList<>()))
                .collect(Collectors.toList());
        createRootDirectory();
        System.out.printf("file system created with %d descriptors%n", descriptorsNumber);
    }

    private void createRootDirectory() {
        Context.rootDirectory = Context.descriptors.stream()
                .filter(descriptor -> !descriptor.getUsed()).findFirst().orElseThrow(NoFreeDescriptorsException::new);
        Context.rootDirectory.setUsed(true);
        Context.rootDirectory.setFileType(FileType.DIRECTORY);
        Context.rootDirectory.setReferenceNumber(1);
        Context.rootDirectory.setSize(0);
        Integer emptyBlock = IntStream.range(0, BLOCKS_NUMBER)
                .filter((i) -> !Context.bitMap.test(i)).findFirst().orElseThrow(OutOfMemoryError::new);
        Context.rootDirectory.getLinkedBlocks().add(emptyBlock);
        Context.bitMap.set(emptyBlock);
    }

}
