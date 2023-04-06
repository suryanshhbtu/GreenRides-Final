package com.example.GreenRidersHBTU.Util;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.Model.CustomRealTimeCycleAdapter;
import com.example.GreenRidersHBTU.Model.StudentRealTime;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // back button to close this activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.word_list);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        getRentedCycle();
        l = findViewById(R.id.list);



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
                    Toast.makeText(ShowRealtimeCycles.this, "Loaded Realtime Rented Cycles Successfully",
                            Toast.LENGTH_LONG).show();
                   List<Cycle> result = response.body();
                    String cycles[] = new String[result.size()];
                    String realTimeStd[] = new String[result.size()];
                    ArrayList<StudentRealTime> stdList = new ArrayList<>();

                    for(int i = 0;i<result.size();i++){
                        stdList.add(new StudentRealTime(result.get(i).getStdname(), result.get(i).getBranchName(), result.get(i).getEmail(),result.get(i).getRollNo(),result.get(i).getCycleid()));
                       cycles[i] = result.get(i).getCycleid();
                   }
                    CustomRealTimeCycleAdapter customRealTimeCycleAdapter =  new CustomRealTimeCycleAdapter(getApplicationContext(), stdList);
                    l.setAdapter(customRealTimeCycleAdapter);
                    ArrayAdapter<String> arr;

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
}