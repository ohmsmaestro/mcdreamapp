package com.stetis.mcdream;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stetis.adapters.DailyReportAdapter;
import com.stetis.asynctask.HttpClient;
import com.stetis.asynctask.ResponseCallback;
import com.stetis.dialog.ProgressDialog;
import com.stetis.json.JsonConstants;
import com.stetis.utils.AppUtils;
import com.stetis.utils.ExtApi;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyReportActivity extends AppCompatActivity implements ResponseCallback {
    private EditText fromdate;
    private EditText todate;
    private ImageView search;
    private RecyclerView recyclerView;
    private String fromDateString = "";
    private String toDateString = "";
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private ProgressDialog progress;
    private Activity activity;
    private LayoutInflater inflater;
    private LinearLayout previous;
    private TextView headerName;
    private ActionBar bar;
    private ActionBar.LayoutParams params;
    private Context context;
    private String currentDate = "";
    private boolean defaultPassed = false;
    private LinearLayout emptyView;
    final Calendar cldr2 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        initView();
        currentDate = ExtApi.getCurrentDate().get("t1");
        initAction();
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
        headerName.setText("Daily Report");


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
    }

    private void initView() {
        fromdate = (EditText) findViewById(R.id.fromdate);
        todate = (EditText) findViewById(R.id.todate);
        search = (ImageView) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        emptyView = (LinearLayout) findViewById(R.id.norecords);

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    long minDate = 0;
    public void initAction() {

        fromdate.setInputType(InputType.TYPE_NULL);
        todate.setInputType(InputType.TYPE_NULL);
        final Calendar cldr1 = Calendar.getInstance();
        minDate = cldr1.getTime().getTime();
        AtomicInteger day1 = new AtomicInteger(cldr1.get(Calendar.DAY_OF_MONTH));
        AtomicInteger month1 = new AtomicInteger(cldr1.get(Calendar.MONTH));
        AtomicInteger year1 = new AtomicInteger(cldr1.get(Calendar.YEAR));


        AtomicInteger day2 = new AtomicInteger(cldr2.get(Calendar.DAY_OF_MONTH));
        AtomicInteger month2 = new AtomicInteger(cldr2.get(Calendar.MONTH));
        AtomicInteger year2 = new AtomicInteger(cldr2.get(Calendar.YEAR));



        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // date picker dialog
                fromDatePickerDialog = new DatePickerDialog(context,
                        (view, selectedYear, monthOfYear, dayOfMonth) -> {
                            try {
                                day1.set(dayOfMonth);
                                month1.set(monthOfYear);
                                year1.set(selectedYear);
                                fromdate.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
                                fromDateString = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fromDateString);
                                minDate = date.getTime();
                                setMinToDateDialog(date.getTime());
                                //Log.d("here", (toDatePickerDialog==null)+"");
                                //toDatePickerDialog.getDatePicker().setMinDate(date.getTime());
                                if (defaultPassed == false) {
                                    todate.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
                                } else {


                                }
                                //toDatePickerDialog.getDatePicker().setMaxDate();
                            } catch (Exception ex) {
                                ex.printStackTrace();

                            }
                        }, year1.get(), month1.get(), day1.get());

                fromDatePickerDialog.show();
            }
        });


        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMinToDateDialog(minDate);
                // date picker dialog
//                toDatePickerDialog = new DatePickerDialog(context,
//                        (view, selectedYear, monthOfYear, dayOfMonth) -> {
//                            day2.set(dayOfMonth);
//                            month2.set(monthOfYear);
//                            year2.set(selectedYear);
//                            todate.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
//                            toDateString = selectedYear + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
//                        }, year2.get(), month2.get(), day2.get());
               toDatePickerDialog.show();
            }
        });

        fromDateString = ExtApi.getCurrentDate().get("t2");
        toDateString = ExtApi.getCurrentDate().get("t2");

        fromdate.setText(currentDate);
        todate.setText(currentDate);
        defaultPassed = true;
    }

    public void setMinToDateDialog(long minDate){

        final Calendar cldr2 = Calendar.getInstance();
        Date date = new Date(minDate);
        cldr2.setTime(date);
        AtomicInteger day2 = new AtomicInteger(cldr2.get(Calendar.DAY_OF_MONTH));
        AtomicInteger month2 = new AtomicInteger(cldr2.get(Calendar.MONTH));
        AtomicInteger year2 = new AtomicInteger(cldr2.get(Calendar.YEAR));
        toDatePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        day2.set(dayOfMonth);
                        month2.set(monthOfYear);
                        year2.set(selectedYear);
                        todate.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
                        toDateString = selectedYear + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    }
                }, year2.get(), month2.get(), day2.get());

        toDatePickerDialog.getDatePicker().setMinDate(minDate);
    }

    public void retrieveRange(View view) {
        processRequest();
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
                String resource = String.format(HttpUtils.RETRIEVEREQUEUSTS, storeid, fromDateString, toDateString);
                client.execute(String.format(HttpUtils.baseUrlV2(), resource));
            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                JSONArray requests = data.getJSONArray(JsonConstants.REQUESTS);
                if (requests.length() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    DailyReportAdapter adapter = new DailyReportAdapter(requests);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
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
                        // finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}