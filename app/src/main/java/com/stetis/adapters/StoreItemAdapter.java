package com.stetis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stetis.json.JsonConstants;
import com.stetis.mcdream.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ViewHolder> implements Filterable {

    private JSONArray listdata;
    private JSONArray listdatadisplay;

    // RecyclerView recyclerView;
    public StoreItemAdapter(JSONArray listdata) {
        this.listdata = listdata;
        this.listdatadisplay = listdata;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.itemlistcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
             JSONObject myListData = listdata.getJSONObject(position);
            // holder.line.setBackgroundColor(listdata[position].getStatus());
            holder.item.setText(myListData.getString(JsonConstants.ITEM));
            holder.unit.setText(myListData.getString(JsonConstants.QUANTITY));

            //holder.participant.setText(""+myListData.getString(JsonConstants.PARTICIPANTS)+" Members");

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listdatadisplay.length();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;
        private TextView unit;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item = (TextView) itemView.findViewById(R.id.item);
            this.unit = (TextView) itemView.findViewById(R.id.unit);
        }


    }

    @Override
    public Filter getFilter() {
        // Log.d("here", "Right here");
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listdatadisplay = (JSONArray)results.values;
                //candidateArrayDisplayed = (ArrayList<Candidate>)results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                JSONArray filteredResult = new JSONArray();
                //ArrayList<Candidate> filteredResult = new ArrayList<>();
                if(listdata ==null){
                    listdata = listdatadisplay;
                   // candidateArrayList = new ArrayList<>(candidateArrayDisplayed);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = listdata.length();
                    results.values = listdata;
                } else {
                    constraint = constraint.toString().toLowerCase();

                    for (int i = 0; i< listdata.length(); i++){
                        try {
                            String data = listdata.getJSONObject(i).getString(JsonConstants.ITEM);
                            if (data.toLowerCase().contains(constraint.toString())) {
                                JSONObject candidate = new JSONObject();
                                candidate.put(JsonConstants.ITEM, listdata.getJSONObject(i).getString(JsonConstants.ITEM));
                                candidate.put(JsonConstants.QUANTITY, listdata.getJSONObject(i).getString(JsonConstants.QUANTITY));
                                filteredResult.put(candidate);
                            }
                        }
                        catch (Exception ex){

                        }
                    }

                    results.count = filteredResult.length();
                    results.values = filteredResult;

                }
                return results;
            }
        };
        return filter;
    }
}
