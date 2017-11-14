package com.technotapp.servicestation.application;

public class Constant {

    public static class Pax {
        public static final String SERVER_IP = "87.236.214.176 ";
        public static final int SERVER_PORT = 8887;
        public static final String API_BASE_URL = "http://87.236.214.176:3400/";


        public static class Printer {
            public static final int FONT_BIG = 32;
            public static final int FONT_NORMAL = 24;
            public static final int FONT_SMALL = 20;
        }
    }

    public static class Session {
        public static final String IS_FIRST_RUN = "isFirstRun";

    }

    public static class RequestMode {
        public static final int BALANCE = 0;
        public static final int DEPOSIT = 1;
        public static final int BUY = 2;

    }


    public static class ResponseCode {
        public static final String SUCCESS = "00";
    }

    public static class Fonts {
        public static final String BTITR = "irsans";
        public static final String IRSANS = "irsans";
        public static final String IRSANSM = "irsans";
        public static final String IRSANSUL = "irsans";
        public static final String IRSANSB = "irsans";
        public static final String ZAR = "irsans";
    }

    public static class AlertType {
        public static int Warning = 1;
        public static int Error = 2;
        public static int Done = 3;
        public static int Information = 0;

    }

    public static class Key {
        public static final String CURRENT_FRAGMENT = "current_fragment";
        public static final String MENU_PACKAGE = "menu.package";
        public static final String MENU_ACTION = "menu.action";
    }

    public static class MenuAction {
        public static final String CHARGE = "charge_service";
        public static final String CARDSERVICE = "card_service";
        public static final String INSURANCE = "insurance_service";
        public static final String TICKET = "ticket_service";
        public static final String SIMCARD = "simcard_service";
        public static final String RECEIPT = "receipt_service";
        public static final String INTERNET = "internet_package_service";
        public static final String ANSAR = "ansar_service";
        public static final String CARD_TO_CARD = "card_to_card_service";
        public static final String DEPOSIT = "deposit_service";
        public static final String BUY = "buy_service";
        public static final String BALANCE = "balance_service";

    }

    public static class Operator {
        public static final byte RIGHTEL = 0;
        public static final byte IRANCELL = 1;
        public static final byte HAMRAHEAVAL = 2;
        public static final byte TALIYA = 3;

    }

    public static class Api {
        public static class Type {
            public static final int TERMINAL_LOGIN = 0;
            public static final int CheckTokenValidation = 1;
        }

        public static class ErrorCode {
            public static final int Successfull = 8;
            public static final int CheckTokenValidation = 1;
        }

    }

    public static class Encryption {
        public static final String RSA_KEY = "pkT+gaenqqIXUzNRdoj+xqdqJXgAHgpp2ZNdaGfV4wUWJ8KTAYZlXP0cvgbwlu94M7cvLjQ3P6rn4U+VirsskSjnburdNGDTb2lf2mnaIQs9M7npb0SVaY0aWyZiuJTDjOxbgegYxA6tNDw66ewpIWMavyOyke59xmHUFqUnylULSpdmKkAuxbjBkxkRjzqsWER6HdRo6LJr4LjNYCVzyDurorFkinvyawBmO5Wi0AknkAQxQbC7sFlR6jLPoHVEL259TEq3vogsuQVXZfiPB0WmwRJtKBx++PAXR7WtJghVc7Ub/48x4I3zgL8yNtxuMqh8n0mUw6cBNeXgMJdf2Q==";

    }

    public static class PayBill {
        public static class Organization {
            public static final byte WATER = 1;
            public static final byte ELECTRICAL = 2;
            public static final byte GAS = 3;
            public static final byte PHONE = 4;
            public static final byte MOBILE = 5;
            public static final byte TAX_OF_MUNICIPALITY = 6;
            public static final byte TAX = 8;
            public static final byte TRAFFIC_CRIME = 9;
        }


    }
}
