package com.m1kes.expressscript.adapters.recyclerview;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.MedicalAid;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicalAidAdapter extends RecyclerView.Adapter<MedicalAidAdapter.RecyclerViewHolder> {

    private List<MedicalAid> data;
    private Context context;
    private MedicalAidAdapter.RecyclerViewHolder viewHolder;

    public MedicalAidAdapter(Context context, List<MedicalAid> data) {
        if(data == null || data.isEmpty() )data = new ArrayList<>();
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_aid_row_item,parent,false);
        viewHolder = new RecyclerViewHolder(view,context);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        if(holder == null){
            System.out.println("View holder is null");
        }
        MedicalAid item = data.get(position);

        if (item != null) {
            holder.medicalName.setText(item.getName());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtMedicalAidName) TextView medicalName;
        private Context context;

        RecyclerViewHolder(View view,Context context){
            super(view);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }

}
