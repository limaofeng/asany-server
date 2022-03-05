package cn.asany.email.client.smtp;

import cn.asany.email.client.smtp.interceptor.BridgeInterceptor;
import cn.asany.email.client.smtp.interceptor.ProtocolInterceptor;
import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.mail.Mail;
import java.io.IOException;

/**
 * @see BridgeInterceptor
 * @see cn.asany.email.client.smtp.interceptor.ConnectInterceptor
 * @see cn.asany.email.client.smtp.interceptor.AuthInterceptor
 * @see ProtocolInterceptor
 */
public interface Interceptor {

  void intercept(Chain chain) throws IOException;

  interface Chain {

    void proceed(Mail mail) throws IOException;

    Mail mail();

    SmtpClient client();

    Channel channel();

    ServerOptions serverOptions();

    TransferSpec transferSpec();
  }
}
