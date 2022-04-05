package cn.asany.storage.data.web.wrapper;

import cn.asany.storage.data.util.BandwidthLimiter;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import org.jetbrains.annotations.NotNull;

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
