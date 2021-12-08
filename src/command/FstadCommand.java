package command;

import filesystem.Descriptor;
import utils.Context;

import java.util.List;

public class FstadCommand implements Command{
    @Override
    public void execute(List<String> args) {
        int descriptorsNumber;
        try {
            descriptorsNumber = Integer.parseInt(args.get(0));
        } catch (NumberFormatException exception) {
            System.out.println("incorrect argument");
            return;
        }
        Descriptor descriptor = Context.descriptors.get(descriptorsNumber);
        String message = String.format("ID : %d%n", descriptorsNumber) +
                String.format("IS USED : %b%n", descriptor.getUsed()) +
                String.format("TYPE : %s%n", descriptor.getFileType()) +
                String.format("REFERENCE NUMBER : %d%n", descriptor.getReferenceNumber()) +
                String.format("SIZE : %d%n", descriptor.getSize());
        System.out.println(message);
    }
}
