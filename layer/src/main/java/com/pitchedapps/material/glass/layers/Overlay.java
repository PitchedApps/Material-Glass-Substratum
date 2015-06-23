package com.pitchedapps.material.glass.layers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.lovejoy777.rroandlayersmanager.IOperation;



/**
 * Created by Niklas on 02.06.2015.
 */
public class Overlay extends Service {


    public void onStart(Intent intent, int startId) {
        super.onStart( intent, startId );
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return mulBinder;
    }

    private final IOperation.Stub mulBinder =
            new IOperation.Stub() {
                public int operation(int i1, int i2) {
                    return i1 * i2;
                }
            };
    }