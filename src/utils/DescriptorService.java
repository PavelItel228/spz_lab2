    package utils;

    import filesystem.Block;
    import filesystem.Descriptor;

    import java.nio.ByteBuffer;
    import java.nio.charset.Charset;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.IntStream;

    import static utils.ByteUtils.*;
    import static utils.Constants.*;
    import static utils.Constants.FILE_NAME_SIZE;

    public class DescriptorService {
        public static short getDescriptorId(String name) {
            Block block = new Block(Context.rootDirectory.getLinkedBlocks().get(0), true, true);
            Integer mappingSize = FILE_NAME_SIZE + INTEGER_SIZE;
            Optional<Short> descriptorId = Optional.empty();
            for (int i = 0; i < BLOCKS_SIZE / mappingSize; i++) {
                int start = i * mappingSize;
                int end = start + mappingSize;
                if (block.getMemory().subList(start, end).stream().anyMatch((j) -> j != 0)) {
                    for (int j = 0; j < FILE_NAME_SIZE; j++) {
                        if (block.getMemory().subList(start, end).get(j) != 0) {
                            String fileName = byteListToString(block.getMemory().subList(start, end).subList(j, FILE_NAME_SIZE), Charset.defaultCharset());
                            if (fileName.equals(name)) {
                                descriptorId = Optional.of(intFromByteList(block.getMemory().subList(start, end).subList(FILE_NAME_SIZE, mappingSize)));
                            }
                            break;
                        }
                    }
                }
            }
            if (descriptorId.isEmpty()) {
                throw new RuntimeException("File not found");
            }
            return descriptorId.get();
        }

        public static void deleteBlockFromDescriptor(Descriptor descriptor, int blocksToDelete) {
            Integer fileBlockId = descriptor.getLinkedBlocks().get(descriptor.getLinkedBlocks().size() - 1);
            Block block = new Block(fileBlockId, Boolean.FALSE, Boolean.FALSE);
            int deletedBlocks = 0;
            for (int i = (BLOCKS_SIZE / INTEGER_SIZE) - 1; i > -1; i--) {
                int start = i * INTEGER_SIZE;
                int end = start + INTEGER_SIZE;
                if (block.getMemory().subList(start, end).stream().anyMatch((k) -> k != 0)) {
                    ArrayList<Byte> blockToDeleteId = new ArrayList<>();
                    blockToDeleteId.add(block.getMemory().subList(start, end).get(0));
                    blockToDeleteId.add(block.getMemory().subList(start, end).get(1));
                    int blockId = intFromByteList(blockToDeleteId);
                    Context.bitMap.reset(blockId);
                    block.getMemory().subList(start, end).set(0, (byte) 0);
                    block.getMemory().subList(start, end).set(1, (byte) 0);
                    deletedBlocks++;
                }
                if (deletedBlocks == blocksToDelete) {
                    break;
                }
            }
            System.out.printf("%d block deleted%n", deletedBlocks);
        }

        public static void addBlocksToDescriptor(Descriptor descriptor) {
            short emptyBlock = (short) IntStream.range(0, BLOCKS_NUMBER)
                    .filter((p) -> !Context.bitMap.test(p)).findFirst().orElseThrow(OutOfMemoryError::new);
            List<Byte> blockId = convertBytesToList(ByteBuffer.allocate(2).putShort(emptyBlock).array());
            Integer fileBlockId = descriptor.getLinkedBlocks().get(descriptor.getLinkedBlocks().size() - 1);
            Block block = new Block(fileBlockId, Boolean.FALSE, Boolean.FALSE);
            boolean allocated = false;
            for (int j = 0; j < BLOCKS_SIZE / INTEGER_SIZE; j++) {
                int start = j * INTEGER_SIZE;
                int end = start + INTEGER_SIZE;
                if (block.getMemory().subList(start, end).stream().allMatch((k) -> k == 0)) {
                    block.getMemory().subList(start, end).set(0, blockId.get(0));
                    block.getMemory().subList(start, end).set(1, blockId.get(1));
                    allocated = true;
                    break;
                }
            }
            Context.bitMap.set((int) emptyBlock);
            if (!allocated) {
                throw new RuntimeException("Cant find memory to save block id");
            }
        }
    }
