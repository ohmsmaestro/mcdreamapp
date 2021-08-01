package com.stetis.mcdream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.stetis.json.JsonConstants;
import com.stetis.utils.PreferenceUtils;

public class StoreActivity extends AppCompatActivity {
    private TextView name;
    private TextView designation;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storeactivity);
        context = this;
        initializeUI();
        updateUserUI();
    }

    public void initializeUI(){
        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
    }

    public void updateUserUI(){
        name.setText(PreferenceUtils.getString(JsonConstants.NAME, "", context));
        String designationInformation = PreferenceUtils.getString(JsonConstants.ROLENAME, "", context)+" ("+PreferenceUtils.getString(JsonConstants.STORENAME, "", context)+")";
        designation.setText(designationInformation);
    }

    public void performAction(View view) {
        if(R.id.takerequest==view.getId()){
            Intent intent = new Intent(context, TakeRequestActivity.class);
            startActivity(intent);
        }
        else if(R.id.updateitem==view.getId()){
            Intent intent = new Intent(context, UpdateStoreActivity.class);
            startActivity(intent);
        }
        else if(R.id.dailyreport==view.getId()){
            Intent intent = new Intent(context, DailyReportActivity.class);
            startActivity(intent);
        }

        else if(R.id.viewstore==view.getId()){
            Intent intent = new Intent(context, ItemActivity.class);
            startActivity(intent);
        }

        else if(R.id.loglpo==view.getId()){
            Intent intent = new Intent(context, LogLPOActivity.class);
            startActivity(intent);
        }

        else if(R.id.viewlpos ==view.getId()){
            Intent intent = new Intent(context, ViewLPOActivity.class);
            startActivity(intent);
        }
    }
}
