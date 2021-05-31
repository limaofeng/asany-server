package cn.asany.security.core.graphql.types;

import lombok.Data;

import java.util.Date;

@Data
public class DepartmentDTO {

    private Long id;

    private String name;

    private String description;

    private String path;

    private String sn;

    private Integer sort;

    private String organizationId;

    private Long pid;

    private Boolean enabled;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Long type;


}