package com.stetis.utils;

/**
 * Created by jante on 8/1/15.
 */
public class SingleInstance {
    private static SingleInstance ourInstance = new SingleInstance();

    public static SingleInstance getInstance() {
        return ourInstance;
    }


    private SingleInstance() {
      //  ConfirmPatternActivity confirm= null;
    }

    private SinglePresent singlePresent = new SinglePresent();
    public SinglePresent currentInstance(){
        return singlePresent;
    }
}
