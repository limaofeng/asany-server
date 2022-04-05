package cn.asany.storage.data.web;

import cn.asany.storage.data.web.wrapper.UploadLimitRequestWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

public class UploadLimitFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    filterChain.doFilter(new UploadLimitRequestWrapper(request), response);
  }
}
