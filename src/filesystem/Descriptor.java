package filesystem;


import java.util.HashMap;
import java.util.List;

public class Descriptor {
    private Short id;
    private Boolean isUsed;
    private FileType fileType;
    private Integer referenceNumber;
    private Integer size;
    private List<Integer> linkedBlocks;
    private HashMap<String, Descriptor> directoryMappings = new HashMap<>();

    public Descriptor(Short id, Boolean isUsed, FileType fileType, Integer referenceNumber, Integer size, List<Integer> linkedBlocks) {
        this.id = id;
        this.isUsed = isUsed;
        this.fileType = fileType;
        this.referenceNumber = referenceNumber;
        this.size = size;
        this.linkedBlocks = linkedBlocks;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Integer getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Integer referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Integer> getLinkedBlocks() {
        return linkedBlocks;
    }

    public void setLinkedBlocks(List<Integer> linkedBlocks) {
        this.linkedBlocks = linkedBlocks;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public HashMap<String, Descriptor> getDirectoryMappings() {
        return directoryMappings;
    }

    public void setDirectoryMappings(HashMap<String, Descriptor> directoryMappings) {
        this.directoryMappings = directoryMappings;
    }
}
