package com.technotapp.servicestation.pax.mag;

public interface IMagCardCallback {
    void onFail();

    void onSuccessful(String track1, String track2, String track3);

}
