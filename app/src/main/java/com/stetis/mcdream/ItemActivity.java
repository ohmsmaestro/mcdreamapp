package com.stetis.mcdream;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stetis.adapters.DailyReportAdapter;
import com.stetis.adapters.StoreItemAdapter;
import com.stetis.asynctask.HttpClient;
import com.stetis.asynctask.ResponseCallback;
import com.stetis.dialog.ProgressDialog;
import com.stetis.json.JsonConstants;
import com.stetis.utils.AppUtils;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ItemActivity extends AppCompatActivity implements ResponseCallback {
    private EditText searchitem;
    private RecyclerView recyclerView;
    private LinearLayout norecords;
    private ProgressDialog progress;
    private Activity activity;
    private LayoutInflater inflater;
    private LinearLayout previous;
    private TextView headerName;
    private ActionBar bar;
    private ActionBar.LayoutParams params;
    private Context context;
    private StoreItemAdapter storeItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeitem);
        initView();

        context = this;
        progress = new ProgressDialog(context, false);
        progress.getInfoTextView().setText("Processing...");
        activity = this;
        bar = getSupportActionBar();
        params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_back, null);
        previous = (LinearLayout) view.findViewById(R.id.previous);
        headerName = (TextView) view.findViewById(R.id.content);
        headerName.setText("Store Items");


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bar.setCustomView(view, params);
        bar.setDisplayShowCustomEnabled(true);
        Toolbar toolbar = (Toolbar) view.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        processRequest();
    }


    private void initView() {
        searchitem = (EditText) findViewById( R.id.searchitem );
        recyclerView = (RecyclerView)findViewById( R.id.recyclerView );
        norecords = (LinearLayout)findViewById( R.id.norecords );

        searchitem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                storeItemAdapter.getFilter().filter(s);
            }
        });
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

    @Override
    public void onRequestFailed(String info) {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

        dialog(info);
    }

    public void processRequest() {
        try {
            if (AppUtils.isInternetConnected(context)) {
                progress.show();
                int storeid = PreferenceUtils.getInt(JsonConstants.STOREID, 0, context);
                HttpClient client = new HttpClient("GET", context);
                client.setResponseCallback(this);
                String resource = String.format(HttpUtils.STOREITEMS, storeid);
                Log.d("here", resource);
                client.execute(String.format(HttpUtils.baseUrlV2(), resource));
            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONObject meta = object.getJSONObject(JsonConstants.META);
            String message = meta.getString(JsonConstants.MESSAGE);
            String status = meta.getString(JsonConstants.STATUS);
            boolean success = TextUtils.equals(status, JsonConstants.SUCCESS);
            if (success) {
                JSONObject data = object.getJSONObject(JsonConstants.DATA);
                JSONArray requests = data.getJSONArray(JsonConstants.ITEMS);
                if (requests.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    norecords.setVisibility(View.GONE);
                    storeItemAdapter = new StoreItemAdapter(requests);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(storeItemAdapter);
                    storeItemAdapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    norecords.setVisibility(View.VISIBLE);
                }
            } else {
                dialog(message);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Alert")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}