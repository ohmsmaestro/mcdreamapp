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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stetis.asynctask.HttpClient;
import com.stetis.asynctask.ResponseCallback;
import com.stetis.asynctask.TaskRunner;
import com.stetis.dialog.ProgressDialog;
import com.stetis.entities.Staff;
import com.stetis.entities.Supplier;
import com.stetis.json.JsonConstants;
import com.stetis.utils.ExtApi;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.IgrUtil;
import com.stetis.utils.PreferenceUtils;
import com.stetis.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class LogLPOActivity extends AppCompatActivity implements ResponseCallback {
    private Activity activity;
    private LayoutInflater inflater;
    private LinearLayout previous;
    private TextView headerName;
    private ActionBar bar;
    private ProgressDialog progress;
    private ActionBar.LayoutParams params;
    private Context context;
    private EditText lponumber;
    private EditText waybillnumber;
    private Spinner supplierspinner;
    private EditText price;
    private EditText listofmaterials;
    private EditText daterequested;
    private EditText datesupplied;
    private ArrayList<Supplier> supplierArrayList;
    private ArrayList<Staff> staffArrayList;
    private Button save;
    DatePickerDialog requestedDatePickerDialog;
    DatePickerDialog suppliedDatePickerDialog;
    SimpleDateFormat dateFormatter;
    private int selectedApprovedById = 0;
    private int selectedRequestById = 0;
    private int selectedSupplierId = 0;
    final Calendar cldr2 = Calendar.getInstance();
    private String selectedDateOrdered = "";
    private String selectedDateSupplied = "";
    private boolean defaultPassed = false;
    private String currentDate = "";
    private int requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loglpo);
        context = this;
        activity = this;
        progress = new ProgressDialog(context, false);
        progress.setTitle("Please wait.... Processing");
        initUI();
        //initSupplierSpinner();
        initAction();
        retrieveSuppliers();

        bar = getSupportActionBar();
        params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_back, null);
        previous = (LinearLayout) view.findViewById(R.id.previous);
        Intent intent = getIntent();

        headerName = (TextView) view.findViewById(R.id.content);
        headerName.setText("LOG LPO");


        previous.setOnClickListener(v -> finish());

        bar.setCustomView(view, params);
        bar.setDisplayShowCustomEnabled(true);
        Toolbar toolbar = (Toolbar) view.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
    }


    private void initUI() {
        lponumber = (EditText) findViewById(R.id.lponumber);
        waybillnumber = (EditText) findViewById(R.id.waybillnumber);
        supplierspinner = (Spinner) findViewById(R.id.supplierspinner);
        price = (EditText) findViewById(R.id.price);
        listofmaterials = (EditText) findViewById(R.id.listofmaterials);
        daterequested = (EditText) findViewById(R.id.daterequested);
        datesupplied = (EditText) findViewById(R.id.datesupplied);
    }

    public void initSupplierSpinner() {
        supplierspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedSupplierId = 0;
                } else {
                    selectedSupplierId = supplierArrayList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter supplierArrayAdapter = new ArrayAdapter(context, R.layout.custom_spinner, supplierArrayList) {
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

        supplierspinner.setAdapter(supplierArrayAdapter);
        supplierspinner.setSelection(0);
        supplierArrayAdapter.notifyDataSetChanged();
    }


    long minDate = 0;

    public void initAction() {
        daterequested.setInputType(InputType.TYPE_NULL);

        daterequested.setInputType(InputType.TYPE_NULL);
        datesupplied.setInputType(InputType.TYPE_NULL);
        final Calendar cldr1 = Calendar.getInstance();
        minDate = cldr1.getTime().getTime();
        AtomicInteger day1 = new AtomicInteger(cldr1.get(Calendar.DAY_OF_MONTH));
        AtomicInteger month1 = new AtomicInteger(cldr1.get(Calendar.MONTH));
        AtomicInteger year1 = new AtomicInteger(cldr1.get(Calendar.YEAR));


        AtomicInteger day2 = new AtomicInteger(cldr2.get(Calendar.DAY_OF_MONTH));
        AtomicInteger month2 = new AtomicInteger(cldr2.get(Calendar.MONTH));
        AtomicInteger year2 = new AtomicInteger(cldr2.get(Calendar.YEAR));


        daterequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // date picker dialog
                requestedDatePickerDialog = new DatePickerDialog(context,
                        (view, selectedYear, monthOfYear, dayOfMonth) -> {
                            try {
                                day1.set(dayOfMonth);
                                month1.set(monthOfYear);
                                year1.set(selectedYear);
                                daterequested.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
                                selectedDateOrdered = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(selectedDateOrdered);
                                minDate = date.getTime();
                                setMinToDateDialog(date.getTime());
                                //Log.d("here", (toDatePickerDialog==null)+"");
                                //toDatePickerDialog.getDatePicker().setMinDate(date.getTime());
                                if (defaultPassed == false) {
                                    datesupplied.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
                                } else {


                                }
                                //toDatePickerDialog.getDatePicker().setMaxDate();
                            } catch (Exception ex) {
                                ex.printStackTrace();

                            }
                        }, year1.get(), month1.get(), day1.get());

                requestedDatePickerDialog.show();
            }
        });


        datesupplied.setOnClickListener(new View.OnClickListener() {
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
                suppliedDatePickerDialog.show();
            }
        });

        selectedDateOrdered = ExtApi.getCurrentDate().get("t2");
        selectedDateSupplied = ExtApi.getCurrentDate().get("t2");

        daterequested.setText(currentDate);
        datesupplied.setText(currentDate);
        defaultPassed = true;
    }

    public void setMinToDateDialog(long minDate) {

        final Calendar cldr2 = Calendar.getInstance();
        Date date = new Date(minDate);
        cldr2.setTime(date);
        AtomicInteger day2 = new AtomicInteger(cldr2.get(Calendar.DAY_OF_MONTH));
        AtomicInteger month2 = new AtomicInteger(cldr2.get(Calendar.MONTH));
        AtomicInteger year2 = new AtomicInteger(cldr2.get(Calendar.YEAR));
        suppliedDatePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        day2.set(dayOfMonth);
                        month2.set(monthOfYear);
                        year2.set(selectedYear);
                        datesupplied.setText(dayOfMonth + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + selectedYear);
                        selectedDateSupplied = selectedYear + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    }
                }, year2.get(), month2.get(), day2.get());

        suppliedDatePickerDialog.getDatePicker().setMinDate(minDate);
    }

    public void save(View view) {
        String amountString = price.getText().toString();
        String itemListString = listofmaterials.getText().toString();
        String lpoNumberString = lponumber.getText().toString();
        String waybillNumberString = waybillnumber.getText().toString();
        if (lpoNumberString != null && lpoNumberString.length() != 0 && waybillNumberString != null && waybillNumberString.length() != 0 && selectedSupplierId != 0 && selectedDateOrdered.length() != 0 && selectedDateSupplied.length() != 0 && amountString != null && amountString.length() != 0 && itemListString != null && itemListString.length() != 0) {
            try {

                if (IgrUtil.isNetworkReachable(this)) {
                    try {
                        requestType = 2;
                        JSONObject payload = new JSONObject();
                        payload.put(JsonConstants.SUPPLIERID, selectedSupplierId);
                        payload.put(JsonConstants.DATEORDERED, selectedDateOrdered);
                        payload.put(JsonConstants.DATESUPPLIED, selectedDateSupplied);
                        payload.put(JsonConstants.LPONUMBER, lpoNumberString);
                        payload.put(JsonConstants.STOREID, PreferenceUtils.getInt(JsonConstants.STOREID, 0, context));
                        payload.put(JsonConstants.AMOUNT, amountString);
                        payload.put(JsonConstants.ITEMLIST, itemListString);
                        payload.put(JsonConstants.WAYBILLNUMBER, waybillNumberString);
                        String link = String.format(HttpUtils.baseUrlV2(), HttpUtils.LPOS);
                        progress.show();
                        HttpClient client = new HttpClient(payload.toString(), "POST", context);
                        client.setResponseCallback(this);
                        client.execute(link);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    ToastUtils.showAlertDialog(this, "Attention", "Please check your internet connection");
                }
            } catch (Exception ex) {

            }
        } else {
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
                if (requestType == 2) {
                    processSaveRequestResponse(response);
                } else if (requestType == 1) {
                    processGetSuppliersRespose(response);
                }

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

        if (requestType == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(info)
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
            return;
        }

        dialog(info);
    }

    private void processGetSuppliersRespose(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONObject meta = object.getJSONObject(JsonConstants.META);
            String message = meta.getString(JsonConstants.MESSAGE);
            String status = meta.getString(JsonConstants.STATUS);
            boolean success = TextUtils.equals(status, JsonConstants.SUCCESS);
            if (success) {
                JSONObject data = object.getJSONObject(JsonConstants.DATA);
                JSONArray suppliers = data.getJSONArray(JsonConstants.SUPPLIERS);
                if (suppliers.length() != 0) {
                    supplierArrayList = new ArrayList<>();

                    for (int i = 0; i < suppliers.length(); i++) {
                        Supplier supplier = new Supplier();
                        if (i == 0) {
                            supplier.setId(0);
                            supplier.setName("Select Supplier");
                            supplierArrayList.add(supplier);
                        }

                        supplier = new Supplier();
                        supplier.setName(suppliers.getJSONObject(i).getString(JsonConstants.NAME));
                        supplier.setId(suppliers.getJSONObject(i).getInt(JsonConstants.ID));
                        supplierArrayList.add(supplier);
                    }
                    initSupplierSpinner();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("No suppliers found")
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
            } else {
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

    private void processSaveRequestResponse(String response) {
        try {
            //Log.d("here", response);
            JSONObject object = new JSONObject(response);
            JSONObject meta = object.getJSONObject(JsonConstants.META);
            String message = meta.getString(JsonConstants.MESSAGE);

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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void retrieveSuppliers() {
        try {
            requestType = 1;
            String link = String.format(HttpUtils.baseUrlV2(), HttpUtils.SUPPLIERS);
            progress.show();
            HttpClient client = new HttpClient("GET", context);
            client.setResponseCallback(this);
            client.execute(link);
        } catch (Exception ex) {

        }
    }

}