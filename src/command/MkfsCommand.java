package command;

import exception.NoFreeDescriptorsException;
import filesystem.Descriptor;
import filesystem.FileType;
import utils.BitMap;
import utils.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Context.rootDirectory = createDirectory(null);
        Context.currentDirectory = Context.rootDirectory;
        System.out.printf("file system created with %d descriptors%n", descriptorsNumber);
    }

    public static Descriptor createDirectory(Descriptor parentDirectory) {
        Descriptor desc = Context.descriptors.stream()
                .filter(descriptor -> !descriptor.getUsed()).findFirst().orElseThrow(NoFreeDescriptorsException::new);
        desc.setUsed(true);
        desc.setFileType(FileType.DIRECTORY);
        desc.setReferenceNumber(1);
        desc.setSize(0);
        Integer emptyBlock = IntStream.range(0, BLOCKS_NUMBER)
                .filter((i) -> !Context.bitMap.test(i)).findFirst().orElseThrow(OutOfMemoryError::new);
        desc.getLinkedBlocks().add(emptyBlock);
        Context.bitMap.set(emptyBlock);
        createBasicLinks(desc, Optional.ofNullable(parentDirectory));
        return desc;
    }

    private static void createBasicLinks(Descriptor currentDir, Optional<Descriptor> parentDir){
        currentDir.getDirectoryMappings().put(".", currentDir);
        parentDir.ifPresentOrElse((parentDirectory) -> currentDir.getDirectoryMappings().put("..", parentDirectory),
                () -> currentDir.getDirectoryMappings().put("..", currentDir));
    }

}
