package cn.asany.email.server;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.james.core.MailAddress;
import org.apache.james.core.MaybeSender;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.RcptHook;

public class LoggingRcptHook implements RcptHook {

  @Override
  public HookResult doRcpt(SMTPSession session, MaybeSender sender, MailAddress rcpt) {
    System.out.println("RCPT TO " + rcpt + "with parameters " + sender);
    return HookResult.DECLINED;
  }

  @Override
  public void init(Configuration config) throws ConfigurationException {}

  @Override
  public void destroy() {}
}
