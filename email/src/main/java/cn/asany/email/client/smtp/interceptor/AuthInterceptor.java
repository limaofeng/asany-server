package cn.asany.email.client.smtp.interceptor;

import cn.asany.email.client.smtp.Interceptor;
import cn.asany.email.client.smtp.ServerOptions;
import cn.asany.email.client.smtp.auth.AuthMethod;
import cn.asany.email.client.smtp.auth.Authentication;
import cn.asany.email.client.smtp.misc.Utils;
import java.io.IOException;
import java.util.List;

public class AuthInterceptor implements Interceptor {
  private final Authentication auth;

  public AuthInterceptor(Authentication auth) {
    this.auth = auth;
  }

  @Override
  public void intercept(Chain chain) throws IOException {
    final ServerOptions serverOptions = chain.serverOptions();

    AuthMethod authMethod = chooseAuthMethod(serverOptions);
    if (authMethod == null) throw new IOException("AuthMethod all unavailable");

    boolean succeed = authMethod.auth(auth, chain.channel());
    if (!succeed) throw new IOException("Authentication failed");
    Utils.d("authentication succeed!");
    chain.proceed(chain.mail());
  }

  protected AuthMethod chooseAuthMethod(ServerOptions options) {
    final List<AuthMethod> methods = options.authMethods();
    if (methods.contains(AuthMethod.AUTH)) {
      return AuthMethod.AUTH;
    }
    if (methods.contains(AuthMethod.PLAIN)) {
      return AuthMethod.PLAIN;
    }
    return methods.isEmpty() ? AuthMethod.AUTH : methods.get(0);
  }
}
