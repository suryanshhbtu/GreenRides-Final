package com.example.GreenRidersHBTU.Model;
// CUSTOM ADAPTER to show realtime status of rented cycles
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.GreenRidersHBTU.R;

import java.util.ArrayList;

public class CustomStudentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Student> arr;
    LayoutInflater inflater;

    public CustomStudentAdapter(Context ctx, ArrayList<Student> arr) {
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

        stdNameTV.setText(arr.get(i).name);
        branchNameTV.setText(arr.get(i).branch);
        rollNoTV.setText(arr.get(i).rollno);
        cycleIdTV.setText(arr.get(i).cycleid);

        return view;
    }
}
