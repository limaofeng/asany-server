/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.message.core;

import cn.asany.message.api.EmailChannelConfig;
import cn.asany.message.api.EmailMessage;
import cn.asany.message.api.MessageChannel;
import cn.asany.message.api.MessageException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailMessageChannel implements MessageChannel<EmailMessage> {

  private final EmailChannelConfig config;
  private final JavaMailSender mailSender;

  public EmailMessageChannel(EmailChannelConfig config) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(config.getHost());
    mailSender.setPort(config.getPort());
    mailSender.setUsername(config.getUsername());
    mailSender.setPassword(config.getPassword());

    // 设置使用SSL
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", config.getProtocol());
    props.put("mail.smtps.auth", config.isAuth() ? "true" : "false");
    props.put("mail.smtps.starttls.enable", config.isStarttlsEnable() ? "true" : "false");
    props.put("mail.smtps.ssl.trust", config.getSslTrust());
    props.put("mail.smtps.ssl.enable", config.isSslEnable());

    this.mailSender = mailSender;
    this.config = config;
  }

  @Override
  @SneakyThrows
  public void send(EmailMessage message) throws MessageException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    InternetAddress fromAddress =
        new InternetAddress(
            config.getFrom(), MimeUtility.encodeText(config.getFromName()), "UTF-8");

    helper.setFrom(fromAddress.toString());
    helper.setTo(parseAddresses(message.getTo()));

    helper.setSubject(message.getSubject());
    helper.setText(message.getText(), isHtml(message.getText()));

    mailSender.send(mimeMessage);
  }

  private static String[] parseAddresses(Set<String> addresses) {
    return addresses.stream()
        .map(address -> createInternetAddress(address).toString())
        .toArray(String[]::new);
  }

  /**
   * 创建一个InternetAddress对象，自动处理只有邮箱或带有姓名的邮箱。
   *
   * @param fullAddress 包含可选姓名和邮箱的字符串，例如："王五<your-email@yourdomain.com>"
   * @return 一个配置好的InternetAddress对象
   */
  @SneakyThrows
  public static InternetAddress createInternetAddress(String fullAddress) {
    int indexOfAngleBracket = fullAddress.indexOf('<');
    if (indexOfAngleBracket != -1) {
      // 分离姓名和邮箱
      String personal = fullAddress.substring(0, indexOfAngleBracket).trim();
      String email =
          fullAddress.substring(indexOfAngleBracket + 1, fullAddress.length() - 1).trim();
      // 对个人名字进行MIME编码
      personal = MimeUtility.encodeText(personal, "UTF-8", null);
      return new InternetAddress(email, personal, "UTF-8");
    } else {
      // 只有电子邮箱地址
      return new InternetAddress(fullAddress.trim());
    }
  }

  public static boolean isHtml(String text) {
    // 正则表达式检查尖括号和常见标签
    Pattern pattern = Pattern.compile(".*<\\s*(\\w+)[^>]*>.*", Pattern.DOTALL);
    return pattern.matcher(text).matches();
  }
}
