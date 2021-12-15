package utils;

import filesystem.Descriptor;

public class PathData {
    Descriptor descriptor;
    String filename;

    public PathData(Descriptor descriptor, String name) {
        this.descriptor = descriptor;
        this.filename = name;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
