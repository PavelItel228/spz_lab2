package command;

import utils.PathData;

import java.util.List;

import static command.MkdirCommand.resolvePath;

public class LinkCommand implements Command {
    @Override
    public void execute(List<String> args) {
        PathData source = resolvePath(args.get(0), false);
        PathData target = resolvePath(args.get(1), false);
        target.getDescriptor().getDirectoryMappings().put(target.getFilename(), source.getDescriptor());
        source.getDescriptor().setReferenceNumber(source.getDescriptor().getReferenceNumber() + 1);
    }
}
