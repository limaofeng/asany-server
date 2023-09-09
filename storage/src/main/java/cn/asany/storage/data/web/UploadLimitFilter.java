package cn.asany.storage.data.web;

import cn.asany.storage.data.web.wrapper.UploadLimitRequestWrapper;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
