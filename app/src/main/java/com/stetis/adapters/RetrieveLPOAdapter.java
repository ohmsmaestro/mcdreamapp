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
import org.json.JSONException;
import org.json.JSONObject;


public class RetrieveLPOAdapter extends RecyclerView.Adapter<RetrieveLPOAdapter.ViewHolder> {

    private JSONArray listdata;
    private JSONArray listdatadisplay;

    // RecyclerView recyclerView;
    public RetrieveLPOAdapter(JSONArray listdata) {
        this.listdata = listdata;
       // this.listdatadisplay = listdata;
        //setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.lpolistcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
           // JSONObject myListData = listdatadisplay.getJSONObject(position);
            // holder.line.setBackgroundColor(listdata[position].getStatus());


            holder.bind(listdata.getJSONObject(position));

            //holder.participant.setText(""+myListData.getString(JsonConstants.PARTICIPANTS)+" Members");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listdata.length();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lponumber;
        private TextView supplier;
        private TextView amount;
        private TextView datesupplied;

        public ViewHolder(View itemView) {
            super(itemView);
            this.lponumber = (TextView) itemView.findViewById(R.id.lponumber);
            this.supplier = (TextView) itemView.findViewById(R.id.supplier);
            this.amount = (TextView) itemView.findViewById(R.id.amount);
            this.datesupplied = (TextView) itemView.findViewById(R.id.datesupplied);
        }

        public void bind(JSONObject item) {
            try {
                lponumber.setText(item.getString(JsonConstants.LPONUMBER));
                supplier.setText(item.getString(JsonConstants.SUPPLIER));
                amount.setText("\u20A6" + item.getString(JsonConstants.AMOUNT));
                datesupplied.setText(item.getString(JsonConstants.DATESUPPLIED));
            } catch (Exception ex) {

            }
        }
    }

//    @Override
//    public Filter getFilter() {
//        // Log.d("here", "Right here");
//        Filter filter = new Filter() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                listdatadisplay = (JSONArray) results.values;
//                //candidateArrayDisplayed = (ArrayList<Candidate>)results.values;
//                notifyDataSetChanged();
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//
//                JSONArray filteredResult = new JSONArray();
//                //ArrayList<Candidate> filteredResult = new ArrayList<>();
//                if (listdata == null) {
//                    try {
//                        listdata = new JSONArray(listdatadisplay.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if (constraint == null || constraint.length() == 0) {
//                    results.count = listdata.length();
//                    results.values = listdata;
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < listdata.length(); i++) {
//                        try {
//                            String data = listdata.getJSONObject(i).getString(JsonConstants.LPONUMBER);
//                            if (data.toLowerCase().contains(constraint.toString())) {
//                                JSONObject lpo = new JSONObject();
//                                lpo.put(JsonConstants.LPONUMBER, listdata.getJSONObject(i).getString(JsonConstants.LPONUMBER));
//                                lpo.put(JsonConstants.AMOUNT, "&#x20A6;" + listdata.getJSONObject(i).getString(JsonConstants.AMOUNT));
//                                lpo.put(JsonConstants.DATESUPPLIED, listdata.getJSONObject(i).getString(JsonConstants.DATESUPPLIED));
//                                lpo.put(JsonConstants.SUPPLIER, listdata.getJSONObject(i).getString(JsonConstants.SUPPLIER));
//                                filteredResult.put(lpo);
//                            }
//                        } catch (Exception ex) {
//
//                        }
//                    }
//
//                    results.count = filteredResult.length();
//                    results.values = filteredResult;
//
//                }
//                return results;
//            }
//        };
//        return filter;
//    }
}
