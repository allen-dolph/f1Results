package com.allendolph.f1results.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by allendolph on 4/7/15.
 * The service which allows the sync adapter framework to access the authenticator.
 */
public class F1ResultsAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private F1ResultsAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new F1ResultsAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
