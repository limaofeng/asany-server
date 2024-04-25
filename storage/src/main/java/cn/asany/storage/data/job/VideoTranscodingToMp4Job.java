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
package cn.asany.storage.data.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Deprecated
@Slf4j
public class VideoTranscodingToMp4Job implements Job {

  @Override
  public void execute(JobExecutionContext context) {
    String infile = "";
    String outfile = "";
    String mp4 = "ffmpeg -i " + infile + " -vcodec libx264 -profile:v Main " + outfile;
    try {
      Process proc = Runtime.getRuntime().exec(mp4);
      InputStream stderr = proc.getErrorStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(stderr));
      String line;
      while ((line = br.readLine()) != null) {
        log.debug("Process exitValue:" + line);
      }
      int exitVal = proc.waitFor();
      StreamUtil.closeQuietly(stderr);
      log.debug("Process exitValue:" + exitVal);
    } catch (IOException | InterruptedException e) {
      log.error(e.getMessage(), e);
    }
  }
}
