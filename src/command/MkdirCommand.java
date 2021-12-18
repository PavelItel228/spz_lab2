package command;

import filesystem.Descriptor;
import filesystem.FileType;
import utils.Context;
import utils.PathData;

import java.util.Arrays;
import java.util.List;

import static command.MkfsCommand.createDirectory;
import static command.OpenCommand.open;
import static command.ReadCommand.readData;
import static utils.Constants.MAX_REDIRECTION;

public class MkdirCommand implements Command {
    @Override
    public void execute(List<String> args) {
        PathData pathData = resolvePath(args.get(0), true);

        Descriptor newDir = createDirectory(pathData.getDescriptor());
        pathData.getDescriptor().getDirectoryMappings().put(pathData.getFilename(), newDir);
        newDir.setReferenceNumber(1);
    }

    public static PathData resolvePath(String path, boolean isLink) {
        return resolvePath(path, isLink, false);
    }

    public static PathData resolvePath(String path, boolean isLink, boolean isCd) {
        boolean continueFlag = true;
        int redirectionCount = 0;
        String dirName = "";
        Descriptor currentDirectory = null;
        while (continueFlag) {
            if (redirectionCount == MAX_REDIRECTION){
                throw new RuntimeException("max redirection reached");
            }
            continueFlag = false;
            if (path.startsWith("/")) {
                currentDirectory = Context.rootDirectory;
            } else {
                currentDirectory = Context.currentDirectory;
            }

            String pathWithoutSlash = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
            String[] dirs = pathWithoutSlash.split("/");
            if (dirs.length == 0) {
                return new PathData(currentDirectory, "");
            }

            for (int i = 0; i < dirs.length; i++) {
                dirName = dirs[i];
                if (dirName.equals("")) {
                    continue;
                }
                Descriptor currentDescriptor = currentDirectory.getDirectoryMappings().get(dirName);
                if (currentDescriptor == null) {
                    if (i == (dirs.length - 1)) {
                        return new PathData(currentDirectory, dirName);
                    } else {
                        throw new RuntimeException("File not found");
                    }
                }
                if (currentDescriptor.getFileType().equals(FileType.FILE) && isCd){
                    throw new RuntimeException("not a directory: " + dirName);
                }
                if (currentDescriptor.getFileType().equals(FileType.SIMLINK)) {
                    if (i == (dirs.length - 1) && !isLink) {
                        return new PathData(currentDescriptor, dirName);
                    } else {
                        String symlinkPath = readDataFromSymlink(currentDescriptor);
                        if (symlinkPath.startsWith("/")) {
                            dirs = Arrays.copyOfRange(dirs, i, dirs.length);
                            dirs[0] = symlinkPath;
                        } else {
                            dirs[i] = symlinkPath;
                        }
                        path = String.join("/", dirs);
                        continueFlag = true;
                        redirectionCount +=1;
                        break;
                    }
                }
                currentDirectory = currentDescriptor;

            }
        }
        return new PathData(currentDirectory, dirName);
    }

    private static String readDataFromSymlink(Descriptor descriptor) {
        Integer fd = open(descriptor);
        String path = readData(fd, 0, descriptor.getSize()).replaceAll(new String(new byte[]{0}), "");
        Context.openedFiles.remove(fd);
        return path;
    }
}
