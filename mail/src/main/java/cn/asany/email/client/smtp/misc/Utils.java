package cn.asany.email.client.smtp.misc;

import cn.asany.email.client.smtp.MailIdGenerator;
import cn.asany.email.client.smtp.Version;
import cn.asany.email.client.smtp.interceptor.SmtpBody;
import cn.asany.email.client.smtp.interceptor.SmtpHeader;
import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.mail.Mail;
import cn.asany.email.client.smtp.mail.SmtpDate;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public final class Utils {

  public static final Charset ASCII = StandardCharsets.US_ASCII;
  public static final Charset UTF8 = StandardCharsets.UTF_8;
  public static final int SP = 20;
  public static final int CR = 13;
  public static final int LF = 10;
  public static final int DOT = 45;

  /** line end in smtp protocol */
  public static final byte[] CRLF = {'\r', '\n'};

  public static void closeQuietly(Closeable c) {
    if (c == null) return;
    try {
      c.close();
    } catch (IOException ignored) {
    }
  }

  public static long checkNotNegative(long n) {
    if (n < 0) throw new IllegalArgumentException(n + " < 0");
    return n;
  }

  public static ThreadFactory makeThreadFactory(final String name, final boolean daemon) {
    return r -> {
      Thread thread = new Thread(r, name);
      thread.setDaemon(daemon);
      return thread;
    };
  }

  public static List<String> readEachLine(BufferedSource source) throws IOException {
    List<String> result = new ArrayList<>();
    String line;
    do {
      line = source.readUtf8Line();
      result.add(line);
    } while (line != null && line.charAt(3) != '=');
    return result;
  }

  public static String readAll(BufferedSource source) throws IOException {
    StringBuilder builder = new StringBuilder();
    String line;
    do {
      line = source.readUtf8Line();
      builder.append(line);
      builder.append("\r\n");
    } while (line != null && line.charAt(3) != '=');
    return builder.toString();
  }

  public static int parseCode(String response) {
    if (response == null) return -1;
    if (response.length() < 3) return -1;
    try {
      return Integer.parseInt(response.substring(0, 3));
    } catch (NumberFormatException nfe) {
      return -1;
    }
  }

  public static boolean isBlank(String text) {
    return text == null || text.length() == 0;
  }

  public static int asciiCharacterCount(String text) {
    int count = 0;
    char ch;
    for (int i = 0; i < text.length(); i++) {
      ch = text.charAt(i);
      if (ch <= 127) count++;
    }
    return count;
  }

  public static String encodeUtf8B(String text) {
    return "=?UTF-8?B?" + Base64.encode(text.getBytes(UTF8)) + "?=";
  }

  public static MailContent toFullContent(Mail mail, TransferSpec spec) throws IOException {
    ByteArrayOutputStream _header = new ByteArrayOutputStream();
    ByteArrayOutputStream _body = new ByteArrayOutputStream();
    new ByteArrayOutputStream();
    final BufferedSink header = Okio.buffer(Okio.sink(_header));
    final BufferedSink body = Okio.buffer(Okio.sink(_body));
    Mail.Builder builder = mail.newBuilder();

    MailIdGenerator idGenerator = MailIdGenerator.DEFAULT;

    if (mail.headers().get("X-Mailer") == null) {
      builder.addHeader("X-Mailer", Version.versionText());
    }
    if (mail.headers().get("Message-Id") == null) {
      builder.addHeader("Message-Id", idGenerator.generate(mail));
    }
    if (mail.headers().get("Date") == null) {
      builder.addHeader("Date", SmtpDate.format(new Date()));
    }
    if (mail.headers().get("MIME-Version") == null) {
      builder.addHeader("MIME-Version", "1.0");
    }
    if (mail.headers().get("Content-Type") == null && mail.body() != null) {
      builder.addHeader("Content-Type", mail.body().contentType().toString());
    }

    mail = builder.buildNoCheck();

    SmtpHeader.writeAllHeaders(header, mail.headers(), spec);

    SmtpHeader.writeMailbox(header, spec, "From", Collections.singletonList(mail.from()));
    SmtpHeader.writeMailbox(header, spec, "To", mail.recipients());
    SmtpHeader.writeMailbox(header, spec, "Cc", mail.cc());
    SmtpHeader.writeMailbox(header, spec, "Bcc", mail.bcc());

    header.write(Utils.CRLF);
    header.flush();

    if (mail.body() != null) {
      SmtpBody.writeBodies(body, mail.body(), spec);
      // body.writeUtf8(".\r\n");
    }

    body.flush();

    return MailContent.of(
        new ByteArrayInputStream(_header.toByteArray()),
        new ByteArrayInputStream(_body.toByteArray()));
  }

  /*   log  */

  public static void i(String msg) {
    logInternal("INFO", msg);
  }

  public static void d(String msg) {
    logInternal("DEBUG", msg);
  }

  public static void e(String msg) {
    logInternal("ERROR", msg);
  }

  @Data
  @AllArgsConstructor
  public static class MailContent {
    private InputStream header;
    private InputStream body;

    public static MailContent of(InputStream header, InputStream body) {
      return new MailContent(header, body);
    }
  }

  private static void logInternal(String tag, String msg) {
    System.out.print(tag);
    System.out.print(" ------> ");
    System.out.println(msg);
  }
}
