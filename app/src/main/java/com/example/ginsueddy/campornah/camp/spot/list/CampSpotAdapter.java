package com.example.ginsueddy.campornah.camp.spot.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ginsueddy.campornah.CampSpot;
import com.example.ginsueddy.campornah.R;

import java.util.ArrayList;

/**
 * Created by ginsueddy on 2/23/18.
 */

public class CampSpotAdapter extends RecyclerView.Adapter<CampSpotAdapter.CampSpotAdapterViewHolder>{

    private ArrayList<CampSpot> campSpots;

    private final CampSpotAdapterOnClickHandler mClickHandler;

    public interface CampSpotAdapterOnClickHandler {
        void onClick(String singleCampSpotData);
    }

    public CampSpotAdapter(CampSpotAdapterOnClickHandler campSpotAdapterOnClickHandler){
        mClickHandler = campSpotAdapterOnClickHandler;
    }

    @Override
    public CampSpotAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.activity_camp_spot_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view  = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new CampSpotAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CampSpotAdapterViewHolder holder, int position) {
        String nameForSingleCampSpot = campSpots.get(position).getName();

        holder.mCampSpotTextView.setText(nameForSingleCampSpot);
    }

    @Override
    public int getItemCount() {
        if(campSpots == null){
            return 0;
        }
        else{
            return campSpots.size();
        }
    }

    public void setCampSpots (ArrayList<CampSpot> campSpots){
        this.campSpots = campSpots;
        notifyDataSetChanged();
    }


    public class CampSpotAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mCampSpotTextView;

        public CampSpotAdapterViewHolder(View itemView) {
            super(itemView);
            mCampSpotTextView = (TextView) itemView.findViewById(R.id.my_camp_spot_list_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String singleCampSpotData = campSpots.get(adapterPosition).getName();
            mClickHandler.onClick(singleCampSpotData);
        }
    }
}


