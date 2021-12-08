package command;

import utils.Context;

import java.util.List;

public class ExceptionCommand implements Command{
    @Override
    public void execute(List<String> args) {
        System.out.println("command not found");
    }
}
