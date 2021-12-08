package command;

import utils.Context;

import java.util.List;

public class CloseCommand implements Command{
    @Override
    public void execute(List<String> args) {
        int fg;
        try {
            fg = Integer.parseInt(args.get(0));
        } catch (NumberFormatException exception) {
            System.out.println("incorrect argument");
            return;
        }
        Context.openedFiles.remove(fg);
        System.out.printf("File with fg %d closed%n", fg);
    }
}
