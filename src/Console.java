import command.*;
import utils.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static command.PwdCommand.getCurrentDirectoryPath;

public class Console {
    public void run(){
        HashMap<String, Command> commands = new HashMap<>();
        commands.put("help", new HelpCommand());
        commands.put("exit", new ExitCommand());
        commands.put("mkfs", new MkfsCommand());
        commands.put("mount", new MountCommand());
        commands.put("unmount", new UnmountCommand());
        commands.put("fstad", new FstadCommand());
        commands.put("create", new CreateCommand());
        commands.put("ls", new LsCommand());
        commands.put("open", new OpenCommand());
        commands.put("close", new CloseCommand());
        commands.put("write", new WriteCommand());
        commands.put("read", new ReadCommand());
        commands.put("truncate", new TruncateCommand());
        commands.put("link", new LinkCommand());
        commands.put("unlink", new UnlinkCommand());
        commands.put("mkdir", new MkdirCommand());
        commands.put("cd", new CdCommand());
        commands.put("pwd", new PwdCommand());
        commands.put("rmdir", new RmdirCommand());
        commands.put("symlink", new SymlinkCommand());


        Scanner scanner = new Scanner(System.in);
        System.out.print(" $ ");

        while (scanner.hasNext()){
            String commandWithArgs = scanner.nextLine();
            List<String> args = Arrays.stream(commandWithArgs.split(" ")).skip(1).collect(Collectors.toList());
            commands.getOrDefault(commandWithArgs.split(" ")[0], new ExceptionCommand()).execute(args);
            System.out.print(getCurrentDirectoryPath() + " $ ");
        }
    }
}
