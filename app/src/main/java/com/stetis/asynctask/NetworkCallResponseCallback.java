package com.stetis.asynctask;

public interface NetworkCallResponseCallback {
    public void onRequestSuccess(String response);

    public void onRequestFailed(String info);
}

