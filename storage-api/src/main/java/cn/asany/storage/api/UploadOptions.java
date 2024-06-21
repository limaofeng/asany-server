/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.api;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * 上传选项
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadOptions {
  /** 上传空间 */
  private String space;

  /** 上传目录 */
  private String folder;

  /** 启用插件 */
  @Builder.Default private Set<String> plugins = new HashSet<>();

  /** 文件名策略 */
  private UploadFileNameStrategy nameStrategy;

  private String uploadId;
  private String hash;

  public boolean isMultipartUpload() {
    return StringUtil.isNotBlank(this.uploadId);
  }
}
