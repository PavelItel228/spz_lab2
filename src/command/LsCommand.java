package command;

import filesystem.Block;
import filesystem.Descriptor;
import utils.Context;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static utils.ByteUtils.byteListToString;
import static utils.ByteUtils.intFromByteList;
import static utils.Constants.*;


public class LsCommand implements Command{
    @Override
    public void execute(List<String> args) {
        Block block = new Block(Context.rootDirectory.getLinkedBlocks().get(0), true, true);
        Integer mappingSize = FILE_NAME_SIZE + INTEGER_SIZE;
        List<String> names = new ArrayList<>();
        List<Short> discriptorIds = new ArrayList<>();
        for (int i = 0 ; i < BLOCKS_SIZE / mappingSize; i++){
            int start = i * mappingSize;
            int end = start + mappingSize;
            if (block.getMemory().subList(start, end).stream().anyMatch((j) -> j != 0)){
                for (int j = 0; j < FILE_NAME_SIZE; j++) {
                    if (block.getMemory().subList(start, end).get(j) != 0){
                        names.add(byteListToString(block.getMemory().subList(start, end).subList(j, FILE_NAME_SIZE), Charset.defaultCharset()));
                        discriptorIds.add(intFromByteList(block.getMemory().subList(start, end).subList(FILE_NAME_SIZE, mappingSize)));
                        break;
                    }
                };
            }
        }
        System.out.println("name          type    size    references    descriptor id");
        IntStream.range(0, names.size()).forEach((i) ->{
            Descriptor descriptor = Context.descriptors.get(discriptorIds.get(i));
            System.out.printf("%s%13s%8d%14d%13d%n", names.get(i), descriptor.getFileType(), descriptor.getSize(), descriptor.getReferenceNumber(), descriptor.getId());
        });
    }
}
