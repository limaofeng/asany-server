package cn.asany.email.client.smtp.interceptor;

import cn.asany.email.client.smtp.*;
import cn.asany.email.client.smtp.mail.Mail;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;

public class RealInterceptorChain implements Interceptor.Chain {

  private List<Interceptor> interceptors;
  private Session session;
  private int index;

  // lazy settle the channel will be available only ConnectInterceptor intercepted!
  private Channel channel;

  private ServerOptions serverOptions;
  private TransferSpec spec;

  private Mail mail;

  public RealInterceptorChain(Session session, List<Interceptor> interceptors) {
    this.interceptors = interceptors;
    this.session = session;
    this.index = 0;
  }

  @Override
  public void proceed(@Nonnull Mail mail) throws IOException {
    this.mail = mail;
    if (index < interceptors.size()) {
      final Interceptor i = interceptors.get(index++);
      i.intercept(this);
    }
  }

  @Override
  @Nonnull
  public Mail mail() {
    return mail;
  }

  @Override
  public SmtpClient client() {
    return session.client();
  }

  public Channel channel() {
    return channel;
  }

  // this should only be called by ConnectInterceptor
  // before any Command
  public final void setChannel(Channel channel) {
    this.channel = channel;
  }

  @Override
  public ServerOptions serverOptions() {
    return serverOptions;
  }

  @Override
  public TransferSpec transferSpec() {
    return spec;
  }

  public final void setTransferSpec(TransferSpec spec) {
    this.spec = spec;
  }

  public final void setServerOptions(ServerOptions serverOptions) {
    this.serverOptions = serverOptions;
  }
}
