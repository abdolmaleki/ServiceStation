package com.technotapp.servicestation.pax.printer;

import com.technotapp.servicestation.pax.printer.printcontent.BalanceContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuyCustomerContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuySellerContent;
import com.technotapp.servicestation.pax.printer.printcontent.DepositContent;

public class PrintFactory {
    public static Printable getPrintContent(String contentName) {
        if (contentName.isEmpty()) {
            return null;
        }
        switch (contentName) {
            case Printable.BALANCE:
                return new BalanceContent();

            case Printable.BUY_CUSTOMER:
                return new BuyCustomerContent();


            case Printable.BUY_SELLER:
                return new BuySellerContent();

            case Printable.DEPOSIT:
                return new DepositContent();

            default:
                return null;
        }
    }
}
