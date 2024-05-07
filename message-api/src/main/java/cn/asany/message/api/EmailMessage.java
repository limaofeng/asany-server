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
package cn.asany.message.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage implements Message {
  private String from;
  private Set<String> to;
  private String subject;
  private String text;
  private String uri;

  public static class EmailMessageBuilder {

    public EmailMessageBuilder to(String... to) {
      if (this.to == null) {
        this.to = new HashSet<>();
      }
      this.to.addAll(Arrays.asList(to));
      return this;
    }

    public EmailMessageBuilder to(String to) {
      if (this.to == null) {
        this.to = new HashSet<>();
      }
      this.to.add(to);
      return this;
    }
  }
}
