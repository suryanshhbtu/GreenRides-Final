package com.example.GreenRidersHBTU.Util;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Admin.AdminHome;
import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.Model.CustomStudentAdapter;
import com.example.GreenRidersHBTU.Model.Student;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.Model.Cycle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ShowRealtimeCycles extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();
    String _id;
    ListView l;
    ProgressDialog dialog;
    ArrayList<Student> stdList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // back button to close this activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.realtime_list);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing

        l = findViewById(R.id.list);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching Rented Cycles");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        getRentedCycle();

    }
    // back button to close AdminHome
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
    private void getRentedCycle() {

//        // post request
        Call<List<Cycle>> call = retrofitInterface.getRentedCycle("Bearer "+ MainActivity.AUTH_TOKEN);
        // execute http request
        call.enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {

                if (response.code() == 200) {

                    dialog.hide();
                    Toast.makeText(ShowRealtimeCycles.this, "Loaded Realtime Rented Cycles Successfully",
                            Toast.LENGTH_LONG).show();
                   List<Cycle> result = response.body();
                    stdList = new ArrayList<>();

                    for(int i = 0;i<result.size();i++){
                        if(result.get(i) != null && result.get(i).getEmail()!= null && result.get(i).getEmail().contains("@hbtu.ac.in"))
                        stdList.add(new Student(result.get(i).getStdname(), result.get(i).getBranchName(), result.get(i).getEmail(),result.get(i).getRollNo(),result.get(i).getCycleid()));
                       }
                    CustomStudentAdapter customStudentAdapter =  new CustomStudentAdapter(getApplicationContext(), stdList);
                    l.setAdapter(customStudentAdapter);

//                    arr= new ArrayAdapter<String>(ShowRealtimeCycles.this,R.layout.support_simple_spinner_dropdown_item,
//                            cycles);
//                    l.setAdapter(arr);



//                    WordAdapter family=new WordAdapter(ShowRealtimeCycles.this,result);
//                    ListView listView=(ListView)findViewById(R.id.list);
//                    listView.setAdapter(family);
                } else if (response.code() == 404) {
                    Toast.makeText(ShowRealtimeCycles.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ShowRealtimeCycles.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                Toast.makeText(ShowRealtimeCycles.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_realtime_mail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.realTimeStatusMail:
                final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { "admincycleapp1@hbtu.ac.in" });

                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Realtime Status Of Rented Cycles");
                String res = "";
                for (Student student : stdList) { // mArrayList: ArrayList<Object>
                    res = res+student.getAllDetails()+"\n\n";
                }
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        res);

                startActivity(Intent.createChooser(
                        emailIntent, "Send mail..."));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}