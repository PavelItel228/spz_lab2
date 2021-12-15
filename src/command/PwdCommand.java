package command;

import filesystem.Descriptor;
import utils.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PwdCommand implements Command{
    @Override
    public void execute(List<String> args) {
        System.out.println(getCurrentDirectoryPath());
    }

    public static String getCurrentDirectoryPath(){
        if (Context.currentDirectory == null){
            return "";
        }
        StringBuilder result = new StringBuilder();
        Short currentDirectoryDescriptorId = Context.currentDirectory.getId();
        Descriptor previousDirectory = Context.currentDirectory.getDirectoryMappings().get("..");
        while (true){
            if (Objects.equals(previousDirectory.getId(), currentDirectoryDescriptorId)){
                result.insert(0, "/");
                break;
            }
            Short finalCurrentDirectoryDescriptorId = currentDirectoryDescriptorId;
            Map.Entry<String, Descriptor> dir = previousDirectory.getDirectoryMappings().entrySet().stream()
                    .filter(entry -> Objects.equals(entry.getValue().getId(), finalCurrentDirectoryDescriptorId))
                    .findFirst().get();
            result.insert(0, dir.getKey() + "/");
            currentDirectoryDescriptorId =  previousDirectory.getId();
            previousDirectory = previousDirectory.getDirectoryMappings().get("..");
        }
        return result.toString();
    }
}
