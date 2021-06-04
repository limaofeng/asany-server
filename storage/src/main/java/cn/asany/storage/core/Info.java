package cn.asany.storage.core;

import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.StringUtil;

import javax.validation.constraints.NotNull;

public class Info {
    private String name;
    private String url;
    @NotNull(groups = RESTful.POST.class)
    private String dir;
    private String entireFileName;
    private String entireFileDir;
    private String entireFileHash;
    private String partFileHash;
    private Integer total;
    private Integer index;

    public boolean isPart() {
        return StringUtil.isNotBlank(this.entireFileHash);
    }

    public String getPartName() {
        return entireFileName + ".part" + StringUtil.addZeroLeft(index.toString(), total.toString().length());
    }

    public String getDir() {
        return dir;
    }

    public String getEntireFileHash() {
        return entireFileHash;
    }

    public String getPartFileHash() {
        return partFileHash;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getEntireFileName() {
        return entireFileName;
    }

    public String getEntireFileDir() {
        return entireFileDir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setEntireFileName(String entireFileName) {
        this.entireFileName = entireFileName;
    }

    public void setEntireFileDir(String entireFileDir) {
        this.entireFileDir = entireFileDir;
    }

    public void setEntireFileHash(String entireFileHash) {
        this.entireFileHash = entireFileHash;
    }

    public void setPartFileHash(String partFileHash) {
        this.partFileHash = partFileHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
