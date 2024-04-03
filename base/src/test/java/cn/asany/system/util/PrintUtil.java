package cn.asany.system.util;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

@Slf4j
public class PrintUtil {

  @Test
  public void listPrintService() {
    PrintService service = PrintServiceLookup.lookupDefaultPrintService();
    if (service != null) {
      log.info("DefaultPrintService:" + service.getName());
    }

    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
    for (PrintService item : services) {
      log.info("PrintService:" + item.getName());
    }
  }

  public static PrintService getPrintService(String name) {
    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
    for (PrintService item : services) {
      if (item.getName().equals(name)) {
        return item;
      }
    }
    return null;
  }

  @Test
  public void print() throws PrinterException {
    PrintService service = getPrintService("192.168.5.22");

    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintService(service);
    job.setPrintable(new MyPrintable());
    job.print();
  }

  @Test
  public void socketPrint() {
    String printerIP = "192.168.1.100"; // 打印机的IP地址
    int port = 9100; // 打印机监听的端口

    try (Socket socket = new Socket(printerIP, port);
        OutputStream out = socket.getOutputStream()) {
      String document = "Hello, this is a test print.\n"; // 要打印的内容
      // 发送文本数据到打印机
      out.write(document.getBytes());
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void IPPPrintService() {
    String ippURL = "http://192.168.1.100:631/ipp/print"; // IPP打印机的URL
    File fileToPrint = new File("path/to/document.pdf"); // 打印文档的路径

    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpPost post = new HttpPost(ippURL);
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.addBinaryBody(
          "file", fileToPrint, ContentType.APPLICATION_OCTET_STREAM, fileToPrint.getName());

      post.setEntity(builder.build());
      client.execute(post);
      // 处理响应...
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
