package com.technotapp.servicestation.application;

public class Constant {

    public static class Pax {
        public static final String TEST = "test";
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
     public static final String CURRENT_FRAGMENT = "current_fragment" ;
    }
    public static class MainItem {
        public static final String CHARGE = "charge_service";
        public static final String CARDSERVICE = "card_service";
        public static final String INSURANCE = "insurance_service";

    }






}
