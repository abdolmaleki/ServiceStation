package com.technotapp.servicestation.connection.socket;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
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
    private static final int TIME_OUT = 5000;

    public SocketEngine(String ip, int port) {
        this.mPort = port;
        this.mIp = ip;
    }

    public void sendData(final byte[] request, final ISocketCallback callback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket();
                    mSocket.connect(new InetSocketAddress(mIp, mPort), TIME_OUT);
                } catch (IOException e) {
                    AppMonitor.reportBug(e, "SocketEngin", "connect");
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
                    ParseISO parseISO = new ParseISO(new TransactionDataModel());
                    TransactionDataModel transactionDataModel = parseISO.parseDataItem(tx);
                    callback.onReceiveData(transactionDataModel);



                } catch (Exception e) {
                    closeConnection();
                    callback.onFail();
                    AppMonitor.reportBug(e, "SocketEngin", "sendData");
                }
            }
        };

        new Thread(runnable).start();

    }

    private void closeConnection(){

        try {
            mSocket.shutdownInput();
            mSocket.shutdownOutput();
            mSocket.close();
        } catch (IOException e) {
            AppMonitor.reportBug(e, "SocketEngin", "closeConnection");
        }
    }

}
