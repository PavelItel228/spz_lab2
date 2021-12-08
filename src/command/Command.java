package command;

import java.util.List;
import utils.Context;

public interface Command {

    public void execute(List<String> args);
}
