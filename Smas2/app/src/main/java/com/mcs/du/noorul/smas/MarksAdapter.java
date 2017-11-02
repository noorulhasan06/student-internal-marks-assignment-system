package com.mcs.du.noorul.smas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Noorul on 18-10-2017.
 */

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MyViewHolder>{
    private ArrayList<MarksDataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subject_id;
        TextView subject_name;
        TextView marks_minor;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.subject_id = (TextView) itemView.findViewById(R.id.subject_id);
            this.subject_name = (TextView) itemView.findViewById(R.id.subject_name);
            this.marks_minor = (TextView) itemView.findViewById(R.id.marks_minor);
        }
    }

    public MarksAdapter(ArrayList<MarksDataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marks_card, parent, false);

        view.setOnClickListener(student_marks.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewId = holder.subject_id;
        TextView textViewName = holder.subject_name;
        TextView textViewmarks = holder.marks_minor;

        textViewName.setText(dataSet.get(listPosition).getSubject_name());
        textViewId.setText(dataSet.get(listPosition).getSubject_id());
        textViewmarks.setText(dataSet.get(listPosition).getMarks());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
