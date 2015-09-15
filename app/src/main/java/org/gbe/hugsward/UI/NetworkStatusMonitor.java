package org.gbe.hugsward.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;

import java.util.concurrent.Callable;

/**
 * Created by gbe on 9/15/15.
 */
public class NetworkStatusMonitor {

    Context mContext;
    Runnable mAction;
    BroadcastReceiver mReceiver;

    public NetworkStatusMonitor(Context context, final Runnable callback){
        mContext = context;
        mAction = callback;
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isNetworkAvailable()){
                    mAction.run();
                }
            }
        };
    }

    public void setListener(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mReceiver, filter);
    }

    public void unsetListener() {
        if(mReceiver != null) {
            try {
                mContext.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException ex) {
                // Do nothing, we just want to make sure it's not registered anymore.
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
