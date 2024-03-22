package cn.asany.email.client.smtp.interceptor;

import cn.asany.email.client.smtp.Channel;
import cn.asany.email.client.smtp.Channel.ChannelConnector;
import cn.asany.email.client.smtp.Dns;
import cn.asany.email.client.smtp.Interceptor;
import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.Mailbox;
import cn.asany.email.client.smtp.misc.Utils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.SocketFactory;

/**
 * create a transfer channel for mail delivery
 *
 * @see SocketFactory
 * @see Channel
 * @see ChannelConnector
 */
public class ConnectInterceptor implements Interceptor {

  @Nullable private InetAddress serverAddress;

  public ConnectInterceptor(@Nullable InetAddress serverAddress) {
    this.serverAddress = serverAddress;
  }

  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {
    final ChannelConnector connector = chain.client().channelConnector();
    final Dns dns = chain.client().dns();
    final Mail mail = chain.mail();

    Channel channel = null;

    if (serverAddress != null) { // if user specifies an exactly serverOptions address
      Utils.d("connecting to server: address " + serverAddress);
      channel = connector.connect(chain.client(), serverAddress);
    } else { // dns lookup for serverOptions address according to From: header
      Utils.d("no server address specified, DNS query started");
      final Mailbox from = mail.from();
      List<InetAddress> addresses;
      try {
        addresses = dns.lookupByMxRecord(from.host());
      } catch (UnknownHostException e) {
        throw new IOException("can not find any dns mx record address with host: " + from.host());
      }

      for (InetAddress address : addresses) {
        try {
          channel = connector.connect(chain.client(), address);
          break;
        } catch (IOException ignore) {
        }
      }
    }

    if (channel == null) {
      throw new IOException("error connecting to server");
    }

    ((RealInterceptorChain) chain).setChannel(channel);
    Utils.d("server connection established!");
    try {
      chain.proceed(mail);
    } finally {
      // close any socket resource...
      Utils.closeQuietly(channel.sink());
      Utils.closeQuietly(channel.source());
      Utils.closeQuietly(channel.socket());
    }
  }
}
