package com.stetis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.stetis.json.JsonConstants;
import com.stetis.mcdream.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.ViewHolder> {

    private JSONArray listdata;

    // RecyclerView recyclerView;
    public DailyReportAdapter(JSONArray listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.dailyreportlistcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
             JSONObject myListData = listdata.getJSONObject(position);
            // holder.line.setBackgroundColor(listdata[position].getStatus());
            holder.item.setText(myListData.getString(JsonConstants.ITEM));
            holder.dateprocessed.setText(myListData.getString(JsonConstants.DATEREQUESTED));
            holder.approvedby.setText(myListData.getString(JsonConstants.APPROVEDBY));
            holder.requestedby.setText(myListData.getString(JsonConstants.REQUESTEDBY));
            //holder.participant.setText(""+myListData.getString(JsonConstants.PARTICIPANTS)+" Members");

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listdata.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;
        private TextView approvedby;
        private TextView dateprocessed;
        private TextView requestedby;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item = (TextView) itemView.findViewById(R.id.item);
            this.approvedby = (TextView) itemView.findViewById(R.id.approvedby);
            this.requestedby = (TextView) itemView.findViewById(R.id.requestedby);
            this.dateprocessed = (TextView) itemView.findViewById(R.id.dateprocessed);
        }
    }
}
