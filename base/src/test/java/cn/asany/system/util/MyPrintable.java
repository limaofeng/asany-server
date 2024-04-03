package cn.asany.system.util;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

class MyPrintable implements Printable {
  public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
    if (pageIndex > 0) {
      return NO_SUCH_PAGE;
    }
    g.drawString("Hello world!", 100, 100);
    return PAGE_EXISTS;
  }
}
