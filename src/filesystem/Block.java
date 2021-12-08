package filesystem;

import utils.Context;

import java.util.List;

import static utils.Constants.BLOCKS_SIZE;

public class Block {
    private Integer n;
    private Boolean isLinksBlock;
    private Boolean isMappingBlock;
    private Integer start;
    private Integer end;
    private List<Byte> memory;


    public Block(Integer n, Boolean isLinksBlock, Boolean isMappingBlock) {
        this.n = n;
        this.isLinksBlock = isLinksBlock;
        this.isMappingBlock = isMappingBlock;
        this.start = BLOCKS_SIZE * n;
        this.end = start + BLOCKS_SIZE;
        this.memory = Context.memory.subList(start, end);
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Boolean getLinksBlock() {
        return isLinksBlock;
    }

    public void setLinksBlock(Boolean linksBlock) {
        isLinksBlock = linksBlock;
    }

    public Boolean getMappingBlock() {
        return isMappingBlock;
    }

    public void setMappingBlock(Boolean mappingBlock) {
        isMappingBlock = mappingBlock;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public List<Byte> getMemory() {
        return memory;
    }

    public void setMemory(List<Byte> memory) {
        this.memory = memory;
    }
}
