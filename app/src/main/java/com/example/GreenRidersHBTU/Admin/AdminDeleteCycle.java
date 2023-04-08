package com.example.GreenRidersHBTU.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.example.GreenRidersHBTU.Scanners.ScannerDeleteCycle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminDeleteCycle extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface

    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();

    public static  TextView qrDeleteTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_delete_cycle);
//        // back button to close DeleteCycle
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainActivity.addCycle = true;
        qrDeleteTV = (TextView)  findViewById(R.id.qrDeleteTV);
        findViewById(R.id.QRLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedUserActivity.this, "Button Dababa",
//                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(AdminDeleteCycle.this, ScannerDeleteCycle.class));
//                rentbtn.setVisibility(View.VISIBLE);
//                scanbtn.setVisibility(View.INVISIBLE);
//                rentLL.setVisibility(View.VISIBLE);
//                scanLL.setVisibility(View.INVISIBLE);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        deleteCycleHandler();

    }
    // back button to close AdminHome
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
    private void deleteCycleHandler() {
        LinearLayout addCycleLL = (LinearLayout) findViewById(R.id.addCycleLL);
        addCycleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminDeleteCycle.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();

                // post request
                Call<Void> call = retrofitInterface.executeDeleteCycle("Bearer "+ MainActivity.AUTH_TOKEN,qrDeleteTV.getText().toString());
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Toast.makeText(AdminDeleteCycle.this, "Cycles Deleted Successfully",
                                    Toast.LENGTH_LONG).show();
                            qrDeleteTV.setText("");

                        } else if (response.code() == 404) {
                            Toast.makeText(AdminDeleteCycle.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(AdminDeleteCycle.this, "Some Error Occured",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AdminDeleteCycle.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}