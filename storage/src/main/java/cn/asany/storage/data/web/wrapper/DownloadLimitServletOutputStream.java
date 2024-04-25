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
package cn.asany.storage.data.web.wrapper;

import cn.asany.storage.data.util.BandwidthLimiter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;

public class DownloadLimitServletOutputStream extends ServletOutputStream {

  private final BandwidthLimiter limiter;
  private final ServletOutputStream out;

  public DownloadLimitServletOutputStream(ServletOutputStream out, BandwidthLimiter limiter) {
    this.out = out;
    this.limiter = limiter;
  }

  @Override
  public void setWriteListener(WriteListener listener) {
    this.out.setWriteListener(listener);
  }

  @Override
  public void write(@NotNull byte[] b, int off, int len) throws IOException {
    limiter.limitNextBytes(len);
    this.out.write(b, off, len);
  }

  @Override
  public void write(@NotNull byte[] b) throws IOException {
    this.write(b, 0, b.length);
  }

  @Override
  public void write(int b) throws IOException {
    limiter.limitNextBytes();
    this.out.write(b);
  }

  @Override
  public boolean isReady() {
    return out.isReady();
  }

  @Override
  public void flush() throws IOException {
    this.out.flush();
  }

  @Override
  public void close() throws IOException {
    this.out.close();
  }
}
