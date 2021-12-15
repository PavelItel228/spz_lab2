package command;

import filesystem.Descriptor;
import utils.Context;

import java.util.List;


public class LsCommand implements Command {
    @Override
    public void execute(List<String> args) {
        System.out.println("name          type    size    references    descriptor id");
        Context.currentDirectory.getDirectoryMappings().entrySet().forEach((entry) -> {
            Descriptor descriptor = entry.getValue();
            System.out.printf("%s%13s%8d%14d%13d%n", entry.getKey(), descriptor.getFileType(), descriptor.getSize(), descriptor.getReferenceNumber(), descriptor.getId());
        });
    }
}
