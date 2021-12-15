package command;

import exception.NoFreeDescriptorsException;
import filesystem.Descriptor;
import filesystem.FileType;
import utils.Context;
import utils.PathData;

import java.util.ArrayList;
import java.util.List;

import static command.CreateCommand.initializeFile;
import static command.MkdirCommand.resolvePath;
import static command.OpenCommand.open;

public class SymlinkCommand implements Command{
    @Override
    public void execute(List<String> args) {
        Descriptor freeDescriptor = Context.descriptors.stream()
                .filter(descriptor -> !descriptor.getUsed()).findFirst().orElseThrow(NoFreeDescriptorsException::new);
        initializeFile(freeDescriptor);
        freeDescriptor.setFileType(FileType.SIMLINK);
        PathData pathData = resolvePath(args.get(1), true);
        pathData.getDescriptor().getDirectoryMappings().put(pathData.getFilename(), freeDescriptor);
        Integer fd = open(freeDescriptor);
        ArrayList<String> writeArgs = new ArrayList<>();
        writeArgs.add(fd.toString());
        writeArgs.add("0");
        writeArgs.add(args.get(0));
        new WriteCommand().execute(writeArgs);
        Context.openedFiles.remove(fd);
    }
}
