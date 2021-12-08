package command;

import utils.Context;

import java.util.List;

public class HelpCommand implements Command{
    @Override
    public void execute(List<String> args) {
        System.out.println("The program simulates a file system driver\n" +
        "commands:\n" +
        "--> help - lists all commands\n" +
        "--> exit - finishes program\n" +
        "--> mkfs <n> - formats file system with <n> descriptors\n" +
        "--> mount - mounts a storage, currently only mount from memory is available, so use \"mount memory\"\n" +
        "--> umount - unmounts storage, deletes all data\n" +
        "--> fstad <id> - returns info about descriptor with id <id>, ids start from 0\n" +
        "--> ls - lists file links with descriptor ids\n" +
        "--> create <name> - creates file with name <name>\n" +
        "--> open <name> - opens file with name <name>, returns fd\n" +
        "--> close <fd> - closes file with <fd>, reads and writes will not be available\n" +
        "--> read <fd> <offset> <size> - reads data from file with <fd>, starts from <offset> byte and reads <size> bytes\n" +
        "--> write <fd> <offset> <data> - writes <data> to file with <fd>, starts from <offset>\n" +
        "--> link <name1> <name2> - links <name2> to file with <name1>\n" +
        "--> unlink <name> - deletes link <name>\n" +
        "--> truncate <name> <size> - truncates file <name> to size <size>\n");
    }
}
