package command;

import utils.Context;
import utils.PathData;

import java.util.List;

import static command.MkdirCommand.resolvePath;

public class CdCommand implements Command{
    @Override
    public void execute(List<String> args) {
        PathData pathData = resolvePath(args.get(0), true);
        Context.currentDirectory = pathData.getDescriptor();
    }
}
