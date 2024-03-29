package com.technotapp.servicestation.application;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class Constant {

    public static class Pax {
        public static final String SERVER_IP = "87.236.214.176";
        public static final int SERVER_PORT = 8887;
        public static final String API_BASE_URL = "http://87.236.214.176:3400/";
        public static final String PICTURE_BASE_URL = "http://87.236.214.176:8000/POS/";
        public static final String DEFUALT_URL_ACTION_PAGE = "http://www.google.com";
        public static final String TYPE_POS = "14";


        public static class Printer {
            public static final int FONT_BIG = 32;
            public static final int FONT_NORMAL = 24;
            public static final int FONT_SMALL = 20;
        }
    }

    public static class Session {
        public static final String IS_FIRST_RUN = "isFirstRun";
        public static final String MOBILE = "mobile";
        public static final String FIRST_NAME = "firstname";
        public static final String LASTNAME = "lastname";
        public static final String USER_NAME = "user.name";
        public static final String GENDER = "gender";
        public static final String APP_VERSION = "app.version";
        public static final String LAST_VERSION = "last.version";
        public static final String IS_NEW_MENU = "is.new.menu";
        public static final String BIRTHDATE = "birthdate";
        public static final String EMAIL = "email";
        public static final String ADDRESS = "address";
        public static final String TERMINAL_ID = "terminal.id";
        public static final String MENU_CATEGORY = "menu.category";
        public static final String ENID_MERCHANT = "nid.merchant";
        public static final String SHOP_NAME = "shop.name";
        public static final String SCORE = "score";
        public static final String IS_CUSTOMER = "is_customer";
        public static final String TOKEN_ID = "token_id";
        public static final String BASE_URL = "base.url";
        public static final String HASH_ID = "hash_id";
        public static final String DESCRIPTION = "description";
        public static final String ECONOMIC_CODE = "economic_code";
        public static final String REGISTER_ID = "register_id";
        public static final String REGISTER_DATE = "register_date";
        public static final String TEL = "tel";
        public static final String SHPCATEGORY = "shopcategory";
        public static final String MANAGERNAME = "manager.name";
        public static final String FAX = "fax";
        public static final String PIN_KEY = "pin.key";
        public static final String HAS_DISCOUNT = "has.discount";
        public static final String DISCOUNT = "discount";
    }

    public static class Setting {
        public static final String IS_TURNING_ENABLED = "is.turning.enabled";
        public static final String IS_SELLER_PRINT_ENABLED = "is.seller.print.enabled";
        public static final String IS_CUSOTMER_PRINT_ENABLED = "is.customer.print.enabled";
        public static final String LAST_TURNING_DATE = "last.turning.date";
        public static final String TURN_RATE = "turn_rate";
    }

    public static class RequestMode {
        public static final int BALANCE = 0;
        public static final int DEPOSIT = 1;
        public static final int BUY = 2;
        public static final int CASH_PAYMENT = 3;

    }


    public static class TransactionResponseCode {
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
        public static int Information = 0;
        public static int Warning = 1;
        public static int Error = 2;
        public static int Success = 3;

    }

    public static class Key {
        public static final String CURRENT_FRAGMENT = "current_fragment";
        public static final String MENU_PACKAGE = "menu.package";
        public static final String MENU_ID = "menu.id";
        public static final String ACTION_URL = "action.url";
        public static final String PRODUCT_ID = "product.id";
        public static final String FACTOR_ID = "factor.id";
        public static final String EXIT = "exit";
        public static final String USER_ROLE = "user.role";
        public static final String PAYMENT_TYPE_LIST = "payment.type.list";
        public static final String FACTOR_TOTAL_PRICE = "factor.total.price";
        public static final String PRINT_BITMAP = "print.bitmap";
        public static final String MENU_ADAPTER_MODEL = "menu.adapter.model";
        public static final String CONTEXT = "context";
        public static final String UPDATE_MENU = "update.menu";
        public static final String PAYMENT_AMOUNT = "payment.amount";
        public static final String CARD_NUMBER = "card.number";
        public static final String ACCOUNT_PASSWORD = "account.password";
        public static final String IS_ACTIVE_PIN = "is.active.pin";
        public static final String SERVICE_TYPE = "service.type";
        public static final String SERVICE_DTO = "service.dto";
        public static final String ACTIVE_PIN = "active.pin";
        public static final String HAS_CASH_PAYMENT = "has.cash.payment";
        public static final String HASH_ID = "hash.id";
        public static final java.lang.String LITR = "LITR";
        public static final java.lang.String DISPENSER = "dispenser";
        public static final String OPERATOR_TYPE = "operator.type";
        public static final String SIMCARD_TYPE = "simcard.type";
        public static final String SUPPORT_TOKEN_ID = "support.token.id";
        public static final String SUPPORT_TOKEN_ID_EXPIRE_TIME = "support.token.expire";
        public static final String SUPPORT_IMAGE = "support.image";
        public static final String SUPPORT_IMAGE_URL = "support.image.url";
        public static final String SUPPORTER_NAME = "support.name";
        public static final String IS_USE_SCORE = "is.use.score";
        public static final String NEED_SHOW_DISCOUNT_OPTION = "need.show.discount.option";
        public static String ACCOUNT_NUMBER = "account_number";
    }

    public static class MenuAction {
        public static final String CHARGE = "charge_service";
        public static final String CARDSERVICE = "card_service";
        public static final String INSURANCE = "insurance_service";
        public static final String TICKET = "ticket_service";
        public static final String SIMCARD = "simcard_service";
        public static final String RECEIPT = "receipt_service";
        public static final String INTERNET_PACKAGE = "internet_package_service";
        public static final String ANSAR = "ansar_service";
        public static final String CARD_TO_CARD = "card_to_card_service";
        public static final String DEPOSIT = "deposit_service";
        public static final String BUY = "buy_service";
        public static final String BALANCE = "balance_service";
        public static final String KHALAFI = "khalafi_service";
        public static final String PURCHASE = "purchase_service";
        public static final String QR_READER = "merchant_qr_code";
        public static final String GAS_STATION = "gas_station_service";
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
            public static final int TERMINAL_INFO = 2;
            public static final int LOG_INFO = 3;
            public static final int ADD_UPDATE_PRODUCT = 4;
            public static final int SEARCH_PRODUCT = 5;
            public static final int SUBMIT_FACTOR = 6;
            public static final int BYE_CHARGE = 7;
            public static final int GET_VERSION = 8;
            public static final int SEARCH_TRANSACTION = 9;
            public static final int SEARCH_Factor = 10;
            public static final int EDIT_SHOP_INFO = 11;
            public static final int SUBMIT_SUGGESTION = 12;
            public static final int GET_CUSTOMER_ACCOUNT = 13;
            public static final int BILL_PAYMENT = 14;
            public static final int INSERT_TRANSACTION = 15;
            public static final int LOGIN_SETTING = 16;
            public static final int GET_INTERNET_PACKAGE = 17;
            public static final int BUY_INTERNET_PACKAGE = 18;
            public static final int SEARCH_PRODUCT_SUPPORT = 19;
            public static final int SEARCH_TRANSACTION_SUPPORT = 20;
            public static final int SEARCH_Factor_SUPPORT = 21;
            public static final int GET_CUSTOMER_ACCOUNTS_AND_VERIFICATION = 22;
        }

        public static class ErrorCode {
            public static final int Successfull = 8;
            public static final int CheckTokenValidation = 1;
        }

        public static class TransactionErrorCode {
            public static final String Successfull = "00";
        }


        public static class ResultCode {
            public static final String Successfull = "0";
        }

    }

    public static class Encryption {
        public static final String RSA_KEY = "pkT+gaenqqIXUzNRdoj+xqdqJXgAHgpp2ZNdaGfV4wUWJ8KTAYZlXP0cvgbwlu94M7cvLjQ3P6rn4U+VirsskSjnburdNGDTb2lf2mnaIQs9M7npb0SVaY0aWyZiuJTDjOxbgegYxA6tNDw66ewpIWMavyOyke59xmHUFqUnylULSpdmKkAuxbjBkxkRjzqsWER6HdRo6LJr4LjNYCVzyDurorFkinvyawBmO5Wi0AknkAQxQbC7sFlR6jLPoHVEL259TEq3vogsuQVXZfiPB0WmwRJtKBx++PAXR7WtJghVc7Ub/48x4I3zgL8yNtxuMqh8n0mUw6cBNeXgMJdf2Q==";
        //public static final String RSA_KEY = "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBMk6ORZQhtrQrbfIQAGTNsQH7kLd2Tf7Z5WUL5DBB7s6x4xaxBFYMBUeWN2DhsGqlteLFEa8G1gx3EG5FnLURzSSbvbcGLpFBlAJnqSsibmh+TyyZVMrqoT/vR8lkO1Jba/CXhWfFfkp4PvoXyuo8ygTbd8Vy9A5p6K4mOZEupHoeXTgSouT3LsV2s344UKie0scJKvjDktEiDf0q1ZWE1R5rL+Dvl7CnzqYq8Iv/SennmFfSqaZujqx3bFTXfiMb0iJ6CjEZpKD40otGkvEFDaAg4a7FvdFVE7LuWX6WFZclPE/lyxhUblJ/70CeWZNKbX+mpTnD7My0ea126HKLFAgMBAAE=";

    }

    public static class TransactionType {
        public static final String BALANCE = "22";
        public static final String BUY = "00";
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

    public static class Valuse {
        public static final int MAX_ROW_PRODUCT = 60000;
    }

    public static class RequestCode {
        public static final int TRANSACTION_RESULT_CODE = 50;
        public static final int WALLET_PAYMENT = 51;
        public static final int QR_SCANNER = 49374;
        public static final int KEYPAD_AMOUNT = 52;
        public static final int FACTOR = 20010;
    }
}
