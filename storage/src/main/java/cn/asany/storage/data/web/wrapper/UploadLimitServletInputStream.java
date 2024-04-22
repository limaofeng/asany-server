package cn.asany.storage.data.web.wrapper;

import cn.asany.storage.data.util.BandwidthLimiter;
import java.io.IOException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.jetbrains.annotations.NotNull;

public class UploadLimitServletInputStream extends ServletInputStream {

  private final ServletInputStream in;
  private final BandwidthLimiter limiter;

  public UploadLimitServletInputStream(ServletInputStream in, BandwidthLimiter limiter) {
    this.in = in;
    this.limiter = limiter;
  }

  @Override
  public boolean isFinished() {
    return in.isFinished();
  }

  @Override
  public boolean isReady() {
    return in.isReady();
  }

  @Override
  public void setReadListener(ReadListener listener) {
    in.setReadListener(listener);
  }

  @Override
  public int read() throws IOException {
    this.limiter.limitNextBytes();
    return this.in.read();
  }

  @Override
  public int read(@NotNull byte[] b) throws IOException {
    return this.read(b, 0, b.length);
  }

  @Override
  public int read(@NotNull byte[] b, int off, int len) throws IOException {
    this.limiter.limitNextBytes(len);
    return in.read(b, off, len);
  }
}
