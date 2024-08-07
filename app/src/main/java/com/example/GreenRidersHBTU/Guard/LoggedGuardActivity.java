package com.example.GreenRidersHBTU.Guard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.Model.Cycle;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.example.GreenRidersHBTU.Scanners.scannerView;
import com.example.GreenRidersHBTU.User.LoggedUserActivity;
import com.example.GreenRidersHBTU.Util.AppInfo;
import com.example.GreenRidersHBTU.Util.ChangePassword;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedGuardActivity extends AppCompatActivity {

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    boolean statusOfCycle = false;

    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();
    String _id;
    TextView scanbtn, returnbtn;

    LinearLayout scanLL, returnLL;
    public static TextView cycleidTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_guard);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing


        Intent intent = getIntent();

        _id = intent.getStringExtra("_id");
        String email = intent.getStringExtra("email");

        String name = intent.getStringExtra("name");

        TextView emailTV = (TextView) findViewById(R.id.emailTV);
        emailTV.setText(email);
        TextView nameTV = (TextView) findViewById(R.id.nameTV);
        nameTV.setText(name);
        cycleidTV = (TextView) findViewById(R.id.cycleidTV);
//        cycleidTV.setText(cycleid);

        returnbtn = (TextView) findViewById(R.id.returnbtn);


        returnLL = (LinearLayout) findViewById(R.id.returnLL);
        scanLL = (LinearLayout) findViewById(R.id.scanLL);


        returnLL.setVisibility(View.INVISIBLE);

        cycleidTV = (TextView) findViewById(R.id.cycleidTV);
        scanbtn = (TextView) findViewById(R.id.scanbtn);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedGuardActivity.this, "Button Dababa",
//                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), scannerView.class);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2
//                if(!cycleidTV.getText().equals("Scan To Remove From Rent.")){
//                    Toast.makeText(LoggedGuardActivity.this, "Lag Gaye",
//                            Toast.LENGTH_SHORT).show();
//                    returnbtn.setVisibility(View.VISIBLE);
//                    scanbtn.setVisibility(View.INVISIBLE);
//                    returnLL.setVisibility(View.VISIBLE);
//                    scanLL.setVisibility(View.INVISIBLE);
//                }

            }
        });

        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedGuardActivity.this, "Return Button is Pressed",
//                        Toast.LENGTH_LONG).show();

                String cycleid = (String) cycleidTV.getText();
//                Toast.makeText(LoggedGuardActivity.this, cycleid, Toast.LENGTH_LONG).show();

                if (!cycleid.equals(""))

                    getCycleHandler(cycleid);
////                if(statusOfCycle)


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
//            RETRUNING FROM SCANNERVIEW since reqestCode is 2 -> logic for visibility of button
            if(!cycleidTV.getText().equals("Scan To Remove From Rent.")){
                scanbtn.setVisibility(View.INVISIBLE);
                scanLL.setVisibility(View.INVISIBLE);
                returnLL.setVisibility(View.VISIBLE);
                returnbtn.setVisibility(View.VISIBLE);
            }
        }
    }


    //
//    }
    private void getCycleHandler(String cycleid) {
//        Toast.makeText(LoggedGuardActivity.this, "get cycle me ghusa", Toast.LENGTH_SHORT).show();
        // post request
        Call<Cycle> call = retrofitInterface.getCycle("Bearer "+ MainActivity.AUTH_TOKEN,cycleid);
        // execute http request
        call.enqueue(new Callback<Cycle>() {
            @Override
            public void onResponse(Call<Cycle> call, Response<Cycle> response) {
                if (response.code() == 200) {
                    Cycle result = response.body();
//                    Intent intent = new Intent(LoggedUserActivity.this, LoggedUserActivity.class);
//                        Log.i("SURFYANSH",result.toString());
//                    String _id = result.get_id();
                    String cycleid = result.getCycleid();
                    String status = result.getStatus();
                    String stdid = result.getStdid();
//                    _id = stdid;
//                    Toast.makeText(LoggedGuardActivity.this, status+" "+cycleid+" "+stdid,
//                            Toast.LENGTH_LONG).show();
                    if (status.equals("rented")) {
                        // AGAR RENTED NAHI HAI
//                        statusOfCycle = true;
                        scanLL.setVisibility(View.VISIBLE);
                        scanbtn.setVisibility(View.VISIBLE);

                        returnLL.setVisibility(View.INVISIBLE);

                        removeRentedHandler(stdid);
                        removeRentedUserHandler(cycleid);
//                        Toast.makeText(LoggedGuardActivity.this, "Rented Hai", Toast.LENGTH_SHORT).show();

                    } else {
//                        Toast.makeText(LoggedGuardActivity.this, "Cycle Rented Nahi Hai", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedGuardActivity.this);
                        builder.setTitle("ALERT");
                        builder.setMessage("This Cycle Is Not Rented Yet");
                        builder.show();

                        scanLL.setVisibility(View.VISIBLE);
                        scanbtn.setVisibility(View.VISIBLE);

                        returnLL.setVisibility(View.INVISIBLE);

                    }
//                    startActivity(intent);
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedGuardActivity.this, "Wrong Credentials",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(LoggedGuardActivity.this, "Some Error Occured",
                            Toast.LENGTH_SHORT).show();
                }

                cycleidTV.setText(" Scan To Remove From Rent. ");
            }

            @Override
            public void onFailure(Call<Cycle> call, Throwable t) {
                Toast.makeText(LoggedGuardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    private void removeRentedHandler(String stdid) {

//        Toast.makeText(LoggedGuardActivity.this, "inside setRentedCycle",
//                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("cycleid", "");
        map.put("stdid", "");
        map.put("stdname", "");
        map.put("email", "");
//        // post request
        Call<Void> call = retrofitInterface.removeRented("Bearer "+ MainActivity.AUTH_TOKEN,stdid, map);
        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(LoggedGuardActivity.this, "Cycle Returned Successfully",
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedGuardActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoggedGuardActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoggedGuardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    //
    private void removeRentedUserHandler(final String cycleid) {

//        Toast.makeText(LoggedGuardActivity.this, "inside setRentedCycle",
//                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("status", "");
        map.put("stdid", "");
        map.put("stdname", "");
        map.put("email", "");
//        // post request
        Call<Void> call = retrofitInterface.setRentedUser("Bearer "+MainActivity.AUTH_TOKEN,cycleid,map);
        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
//                    Toast.makeText(LoggedGuardActivity.this, "Jerk Off",
//                            Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedGuardActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoggedGuardActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoggedGuardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
//            super.onBackPressed();
        finishAffinity();
        finish();
    }
    // Adding Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mailIt:

                Toast.makeText(this,"Generating Mail",Toast.LENGTH_SHORT).show();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","admincycleapp1@hbtu.ac.in", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                return true;
            case R.id.logout:
                MainActivity.AUTH_TOKEN = "";
                startActivity(new Intent(LoggedGuardActivity.this, MainActivity.class));
                SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();

                Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();
              return true;
            case R.id.appinfo:
                startActivity(new Intent(LoggedGuardActivity.this, AppInfo.class));
                Toast.makeText(this,"App Info",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.changePasswordMenu:
                startActivity(new Intent(LoggedGuardActivity.this, ChangePassword.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}