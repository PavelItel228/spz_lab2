package command;

import exception.ProgramFinishedException;
import utils.Context;

import java.util.List;

public class ExitCommand implements Command{
    @Override
    public void execute(List<String> args) {
        throw new ProgramFinishedException();
    }
}
