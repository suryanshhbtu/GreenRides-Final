package com.example.GreenRidersHBTU.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminAddUser extends AppCompatActivity {

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface

    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_user);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        addUserHandler();
        // back button to close AddUser
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    // back button to close AdminHome
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
    private void addUserHandler() {


        LinearLayout addUserLL = (LinearLayout) findViewById(R.id.addUserLL);
        final EditText nameUserET =(EditText) findViewById(R.id.nameUserET);
//        final EditText branchUserET =(EditText) findViewById(R.id.branchUserET);
//        final EditText rollnoUserET =(EditText) findViewById(R.id.rollnoUserET);
        final EditText emailUserET =(EditText) findViewById(R.id.emailUserET);
        final EditText passwordUserET =(EditText) findViewById(R.id.passwordUserET);

        addUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminAddUser.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("name", nameUserET.getText().toString());
                map.put("password", passwordUserET.getText().toString());
                map.put("email", emailUserET.getText().toString());
                String branch = "";
                String curr = emailUserET.getText().toString().substring(4,6);
                switch(curr){
                    case "01":
                        branch = "BE";
                        break;
                    case "02":
                        branch = "Civil Engineering";
                        break;
                    case "03":
                        branch = "Chemical Engineering";
                        break;
                    case "04":
                        branch = "Computer Science Engineering";
                        break;
                    case "05":
                        branch = "Electrical Engineering";
                        break;
                    case "06":
                        branch = "Electronics Engineering";
                        break;
                    case "07":
                        branch = "Food Technology";
                        break;
                    case "08":
                        branch = "Information Technology";
                        break;
                    case "09":
                        branch = "Leather Technology";
                        break;
                    case "10":
                        branch = "Mechanical Engineering";
                        break;
                    case "11":
                        branch = "Oil Technology";
                        break;
                    case "12":
                        branch = "Paint And Leather";
                        break;
                    case "13":
                        branch = "Paint Technology";
                        break;
                }
                map.put("branch", branch);
                map.put("rollno",emailUserET.getText().toString().substring(0,9));
                map.put("cycleid", "");
                map.put("role", "student");
                // post request
                Call<Void> call = retrofitInterface.executeSignup(map);
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 201) {
                            Toast.makeText(AdminAddUser.this, "User Added Successfully",
                                    Toast.LENGTH_LONG).show();
                            nameUserET.setText("");
                            passwordUserET.setText("");
                            emailUserET.setText("");
//                            branchUserET.setText("");
//                            rollnoUserET.setText("");

                        } else{
                            Toast.makeText(AdminAddUser.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AdminAddUser.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}