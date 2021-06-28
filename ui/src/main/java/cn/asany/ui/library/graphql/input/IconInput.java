package cn.asany.ui.library.graphql.input;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IconInput {
    /**
     * 名称
     */
    private String name;
    /**
     * 名称
     */
    private String unicode;
    /**
     * 描述
     */
    private String description;
    /**
     * 正文
     */
    private String content;
    /**
     * 标签
     */
    private List<String> tags;
}
