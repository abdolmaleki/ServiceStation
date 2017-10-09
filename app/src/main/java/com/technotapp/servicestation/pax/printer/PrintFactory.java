package com.technotapp.servicestation.pax.printer;

import com.technotapp.servicestation.pax.printer.printcontent.BalanceContent;

public class PrintFactory {
    public PrintContent getPrintContent(String contentName) {
        if (contentName.isEmpty()) {
            return null;
        }
        switch (contentName) {
            case PrintContent.BALANCE:
                return new BalanceContent();
            default:
                return null;
        }
    }
}
