package cn.asany.storage.data.web.wrapper;

import cn.asany.storage.data.util.BandwidthLimiter;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

public class UploadLimitRequestWrapper extends HttpServletRequestWrapper {

  public UploadLimitRequestWrapper(HttpServletRequest request) {
    super(request);
  }

  @Override
  public Collection<Part> getParts() throws IOException, ServletException {
    // TODO: 底层 Tomcat 实现了, 如果需要限速，需要自己实现解析逻辑
    return super.getParts();
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    return new UploadLimitServletInputStream(super.getInputStream(), new BandwidthLimiter(1024));
  }
}
