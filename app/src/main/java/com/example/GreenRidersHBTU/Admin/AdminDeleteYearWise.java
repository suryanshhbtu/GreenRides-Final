package com.example.GreenRidersHBTU.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.Model.Student;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
public class AdminDeleteYearWise extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    // getting baseUrl of Server
    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();
    ArrayList<Student> stdList;

    private static final String FILE_NAME = "nonReturnedCycles.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_delete_yearwise);

//        // back button to close DeleteYearWise
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        deleteYearStudents();
    }
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
    boolean isValidYear(String year){
        if(year.length() != 4) return false;
        for(int i = 0;i<4;i++){
            if(year.charAt(i)<'0' || year.charAt(i)>'9') return false;
        }
        return true;
    }
    private void deleteYearStudents() {
        LinearLayout deleteBtnLL = (LinearLayout) findViewById(R.id.deleteBtnLL);
        EditText yearDeleteET = (EditText) findViewById(R.id.yearDeleteET);
        deleteBtnLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String year = yearDeleteET.getText().toString();
                // preparing for delete
                if(!isValidYear(year)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminDeleteYearWise.this);
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Enter The Year First in Format '20XX' ");
                    builder.show();
                    return;
                }

                Toast.makeText(AdminDeleteYearWise.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
//                 post request
                Call<List<Student>> call = retrofitInterface.deleteYearWise("Bearer "+ MainActivity.AUTH_TOKEN,year.substring(2));
//                 execute http request
                call.enqueue(new Callback<List<Student>>() {
                    @Override
                    public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {

                        if (response.code() == 200) {
                            Toast.makeText(AdminDeleteYearWise.this, "Batch "+year+" Deleted Successfully",
                                    Toast.LENGTH_LONG).show();
                            List<Student> result = response.body();
                            stdList = new ArrayList<>();

                            for(int i = 0;i<result.size();i++){
                                if(result.get(i) != null && result.get(i).getStdEmail()!= null && result.get(i).getStdEmail().contains("@hbtu.ac.in"))
                                    stdList.add(new Student(result.get(i).getStdName(), result.get(i).getStdBranch(), result.get(i).getStdEmail(),result.get(i).getStdRollNo(),result.get(i).getStdCycleId()));
//                                stdList.add(new Student("Suryansh", "ET", "result.get(i).getStdEmail()","result.get(i).getStdRollNo()","result.get(i).getStdCycleId()"));
                            }
//                            Generating Email
                            final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");

                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                    new String[] { "admincycleapp1@hbtu.ac.in" });

                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                    "Detailed List Of Students Who Have Not Returned Cycle");
                            String res = "";
                            for (Student student : stdList) { // mArrayList: ArrayList<Object>
                                res = res+student.getAllDetails()+"\n\n";
                            }
                            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                    res);

                            startActivity(Intent.createChooser(
                                    emailIntent, "Send mail..."));
                        } else{
                            Toast.makeText(AdminDeleteYearWise.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Student>> call, Throwable t) {
                        Toast.makeText(AdminDeleteYearWise.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }



}


