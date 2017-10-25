package com.technotapp.servicestation.application;

public class Constant {

    public static class Pax {
        public static final String TEST = "test";
        public static final String SERVER_IP = "87.236.214.176 ";
        public static final int SERVER_PORT = 8887;

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


}
