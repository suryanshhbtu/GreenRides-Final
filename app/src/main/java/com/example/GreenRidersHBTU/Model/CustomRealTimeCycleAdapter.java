package com.example.GreenRidersHBTU.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.GreenRidersHBTU.R;

import java.util.ArrayList;

public class CustomRealTimeCycleAdapter extends BaseAdapter {

    Context context;
    ArrayList<StudentRealTime> arr;
    LayoutInflater inflater;

    public CustomRealTimeCycleAdapter(Context ctx, ArrayList<StudentRealTime> arr) {
        this.context = ctx;
        this.arr = arr;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.activity_custom_real_cycle, null);

        TextView stdNameTV = (TextView)view.findViewById(R.id.stdNameTV);
        TextView branchNameTV = (TextView)view.findViewById(R.id.branchNameTV);
        TextView rollNoTV = (TextView)view.findViewById(R.id.rollNoTV);
        TextView cycleIdTV = (TextView)view.findViewById(R.id.cycleIdTV);

        stdNameTV.setText(arr.get(i).stdName);
        branchNameTV.setText(arr.get(i).stdBranch);
        rollNoTV.setText(arr.get(i).stdRollNo);
        cycleIdTV.setText(arr.get(i).stdCycleId);

        return view;
    }
}
