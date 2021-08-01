package com.stetis.mcdream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stetis.asynctask.NetworkCallResponseCallback;
import com.stetis.asynctask.NetworkTask;
import com.stetis.asynctask.TaskRunner;
import com.stetis.dialog.ProgressDialog;
import com.stetis.entities.Item;
import com.stetis.entities.Staff;
import com.stetis.json.JsonConstants;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.IgrUtil;
import com.stetis.utils.PreferenceUtils;
import com.stetis.utils.SingleInstance;
import com.stetis.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Collections;
import java.util.Comparator;

public class LoginActivity extends AppCompatActivity implements NetworkCallResponseCallback {
    private EditText phone, user_pin;
    private String phoneString, passwordString;
    private ProgressDialog progress;
    private Activity activity;
    private LayoutInflater inflater;
    private LinearLayout previous;
    private TextView headerName;
    private ActionBar bar;
    private ActionBar.LayoutParams params;
    private Context context;
    private String rootSource;
    private TextView deviceImei;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            context = this;
            progress = new ProgressDialog(context, false);
            progress.getInfoTextView().setText("Processing...");
            activity = this;
            phone = (EditText) findViewById(R.id.phone);
            user_pin = (EditText) findViewById(R.id.user_pin);
            bar = getSupportActionBar();
            params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.custom_back, null);
            previous = (LinearLayout) view.findViewById(R.id.previous);
            Intent intent = getIntent();
            if (intent != null) {
                rootSource = intent.getStringExtra("source");
            }

            headerName = (TextView) view.findViewById(R.id.content);
            headerName.setText("Login");


            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            if (TextUtils.equals(rootSource, "activate") || TextUtils.equals(rootSource, "flashing") || TextUtils.equals(rootSource, "logout")) {
                previous.setVisibility(View.INVISIBLE);
            }
            bar.setCustomView(view, params);
            bar.setDisplayShowCustomEnabled(true);
            Toolbar toolbar = (Toolbar) view.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
        } catch (Exception ex) {

        }
    }

    @Override
    public void onRequestSuccess(String response) {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

        try {
            if (response != null) {
                try {
                    processResponse(response);
                } catch (Exception ex) {

                }
            }

        } catch (Exception ex) {

        }
    }

    private void processResponse(String response) {
        try {
            Intent intent = new Intent();
            JSONObject object = new JSONObject(response);
            JSONObject meta = object.getJSONObject(JsonConstants.META);
            String message = meta.getString(JsonConstants.MESSAGE);
            String status = meta.getString(JsonConstants.STATUS);
            boolean success = TextUtils.equals(status, JsonConstants.SUCCESS);
            if (success) {
                JSONObject data = object.getJSONObject(JsonConstants.DATA);
                String firstName = data.getString(JsonConstants.FIRSTNAME);
                String lastName = data.getString(JsonConstants.LASTNAME);
                String fullName = lastName+" "+firstName;
                PreferenceUtils.putString(JsonConstants.NAME, fullName, context);
                PreferenceUtils.putString(JsonConstants.JWT, data.getString(JsonConstants.TOKEN), context);
                PreferenceUtils.putString(JsonConstants.ROLENAME, data.getString(JsonConstants.ROLENAME), context);
                String rolename = data.getString(JsonConstants.ROLENAME);
                if(TextUtils.equals(rolename, "STORE")){
                    PreferenceUtils.putInt(JsonConstants.STOREID, data.getInt(JsonConstants.STOREID), context);
                    PreferenceUtils.putString(JsonConstants.STORENAME, data.getString(JsonConstants.STORENAME), context);
                    JSONArray items = data.getJSONArray(JsonConstants.STOREITEMS);
                    JSONArray staffs = data.getJSONArray(JsonConstants.STAFFS);
                    Item item = new Item(0, "Select Item");
                    SingleInstance.getInstance().currentInstance().getItems().add(item);
                    for(int i = 0; i < items.length(); i++){
                        item = new Item(items.getJSONObject(i).getInt(JsonConstants.ITEMID), items.getJSONObject(i).getString(JsonConstants.ITEM));
                        SingleInstance.getInstance().currentInstance().getItems().add(item);
                    }

                    Staff staff = new Staff(0, "Select staff");
                    SingleInstance.getInstance().currentInstance().getStaff().add(staff);
                    for(int i = 0; i < staffs.length(); i++){
                        staff = new Staff(staffs.getJSONObject(i).getInt(JsonConstants.ID), staffs.getJSONObject(i).getString(JsonConstants.NAME));
                        SingleInstance.getInstance().currentInstance().getStaff().add(staff);
                    }

                    Collections.sort(SingleInstance.getInstance().currentInstance().getStaff(), new Comparator<Staff>() {
                        @Override
                        public int compare(Staff o1, Staff o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    intent = new Intent(context, StoreActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                dialog(message);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestFailed(String info) {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

        dialog(info);
    }

    public void activate(View view) {
        try {
//            Intent intent = new Intent(context, StoreActivity.class);
//            startActivity(intent);
            phoneString = phone.getText().toString();
            passwordString = user_pin.getText().toString();
            if (phoneString.length() != 0 && phoneString != null && phoneString.length() > 1 && passwordString.length() != 0 && passwordString != null) {
                PreferenceUtils.putString(JsonConstants.PHONE, phoneString, context);
                processLogin();
            } else {
                ToastUtils.show("All fields are required and phone must be at least one digit", this);
            }
        } catch (Exception ex) {

        }
    }


    public void processLogin() {
        try {
            if (IgrUtil.isNetworkReachable(this)) {
                try {
                    JSONObject payload = new JSONObject();
                    payload.put(JsonConstants.PHONE, phoneString);
                    payload.put(JsonConstants.PASSWORD, passwordString);
                    String link = String.format(HttpUtils.baseUrlV2(), HttpUtils.LOGIN);
                    progress.show();
                    TaskRunner taskRunner = new TaskRunner();
                    NetworkTask networkTask = new NetworkTask(payload.toString(), "POST", link, context);
                    networkTask.setNetworkResponseCallback(this);
                    taskRunner.executeAsync(networkTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                ToastUtils.showAlertDialog(this, "Attention", "Please check your internet connection");
            }
        } catch (Exception ex) {

        }
    }

    public void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Alert")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
