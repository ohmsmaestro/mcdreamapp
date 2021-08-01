package com.stetis.asynctask;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRunner {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Executor executor = Executors.newCachedThreadPool();

    public <String> void executeAsync(CustomCallable<String> callable) {
        try {
            executor.execute(new RunnableTask<String>(handler, callable));
        } catch (Exception e) {
        }
    }

    public static class RunnableTask<String> implements Runnable{
        private final Handler handler;
        private final CustomCallable<String> callable;

        public RunnableTask(Handler handler, CustomCallable<String> callable) {
            this.handler = handler;
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                final String result = callable.call();
                handler.post(new RunnableTaskForHandler(callable, result));
            } catch (Exception e) {
            }
        }
    }

    public static class RunnableTaskForHandler<String> implements Runnable{

        private CustomCallable<String> callable;
        private String result;

        public RunnableTaskForHandler(CustomCallable<String> callable, String result) {
            this.callable = callable;
            this.result = result;
        }

        @Override
        public void run() {
            callable.setDataAfterLoading(result);
        }
    }
}
