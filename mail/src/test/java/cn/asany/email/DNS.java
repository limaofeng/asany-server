package cn.asany.email;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DNS {
  public static void main(String[] args) {
    try {
      Lookup lookup = new Lookup("asany.cn", Type.ANY);
      Record[] records = lookup.run();

      if (lookup.getResult() == Lookup.SUCCESSFUL) {
        String responseMessage = null;
        String listingType = null;
        for (Record record : records) {
          if (record instanceof TXTRecord) {
            TXTRecord txt = (TXTRecord) record;
            for (Object o : txt.getStrings()) {
              responseMessage += (String) o;
            }
            System.out.println("TXRecord " + responseMessage);
          } else if (record instanceof ARecord) {
            listingType = ((ARecord) record).getAddress().getHostAddress();
            System.out.println("ARecord address : " + listingType);
          }
        }
      }
    } catch (TextParseException e) {
      e.printStackTrace();
    }
  }
}
