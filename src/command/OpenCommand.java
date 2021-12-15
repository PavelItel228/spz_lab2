package command;

import filesystem.Descriptor;
import utils.Context;
import utils.PathData;

import java.util.List;

import static command.MkdirCommand.resolvePath;

public class OpenCommand implements Command {
    @Override
    public void execute(List<String> args) {
        Integer fd = open(args.get(0));
        System.out.printf("file %s opened, fd is %d%n", args.get(0), fd);
    }

    public static Integer open(String name){
        Integer fd = generateFD();
        PathData pathData = resolvePath(name, true);
        Context.openedFiles.put(fd, pathData.getDescriptor().getId());
        return fd;
    }

    private static int generateFD() {
        Integer fd = null;
        while (fd == null) {
            fd = (int) ((Math.random() * 100) + 0);
            if (Context.openedFiles.containsKey(fd)) {
                System.out.println("Collision");
                fd = null;
            }
        }
        return fd;
    }

    public static int open(Descriptor descriptor){
        Integer fd = generateFD();
        Context.openedFiles.put(fd, descriptor.getId());
        return fd;
    }
}
