package com.technotapp.servicestation.pax.mag;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;

import com.pax.dal.entity.TrackData;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.fragment.PinFragment;

public class MagCard {
    static MagCard masterMagCard;
    static MagReadThread magReadThread;
    private String mTrack1;
    private String mTrack2;
    private String mTrack3;
    private Handler mMagHandler;

    private MagCard() {

    }

    public static MagCard getInstance() {
        if (masterMagCard == null) {
            masterMagCard = new MagCard();
        }
        return masterMagCard;
    }

    class MagReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!Thread.interrupted()) {
                if (MagCardHelper.getInstance().isSwiped()) {
                    TrackData trackData = MagCardHelper.getInstance().read();
                    if (trackData != null) {
                        String resStr = "";
                        if (trackData.getResultCode() == 0) {
                            resStr = "خطا در کشیدن کارت";
                            Message.obtain(mMagHandler, 0, resStr).sendToTarget();
                            continue;
                        } else {
                            if ((trackData.getResultCode() & 0x01) == 0x01) {
                                resStr = trackData.getTrack1();
                                Message.obtain(mMagHandler, 1, resStr).sendToTarget();
                            }
                            if ((trackData.getResultCode() & 0x02) == 0x02) {
                                resStr = trackData.getTrack2();
                                Message.obtain(mMagHandler, 2, resStr).sendToTarget();
                            }
                            if ((trackData.getResultCode() & 0x04) == 0x04) {
                                resStr = trackData.getTrack3();
                                Message.obtain(mMagHandler, 3, resStr).sendToTarget();
                            }
                            Message.obtain(mMagHandler, 4, "").sendToTarget();

                        }


                    }
                    break;
                }
                SystemClock.sleep(100);
            }
        }
    }

    public void start(final Context ctx, final IMagCardCallback callback) {

        final AlertDialog dialog = new AlertDialog.Builder(ctx).setTitle("کارتو بکشید ..").show();

        mMagHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                        callback.onFail();
                        dialog.cancel();
                        magReadThread.interrupt();
                        magReadThread = null;

                        break;

                    case 1:
                        mTrack1 = msg.obj.toString();
                        break;

                    case 2:
                        mTrack2 = msg.obj.toString();
                        break;

                    case 3:
                        mTrack3 = msg.obj.toString();
                        break;

                    case 4:
                        submitPinFragment(((Activity) ctx));
                        callback.onSuccessful(mTrack1, mTrack2, mTrack3);
                        magReadThread.interrupt();
                        magReadThread = null;
                        dialog.cancel();
                        break;
                    default:
                        break;
                }
            }


        };

        if (magReadThread == null) {
            magReadThread = new MagReadThread();
            MagCardHelper.getInstance().open();
            MagCardHelper.getInstance().reset();
            magReadThread.start();
        }


    }

    private void submitPinFragment(Activity ctx) {
        try {
            PinFragment pinFragment = new PinFragment();
            pinFragment.show(ctx.getFragmentManager(), "pin");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MagCard", "submitPinFragment");
        }

    }
}
