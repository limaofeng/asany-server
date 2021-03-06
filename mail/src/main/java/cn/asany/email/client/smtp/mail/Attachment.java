package cn.asany.email.client.smtp.mail;

import cn.asany.email.client.smtp.interceptor.TransferSpec;
import cn.asany.email.client.smtp.misc.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.Nullable;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/** a mail attachment */
public class Attachment extends MailBody {

  public static MediaType OCTET_STREAM = MediaType.parse("application/octet-stream");

  final String filename;
  final MediaType mediaType;
  final Source source;
  final String contentDisposition;

  Attachment(String filename, Source source, MediaType mediaType) {
    this.filename = filename;
    this.source = source;
    if (shouldEncodeFilename(filename)) {
      String name = Utils.encodeUtf8B(filename);
      this.mediaType = MediaType.parse(mediaType.toString() + "; name=\"" + name + "\"");
      this.contentDisposition = "attachment; filename=\"" + name + "\"";
    } else {
      this.mediaType = MediaType.parse(mediaType.toString() + "; name=\"" + filename + "\"");
      this.contentDisposition = "attachment; filename=\"" + filename + "\"";
    }
  }

  public String contentDisposition() {
    return contentDisposition;
  }

  public boolean shouldEncodeFilename(String text) {
    return Utils.asciiCharacterCount(text) != text.length();
  }

  @Override
  public MediaType contentType() {
    return mediaType;
  }

  @Override
  public void writeBody(BufferedSink sink, TransferSpec spec) throws IOException {
    BufferedSink encodingSink = chooseEncoding(spec).from(sink, spec.lengthLimit());
    encodingSink.writeAll(source);
    encodingSink.flush();
  }

  @Override
  public Encoding transferEncoding() {
    return Encoding.BASE64; // base64 is transfer efficiently encoding
  }

  public static Attachment create(String filename, Source source, @Nullable MediaType mediaType) {
    if (Utils.isBlank(filename)) throw new IllegalArgumentException("filename = null");
    if (source == null) throw new IllegalArgumentException("source == null");
    if (mediaType == null) mediaType = OCTET_STREAM;
    return new Attachment(filename, source, mediaType);
  }

  public static Attachment create(String filename, File file, @Nullable MediaType mediaType) {
    if (Utils.isBlank(filename)) throw new IllegalArgumentException("filename = null");
    if (mediaType == null) mediaType = OCTET_STREAM;
    Source source;
    try {
      source = Okio.source(file);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file: " + file + "not found");
    }
    return new Attachment(filename, source, mediaType);
  }
}
