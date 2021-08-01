package com.stetis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.stetis.mcdream.R;


/**
 * Created by jante on 8/1/15.
 */
public class ProgressDialog extends Dialog {
    private TextView info;

    public ProgressDialog(Context context, boolean cancelable) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress);
        TextView infoText = (TextView) findViewById(R.id.progress_info);
        info = infoText;
        setCancelable(cancelable);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public TextView getInfoTextView() {
        return info;
    }
}
