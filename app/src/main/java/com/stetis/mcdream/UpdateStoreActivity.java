package com.stetis.mcdream;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class UpdateStoreActivity extends AppCompatActivity implements NetworkCallResponseCallback {
    private Activity activity;
    private LayoutInflater inflater;
    private LinearLayout previous;
    private TextView headerName;
    private ActionBar bar;
    private ProgressDialog progress;
    private ActionBar.LayoutParams params;
    private Context context;
    private EditText amount;
    private EditText dateprocessed;
    private Spinner selectItem;
    private ArrayList<Item> itemArrayList;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    private int selectedItemId = 0;
    private String selectedDateProcessed = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_store);
        context = this;
        activity = this;
        progress = new ProgressDialog(context, false);
        progress.getInfoTextView().setText("Processing...");
        initUI();
        initAction();
        initItemSpinner();

        bar = getSupportActionBar();
        params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_back, null);
        previous = (LinearLayout) view.findViewById(R.id.previous);
        Intent intent = getIntent();
        headerName = (TextView) view.findViewById(R.id.content);
        headerName.setText("UPDATE STORE");


        previous.setOnClickListener(v -> finish());

        bar.setCustomView(view, params);
        bar.setDisplayShowCustomEnabled(true);
        Toolbar toolbar = (Toolbar) view.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
    }

    private void initUI() {
        selectItem = (Spinner) findViewById(R.id.selectItem);
        amount = (EditText) findViewById(R.id.amount);
        dateprocessed = (EditText) findViewById(R.id.dateprocessed);
    }

    public void initAction() {
        dateprocessed.setInputType(InputType.TYPE_NULL);
        dateprocessed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                fromDatePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateprocessed.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                selectedDateProcessed = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
                            }
                        }, year, month, day);
                fromDatePickerDialog.show();
            }
        });
    }

    public void save(View view) {
        String quantityValue = amount.getText().toString();
        if(selectedItemId!=0&&selectedDateProcessed.length()!=0&&quantityValue!=null&&quantityValue.length()!=0){
            try {

                if (IgrUtil.isNetworkReachable(this)) {
                    try {
                        JSONObject payload = new JSONObject();
                        payload.put(JsonConstants.ITEMID, selectedItemId);
                        payload.put(JsonConstants.STOREID, PreferenceUtils.getInt(JsonConstants.STOREID, 0, context));
                        payload.put(JsonConstants.DATEPROCESSED, selectedDateProcessed);
                        payload.put(JsonConstants.QUANTITY, quantityValue);
                        String link = String.format(HttpUtils.baseUrlV2(), HttpUtils.ITEMSUPDATE);
                        progress.show();
                        TaskRunner taskRunner = new TaskRunner();
                        NetworkTask networkTask = new NetworkTask(payload.toString(), "POST", link, context);
                        networkTask.setNetworkResponseCallback(this);
                        taskRunner.executeAsync(networkTask);
                        Log.d("here", "After");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    ToastUtils.showAlertDialog(this, "Attention", "Please check your internet connection");
                }
            } catch (Exception ex) {
            }
        }
        else {
            ToastUtils.show("All fields are required", context);
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
                    ex.printStackTrace();
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

    public void initItemSpinner() {
        selectItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    selectedItemId = 0;
                }

                else{
                    selectedItemId = itemArrayList.get(position).getItemid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        itemArrayList = SingleInstance.getInstance().currentInstance().getItems();
        ArrayAdapter itemArrayListSpinner = new ArrayAdapter(context, R.layout.custom_spinner, itemArrayList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position % 2 == 0) {
                    view.setBackgroundResource(R.drawable.first_row);
                } else {
                    view.setBackgroundResource(R.drawable.second_row);
                }
                return view;
            }
        };

        selectItem.setAdapter(itemArrayListSpinner);
        selectItem.setSelection(0);
        itemArrayListSpinner.notifyDataSetChanged();
    }

    private void processResponse(String response) {
        try {
            Intent intent = new Intent();
            JSONObject object = new JSONObject(response);
            JSONObject meta = object.getJSONObject(JsonConstants.META);
            String message = meta.getString(JsonConstants.MESSAGE);
            String status = meta.getString(JsonConstants.STATUS);
            dialog(message);


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