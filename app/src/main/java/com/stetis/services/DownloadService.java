package com.stetis.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.stetis.utils.AppUtils;

import java.io.File;

import androidx.annotation.Nullable;


/**
 * Created by SnrMngrFinance on 09/02/2018.
 */

public class DownloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        File file = AppUtils.getFile(AppUtils.SDIR + "/data/sams.apk");
        //Log.d("here", "Not null: "+(file==null)+"");
        if (file != null) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            //Log.d("Lofting", "Update SAMS");
            startActivity(i);
        }
    }
}
