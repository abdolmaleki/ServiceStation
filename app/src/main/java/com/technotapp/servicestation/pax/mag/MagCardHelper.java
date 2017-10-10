package com.technotapp.servicestation.pax.mag;

import com.pax.dal.IDAL;
import com.pax.dal.IMag;
import com.pax.dal.entity.TrackData;
import com.pax.dal.exceptions.MagDevException;
import com.technotapp.servicestation.Infrastructure.GetObj;
import com.technotapp.servicestation.Infrastructure.TestLog;

public class MagCardHelper extends TestLog {

    private static MagCardHelper magTester;

    private IMag iMag;
    private static IDAL dal;

    private MagCardHelper(IDAL dal) {
        iMag = GetObj.getDal().getMag();
        this.dal = dal;
    }

    public static MagCardHelper getInstance() {
        if (magTester == null) {
            magTester = new MagCardHelper(dal);

        }
        return magTester;
    }

    public void open() {
        try {
            iMag.open();
            logTrue("open");
        } catch (MagDevException e) {
            e.printStackTrace();
            logErr("open", e.toString());
        }
    }

    public void close() {
        try {
            iMag.close();
            logTrue("close");
        } catch (MagDevException e) {
            e.printStackTrace();
            logErr("close", e.toString());
        }
    }

    // Reset magnetic stripe card reader, and clear buffer of magnetic stripe card.
    public void reset() {
        try {
            iMag.reset();
            logTrue("reSet");
        } catch (MagDevException e) {
            e.printStackTrace();
            logErr("reSet", e.toString());
        }
    }

    // Check whether a card is swiped
    public boolean isSwiped() {
        boolean b = false;
        try {
            b = iMag.isSwiped();
            // logTrue("isSwiped");
        } catch (MagDevException e) {
            e.printStackTrace();
            logErr("isSwiped", e.toString());
        }
        return b;
    }

    public TrackData read() {
        try {
            TrackData trackData = iMag.read();
            logTrue("read");
            return trackData;
        } catch (MagDevException e) {
            e.printStackTrace();
            logErr("read", e.toString());
            return null;
        }
    }

}
