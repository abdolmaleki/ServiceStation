package com.technotapp.servicestation.pax.iso8583;

import android.util.Log;

import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;

import java.util.ArrayList;

import static com.technotapp.servicestation.Infrastructure.Converters.hexToString;
import static java.lang.System.out;

public class ParseISO {

    private TransactionDataModel mTransactionDataModel;

    public ParseISO(TransactionDataModel transactionDataModel) {
        mTransactionDataModel = transactionDataModel;
    }

    private int index = 0;


    public TransactionDataModel parseDataItem(String dump) {


        String bitmap;
        String length = dump.substring(index, 4);

        out.println("length:" + length);
        index += 4;

        String tpdu = dump.substring(index, index + 10);
        index += 10;

        String mti = dump.substring(index, index + 4);

        out.println("mti:" + mti);
        index += 4;

        bitmap = Converters.hexToBin(dump.substring(index, index + 16));
        out.println("bitmap:" + bitmap);
        index += 16;
        String data = dump.substring(index);


        ArrayList<IsoDataItem> isoArray = new ArrayList<>();
        for (int i = 0; i < bitmap.length(); i++) {


            if (bitmap.charAt(i) == '1') {

                if (i == 0) {
                    Log.d("aa", "2nd bitmap:" + dump.substring(index, index + 16));
                    bitmap = bitmap + Converters.hexToBin(dump.substring(index, index + 16));
                    index += 16;

                } else
                    isoArray.add(makeIsoItem((i + 1), dump));
                Log.d("aa", i + 1 + " ");

            }

        }
        new IsoItem(length, mti, bitmap, isoArray);

        return mTransactionDataModel;

    }


    private IsoDataItem makeIsoItem(int type, String dump) {


        switch (type) {
            case 2: {

                int length = Integer.parseInt(dump.substring(index, index + 2));
                index += 2;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                mTransactionDataModel.setPanNumber(data);
                return new IsoDataItem(type, data);
            }
            case 3: {

                int length = 6;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 4: {
                int length = 12;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                mTransactionDataModel.setAmount(data);
                return new IsoDataItem(type, data);
            }
            case 5: {
                int length = 12;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 6: {
                int length = 12;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 7: {
                int length = 10;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                mTransactionDataModel.setDateTimeShaparak(data);
                return new IsoDataItem(type, data);
            }
            case 9: {
                int length = 8;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 10: {
                int length = 8;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 11: {
                int length = 6;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                mTransactionDataModel.setBackTransactionID(data);
                return new IsoDataItem(type, data);
            }
            case 12: {
                int length = 6;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 13: {
                int length = 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 17: {
                int length = 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 25: {
                int length = 2;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 32: {
                //index-=1;
                int length = Integer.parseInt(dump.substring(index, index + 2));
                index += 2;
                //if (length%2!=0)
                // length++;


                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 35: {
                int length = Integer.parseInt(dump.substring(index, index + 2));
                index += 2;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            //////////////////////////
            case 37: {
                int length = 12;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 38: {
                int length = 12;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 39: {
                int length = 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + hexToString(data));
                mTransactionDataModel.setResponseCode(hexToString(data));
                return new IsoDataItem(type, data);
            }
            case 41: {
                int length = 16;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + hexToString(data));
                return new IsoDataItem(type, data);
            }

            ///////

            case 42: {
                int length = 30;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 43: {
                int length = 80;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 44: {
                int length = Integer.parseInt(dump.substring(index, index + 4)) * 2;
                index += 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }


            case 47: {
                int length = Integer.parseInt(dump.substring(index, index + 4)) * 2;
                index += 4;
                String data = dump.substring(index, index + length);
                index += length;
                out.println(type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 48: {
                int length = Integer.parseInt(dump.substring(index, index + 3));
                index += 3;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 49: {
                int length = 6;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 50: {
                int length = 6;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 51: {
                int length = 6;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }


            case 53: {
                int length = Integer.parseInt(dump.substring(index, index + 1)) * 2;
                index += 1;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 54: {
                int length = Integer.parseInt(dump.substring(index, index + 4)) * 2;
                index += 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }


            case 59: {
                int length = Integer.parseInt(dump.substring(index, index + 4)) * 2;
                index += 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }


            case 62: {

                int length = Integer.parseInt(dump.substring(index, index + 4)) * 2;
                index += 4;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 63: {

                int length = 16;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }


            case 64: {

                int length = 8;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                mTransactionDataModel.setMAC(data);
                return new IsoDataItem(type, data);
            }


            case 90: {
                int length = 42;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }
            case 100: {
                int length = Integer.parseInt(dump.substring(index, index + 2));
                index += 2;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

            case 128: {
                int length = 16;
                String data = dump.substring(index, index + length);
                index += length;
                Log.d("parse", type + ":" + data);
                return new IsoDataItem(type, data);
            }

        }

        return null;

    }


}
