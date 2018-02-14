package com.technotapp.servicestation.pax.mag;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.pax.dal.entity.TrackData;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.PinFragment;
import com.technotapp.servicestation.fragment.SweepingCardDialogFragment;

public class MagCard {
    private static MagCard masterMagCard;
    private static MagReadThread magReadThread;
    private String mTrack1;
    private String mTrack2;
    private String mTrack3;
    private Handler mMagHandler;
    private SweepingCardDialogFragment mSweepingCardDialogFragment;

    private MagCard() {

    }

    public static MagCard getInstance() {
        if (masterMagCard == null) {
            masterMagCard = new MagCard();
        }
        return masterMagCard;
    }

    private class MagReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (!Thread.interrupted()) {
                try {
                    if (MagCardHelper.getInstance().isSwiped()) {
                        TrackData trackData = MagCardHelper.getInstance().read();
                        if (trackData != null) {
                            String resStr;
                            if (trackData.getResultCode() == 0) {
                                resStr = Resources.getSystem().getString(R.string.MagCard_error_SweepCard);
                                Message.obtain(mMagHandler, 0, resStr).sendToTarget();
                                continue;
                            } else {

                                mTrack1 = "";
                                mTrack2 = "";
                                mTrack3 = "";

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

                } catch (Exception e) {
                    Message.obtain(mMagHandler, 5, "").sendToTarget();
                }
            }
        }
    }

    public void start(final Context ctx, final IMagCardCallback callback) {

        showDialog(ctx);

        mMagHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0:
                        callback.onFail();
                        hideDialog();
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

                    case 4: // successful finish read
                        callback.onSuccessful(mTrack1, mTrack2, mTrack3);
                        hideDialog();
                        break;

                    case 5:// crash for bad read card
                        Helper.alert(ctx, "خطا در کشیدن کارت", Constant.AlertType.Error);
                        hideDialog();
                        break;

                    case 6:// card sweeping time-outed
                        hideDialog();
                        break;

                    default:
                        break;
                }
            }


        };

        if (magReadThread == null) {
            try {
                magReadThread = new MagReadThread();
                MagCardHelper.getInstance().open();
                MagCardHelper.getInstance().reset();
                magReadThread.start();

            } catch (Exception e) {
                AppMonitor.reportBug(ctx, e, "MagCard", "start");
            }

        }
    }

    private void submitPinFragment(Activity ctx) {
        try {
            PinFragment pinFragment = new PinFragment();
            pinFragment.show(ctx.getFragmentManager(), "pin");
        } catch (Exception e) {
            AppMonitor.reportBug(ctx, e, "MagCard", "submitPinFragment");
        }

    }

    private void showDialog(Context ctx) {

        try {
            mSweepingCardDialogFragment = new SweepingCardDialogFragment();
            mSweepingCardDialogFragment.show((Activity) ctx, new SweepingCardDialogFragment.OnSweepDialogListener() {
                @Override
                public void onCancelOrTimeout() {
                    Message.obtain(mMagHandler, 6, "").sendToTarget();
                }
            });

        } catch (Exception e) {
            AppMonitor.reportBug(ctx, e, "MagCard", "showDialog");
        }
    }

    private void hideDialog() {
        if (magReadThread != null && !magReadThread.isInterrupted()) {
            magReadThread.interrupt();
            magReadThread = null;
        }

        if (mSweepingCardDialogFragment != null && mSweepingCardDialogFragment.isVisible()) {
            mSweepingCardDialogFragment.interruptSweepCard();
            mSweepingCardDialogFragment.dismiss();
        }
    }
}
