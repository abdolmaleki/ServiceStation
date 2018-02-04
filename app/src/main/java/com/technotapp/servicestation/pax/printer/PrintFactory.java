package com.technotapp.servicestation.pax.printer;

import com.technotapp.servicestation.pax.printer.printcontent.BalanceContent;
import com.technotapp.servicestation.pax.printer.printcontent.BalanceNoIconContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuyCustomerContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuyCustomerNoIconContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuyCustomerRatingContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuySellerContent;
import com.technotapp.servicestation.pax.printer.printcontent.BuySellerNoIconContent;
import com.technotapp.servicestation.pax.printer.printcontent.CashContent;
import com.technotapp.servicestation.pax.printer.printcontent.CashNoIconContent;
import com.technotapp.servicestation.pax.printer.printcontent.CashRatingContent;
import com.technotapp.servicestation.pax.printer.printcontent.DepositContent;
import com.technotapp.servicestation.pax.printer.printcontent.DepositNoIconContent;

public class PrintFactory {
    public static Printable getPrintContent(String contentName) {
        if (contentName.isEmpty()) {
            return null;
        }
        switch (contentName) {
            case Printable.BALANCE:
                return new BalanceContent();

            case Printable.BALANCE_NO_ICON:
                return new BalanceNoIconContent();

            case Printable.BUY_CUSTOMER:
                return new BuyCustomerContent();

            case Printable.BUY_CUSTOMER_HAVE_RATE:
                return new BuyCustomerRatingContent();

            case Printable.BUY_CUSTOMER_NO_ICON:
                return new BuyCustomerNoIconContent();

            case Printable.BUY_SELLER:
                return new BuySellerContent();

            case Printable.BUY_SELLER_NO_ICON:
                return new BuySellerNoIconContent();

            case Printable.DEPOSIT:
                return new DepositContent();

            case Printable.DEPOSIT_NO_ICON:
                return new DepositNoIconContent();

            case Printable.CASH:
                return new CashContent();

            case Printable.CASH_HAVE_RATE:
                return new CashRatingContent();

            case Printable.CASH_NO_ICON:
                return new CashNoIconContent();


            default:
                return null;
        }
    }
}
