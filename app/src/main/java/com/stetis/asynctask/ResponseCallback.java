package com.stetis.asynctask;

/**
 * Created by jante on 8/6/15.
 */
public interface ResponseCallback {
    public void onRequestSuccess(String response);

    public void onRequestFailed(String info);
}
