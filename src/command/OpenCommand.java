package command;

import utils.Context;

import java.util.List;

import static utils.DescriptorService.getDescriptorId;

public class OpenCommand implements Command {
    @Override
    public void execute(List<String> args) {
        Integer fd = generateFD();
        Context.openedFiles.put(fd, getDescriptorId(args.get(0)));
        System.out.printf("file %s opened, fd is %d%n", args.get(0), fd);
    }

    private int generateFD() {
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
}
