package cn.asany.email.client.smtp.auth;

import cn.asany.email.client.smtp.Channel;
import cn.asany.email.client.smtp.interceptor.Response;
import cn.asany.email.client.smtp.misc.Base64;
import cn.asany.email.client.smtp.misc.Utils;
import java.io.IOException;
import okio.BufferedSink;
import okio.BufferedSource;

/**
 * AuthMethod represent a serverOptions authentication procedure when serverOptions requires user
 * auth before any mail being sent.
 */
public interface AuthMethod {

  /** @return true if auth succeed */
  boolean auth(Authentication auth, Channel channel) throws IOException;

  AuthMethod AUTH =
      (auth, channel) -> {
        //      Utils.d("login with AUTH");

        final BufferedSink sink = channel.sink();
        final BufferedSource source = channel.source();
        // send auth login
        sink.writeUtf8("AUTH LOGIN").write(Utils.CRLF).flush();
        int code = Response.parseCode(source.readUtf8Line());

        // if server want continue, then send our key
        if (code != 334) return false;
        sink.writeUtf8(Base64.encode(auth.key().getBytes(Utils.ASCII))).write(Utils.CRLF).flush();

        // if server want continue, then send our token
        code = Utils.parseCode(source.readUtf8Line());
        if (code != 334) return false;
        sink.writeUtf8(Base64.encode(auth.token().getBytes(Utils.ASCII))).write(Utils.CRLF).flush();
        code = Response.parseCode(source.readUtf8Line());
        return code == 235;
      };

  AuthMethod PLAIN =
      (auth, channel) -> {
        Utils.d("login with PLAIN");

        final BufferedSink sink = channel.sink();
        final BufferedSource source = channel.source();
        // send auth plain
        sink.writeUtf8("AUTH PLAIN").write(Utils.CRLF).flush();
        int code = Response.parseCode(source.readUtf8Line());
        if (code != 334) return false;

        // if server want continue, then send our plain auth
        sink.writeByte(0 /*ASCII null*/)
            .writeUtf8(auth.key)
            .writeByte(0 /*ASCII null*/)
            .writeUtf8(auth.token)
            .write(Utils.CRLF)
            .flush();
        code = Response.parseCode(source.readUtf8Line());
        return code == 235;
      };

  // TODO implement more auth method here

}
