package cn.asany.email.client.smtp;

import cn.asany.email.client.smtp.auth.AuthMethod;
import java.util.List;

public interface ServerOptions {

  boolean eightBitMimeSupported();

  boolean startTlsSupported();

  boolean pipeLiningSupported();

  List<AuthMethod> authMethods();
}
