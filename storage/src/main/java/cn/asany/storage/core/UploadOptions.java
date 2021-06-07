package cn.asany.storage.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.util.common.StringUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadOptions {
    /**
     * 原始文件名
     */
    private String name;
    private String url;
    /**
     * 上传空间
     */
    private String space;
    /**
     * 插件
     */
    private String plugin;
    /**
     * 文件名策略
     */
    private String nameStrategy;
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

}
