package com.stetis.asynctask;

public abstract class BaseTask<String> implements CustomCallable<String> {

    @Override
    public void setDataAfterLoading(String result) {

    }

    @Override
    public String call() throws Exception {
        return null;
    }
}