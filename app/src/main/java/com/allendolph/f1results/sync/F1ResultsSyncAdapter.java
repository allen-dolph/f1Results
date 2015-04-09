package com.allendolph.f1results.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by allendolph on 4/7/15.
 */
public class F1ResultsSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = F1ResultsSyncAdapter.class.getCanonicalName();

    private Context mContext;

    public F1ResultsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Performing Sync...");

    }
}
