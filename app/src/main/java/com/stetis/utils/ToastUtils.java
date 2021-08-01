

package com.stetis.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by bizzdesk on 8/1/15.
 */
public class ToastUtils {

    private ToastUtils() {}

    public static void show(CharSequence text, int duration, Context context) {
        Toast.makeText(context, text, duration).show();
    }
    public static void show(int resId, int duration, Context context) {
        show(context.getText(resId), duration, context);
    }

    public static void show(CharSequence text, Context context) {
        show(text, Toast.LENGTH_LONG, context);
    }

    public static void show(int resId, Context context) {
        show(context.getText(resId), context);
    }

    public static void showAlertDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showAlertDialog(Context context, int title, int message){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setTitle(title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception ex){ex.printStackTrace();}
    }

    public void hiddenKeyboard(Activity context) {
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public static void showAlertDialogDecision(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok,
                        null
                        );
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


//    public void setupUI(View view) {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    IgrUtil.hideKeyboard(LoginActivity.this);
//                    return false;
//                }
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }
//
//    @OnClick(R.id.login)
//    public void closeKeyboard() {
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//    }
