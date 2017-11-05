package com.technotapp.servicestation.Infrastructure;

public class Converters {

    private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String hexToString(String hexString) {
        try {
            int len = hexString.length();
            byte[] data = new byte[len / 2];

            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
            }

            String result;

            result = new String(data, "utf-8");
            return result;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "Converters", "hexToString");
            return null;
        }


    }

    public static String bytesToHex(byte[] bytes) {
        try {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "Converters", "bytesToHex");
            return null;
        }
    }

    public static String hexToBin(String hex) {
        try {
            StringBuilder bin = new StringBuilder();
            String binFragment;
            int iHex;
            hex = hex.trim();
            hex = hex.replaceFirst("0x", "");

            for (int i = 0; i < hex.length(); i++) {
                iHex = Integer.parseInt("" + hex.charAt(i), 16);
                binFragment = Integer.toBinaryString(iHex);

                while (binFragment.length() < 4) {
                    binFragment = "0".concat(binFragment);
                }
                bin.append(binFragment);
            }
            return bin.toString();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "Converters", "hexToBin");
            return null;
        }
    }

}
