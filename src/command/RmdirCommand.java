package command;

import filesystem.Descriptor;
import utils.PathData;

import java.util.List;

import static command.MkdirCommand.resolvePath;
import static command.UnlinkCommand.freeDescriptor;

public class RmdirCommand implements Command{
    @Override
    public void execute(List<String> args) {
        PathData pathData = resolvePath(args.get(0), true);
        Descriptor parentDir = pathData.getDescriptor().getDirectoryMappings().get("..");
        parentDir.getDirectoryMappings().remove(pathData.getFilename());
        freeDescriptor(pathData.getDescriptor());
    }
}
