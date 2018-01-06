package com.technotapp.servicestation.connection.socket;

import android.content.Context;
import android.os.Looper;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.pax.iso8583.ParseISO;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class SocketEngine {
    private int mPort;
    private String mIp;
    private Socket mSocket;
    private Context mContext;
    private static final int CONNECTION_TIME_OUT = 10000;
    private static final int READ_TIME_OUT = 10000;
    private TransactionDataModel mTransactionDataModel;


    public SocketEngine(Context ctx, String ip, int port, TransactionDataModel transactionDataModel) {
        this.mPort = port;
        this.mIp = ip;
        mTransactionDataModel = transactionDataModel;
        mContext = ctx;
    }

    public void sendData(final byte[] request, final ISocketCallback callback) {

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////// Check Network Connection
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    Looper.prepare();

                    mSocket = new Socket();
                    mSocket.connect(new InetSocketAddress(mIp, mPort), CONNECTION_TIME_OUT);
                    mSocket.setSoTimeout(READ_TIME_OUT);
                } catch (IOException e) {

                    AppMonitor.reportBug(mContext, e, "SocketEngine", "connect");
                    callback.onFail();

                }

                try {

                    ///////////////////////////////////////////////////////////////////
                    ////////////// Send Data
                    ////////////////////////////////////////////////////////////////////

                    OutputStream socketOutputStream = mSocket.getOutputStream();
                    socketOutputStream.write(request);

                    ///////////////////////////////////////////////////////////////////
                    ////////////// Receive Data
                    ////////////////////////////////////////////////////////////////////

                    byte[] messageByte = new byte[200];
                    DataInputStream in = new DataInputStream(mSocket.getInputStream());
                    int bytesRead = in.read(messageByte);
                    byte[] final_b;
                    final_b = Arrays.copyOf(messageByte, bytesRead);
                    String tx = Converters.bytesToHex(final_b);
                    ParseISO parseISO = new ParseISO(mTransactionDataModel);
                    //mTransactionDataModel= parseISO.parseDataItem(tx);
                    parseISO.parseDataItem(tx);
                    callback.onReceiveData(mTransactionDataModel);


                } catch (Exception e) {
                    closeConnection();
                    callback.onFail();
                    AppMonitor.reportBug(mContext, e, "SocketEngine", "sendData");
                }
            }
        };

        NetworkHelper.isConnectingToInternet(mContext, new NetworkHelper.CheckNetworkStateListener() {
            @Override
            public void onNetworkChecked(boolean isSuccess, String message) {
                if (isSuccess) {
                    new Thread(runnable).start();
                } else {
                    //Todo create alert
                }
            }
        });
    }

    private void closeConnection() {

        try {
            mSocket.shutdownInput();
            mSocket.shutdownOutput();
            mSocket.close();
        } catch (IOException e) {
            AppMonitor.reportBug(mContext, e, "SocketEngine", "closeConnection");
        }
    }

}
