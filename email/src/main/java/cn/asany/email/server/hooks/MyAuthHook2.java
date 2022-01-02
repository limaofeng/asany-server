package cn.asany.email.server.hooks;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.AuthHook;
import org.apache.james.protocols.smtp.hook.HookResult;

/** @author limaofeng */
public class MyAuthHook2 implements AuthHook {

  @Override
  public HookResult doAuth(SMTPSession session, String username, String password) {
    session.setUser("1");
    return HookResult.OK;
  }

  @Override
  public void init(Configuration config) throws ConfigurationException {
    System.out.println(config);
  }

  @Override
  public void destroy() {
    System.out.println("destroy");
  }
}
