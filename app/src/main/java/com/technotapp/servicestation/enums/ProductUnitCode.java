package com.technotapp.servicestation.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductUnitCode {
    Digit(1, "عدد"),
    gram(2, "گرم"),
    kilogram(3, "کیلوگرم"),
    vahed(4, "واحد"),
    centimeter(5, "سانتی متر"),
    meter(6, "متر"),
    litr(7, "لیتر"),
    ghete(8, "قطعه");


    String value;
    int key;

    private static Map<Integer, String> map = new HashMap<Integer, String>();

    static {
        for (ProductUnitCode productUnitCodeEnum : ProductUnitCode.values()) {
            map.put(productUnitCodeEnum.key, productUnitCodeEnum.value);
        }
    }

    public static String valueOf(int productUnitKey) {
        return map.get(productUnitKey);
    }

    public static int getKey(String value) {
        for (Map.Entry<Integer, String> obj : map.entrySet()) {
            if (obj.getValue().equals(value)) {
                return obj.getKey();
            }
        }

        return 0;
    }

    ProductUnitCode(int key, String value) {
        this.value = value;
        this.key = key;
    }

}
