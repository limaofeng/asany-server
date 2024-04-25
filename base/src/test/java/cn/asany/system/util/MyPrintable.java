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
