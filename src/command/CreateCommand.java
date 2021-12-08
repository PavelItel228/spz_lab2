    package command;

    import exception.NoFreeDescriptorsException;
    import filesystem.Block;
    import filesystem.Descriptor;
    import filesystem.FileType;
    import utils.Context;

    import java.nio.ByteBuffer;
    import java.nio.charset.StandardCharsets;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;
    import java.util.stream.IntStream;
    import java.util.stream.Stream;

    import static utils.Constants.*;

    public class CreateCommand implements Command {
        @Override
        public void execute(List<String> args) {
            Descriptor unusedDescriptor = Context.descriptors.stream()
                    .filter(descriptor -> !descriptor.getUsed()).findFirst().orElseThrow(NoFreeDescriptorsException::new);
            initializeFile(unusedDescriptor);
            Block block = new Block(Context.rootDirectory.getLinkedBlocks().get(0), true, true);
            createFileMapping(block, unusedDescriptor, args.get(0));
            System.out.printf("file %s created%n", args.get(0));
        }

        private void initializeFile(Descriptor descriptor) {
            descriptor.setUsed(true);
            descriptor.setFileType(FileType.FILE);
            descriptor.setReferenceNumber(1);
            descriptor.setSize(0);
            Integer emptyBlock = IntStream.range(0, BLOCKS_NUMBER)
                    .filter((i) -> !Context.bitMap.test(i)).findFirst().orElseThrow(OutOfMemoryError::new);
            Context.bitMap.set(emptyBlock);
            descriptor.getLinkedBlocks().add(emptyBlock);
        }

        public static void createFileMapping(Block block, Descriptor descriptor, String name) {
            if (!block.getLinksBlock() && !block.getMappingBlock()) {
                return;
            }

            Integer mappingSize = FILE_NAME_SIZE + INTEGER_SIZE;
            Optional<Integer> slotForMapping = Optional.empty();
            for (int i = 0; i < BLOCKS_SIZE / mappingSize; i++) {
                int start = i * mappingSize;
                int end = start + mappingSize;
                if (block.getMemory().subList(start, end).stream().allMatch((j) -> j == 0)) {
                    slotForMapping = Optional.of(i);
                    break;
                }
            }

            if (slotForMapping.isEmpty()) {
                throw new RuntimeException();
            }

            if (name.length() > FILE_NAME_SIZE) {
                name = name.substring(0, FILE_NAME_SIZE);
                System.out.println("file name too big, truncated");
            }
            LinkedList<Byte> nameBytes = convertBytesToList(name.getBytes(StandardCharsets.UTF_8));
            if (nameBytes.size() < FILE_NAME_SIZE) {
                for (int i = nameBytes.size(); i < FILE_NAME_SIZE; i++) {
                    nameBytes.addFirst((byte) 0);
                }
            }
            List<Byte> descriptorIdBytes = convertBytesToList(ByteBuffer.allocate(2).putShort(descriptor.getId()).array());
            List<Byte> data = Stream.concat(nameBytes.stream(), descriptorIdBytes.stream())
                    .collect(Collectors.toList());
            if (data.size() + (slotForMapping.get() * mappingSize) > BLOCKS_SIZE) {
                throw new RuntimeException();
            }
            int s = BLOCKS_SIZE * block.getN() + (slotForMapping.get() * mappingSize);
            int end = s + data.size();
            int b = 0;
            for (int i = s; i < end; i++) {
                Context.memory.set(i, data.get(b));
                b++;
            }
        }

        private static LinkedList<Byte> convertBytesToList(byte[] bytes) {
            final LinkedList<Byte> list = new LinkedList<>();
            for (byte b : bytes) {
                list.add(b);
            }
            return list;
        }

    }
