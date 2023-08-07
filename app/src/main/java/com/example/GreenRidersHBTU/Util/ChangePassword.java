package com.example.GreenRidersHBTU.Util;
// COMMENTS ADDED
import static com.example.GreenRidersHBTU.SignUpUser.isValidPassword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Admin.AdminHome;
import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.example.GreenRidersHBTU.SignUpUser;
import com.google.android.gms.auth.api.Auth;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePassword extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();

    TextView changePasswordET, changeReEnterPasswordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // back button to close this activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changePasswordET = (TextView)  findViewById(R.id.changePasswordET);
        changeReEnterPasswordET = (TextView)  findViewById(R.id.changeReEnterPasswordET);
//        BUILDING RETROFIT OBJECT
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();


        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        changePasswordHandler();

    }
    // back button to close Home
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
//    PASSWORD CHANGE HANDLER
    private void changePasswordHandler() {
        final LinearLayout changePasswordLL = (LinearLayout) findViewById(R.id.changePasswordLL);
        changePasswordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = changePasswordET.getText().toString();
                String passwordReEnter = changeReEnterPasswordET.getText().toString();
                if(!passwordReEnter.equals(password)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                    builder.setTitle("Invalid Password");
                    builder.setMessage("Password does not match");
                    builder.setCancelable(true);
                    builder.show();
                    changePasswordET.setText("");
                    changeReEnterPasswordET.setText("");
                    changePasswordET.setError("Password does not match");
                    changeReEnterPasswordET.setError("Password does not match");
                    return;
                }
                //        make sure password is of atleast length 8 and contains 1 special character
                if(!isValidPassword(password)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                    builder.setTitle("Password is too weak");
                    builder.setMessage("Make sure password is of atleast length 8, contains 1 special character, 1 digit and 1 Capital Letter");
                    builder.setCancelable(true);
                    builder.show();
                    changePasswordET.setText("");
                    changeReEnterPasswordET.setText("");
                    changePasswordET.setError("Password is too weak");
                    changeReEnterPasswordET.setError("Password is too weak");
                    return;
                }
                Toast.makeText(ChangePassword.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("password", changePasswordET.getText().toString());
                // post request
                Call<Void> call = retrofitInterface.executeChangePassword("Bearer "+ MainActivity.AUTH_TOKEN,MainActivity._id,map);
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            startActivity(new Intent(ChangePassword.this, MainActivity.class));
                            SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            Toast.makeText(ChangePassword.this, "Password Changed Successfully, Now Login with new Password",
                                    Toast.LENGTH_LONG).show();
                            changePasswordET.setText("");
                            finishAffinity();
                            finish();
                        } else if (response.code() == 404) {
                            Toast.makeText(ChangePassword.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ChangePassword.this, "Some Error Occured",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ChangePassword.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}