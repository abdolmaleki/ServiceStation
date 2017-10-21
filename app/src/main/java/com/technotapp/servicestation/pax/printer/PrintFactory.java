package com.technotapp.servicestation.pax.printer;

import com.technotapp.servicestation.pax.printer.printcontent.BalanceContent;

public class PrintFactory {
    public static Printable getPrintContent(String contentName) {
        if (contentName.isEmpty()) {
            return null;
        }
        switch (contentName) {
            case Printable.BALANCE:
                return new BalanceContent();
            default:
                return null;
        }
    }
}
