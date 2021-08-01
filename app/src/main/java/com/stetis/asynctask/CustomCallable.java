package com.stetis.asynctask;

import java.util.concurrent.Callable;

public interface CustomCallable<String> extends Callable<String> {
 void setDataAfterLoading(String result);
}
