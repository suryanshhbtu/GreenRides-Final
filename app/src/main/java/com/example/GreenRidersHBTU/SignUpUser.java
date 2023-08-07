package com.example.GreenRidersHBTU;
// COMMENTS ADDED
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Model.BaseUrl;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.example.GreenRidersHBTU.User.LoggedUserActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpUser extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private BaseUrl baseUrl = new BaseUrl();
    private String BASE_URL = baseUrl.getBaseUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
//        addUserHandler();


        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        // to hide keyboard on screen tap
        findViewById(R.id.screen).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(SignUpUser.this);
            }
        });
    }
    @Override
    protected void onStart() {
        // signin using goolge
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // HANDLING SIGNIN
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            if(account == null || account.getEmail().length() < 11 || !account.getEmail().substring(account.getEmail().length()-11).contains("@hbtu.ac.in")){
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpUser.this);
                builder.setTitle("ALERT");
                builder.setMessage("You Tried Signup Using Non-HBTU Email \n 1) Uninstall the app \n 2) Re-install It" +
                        "\n 3) Make sure that use to SignUp");
                builder.setCancelable(false);
                builder.show();
            }else{
            final TextView nameSignUpTV = (TextView) findViewById(R.id.nameSignUpTV);
            final TextView branchSignUpTV = (TextView) findViewById(R.id.branchSignUpTV);
            final TextView rollnoSignUpTV = (TextView) findViewById(R.id.rollNoSignUpTV);
            final TextView emailSignUpTV = (TextView) findViewById(R.id.emailSignUpTV);
            final EditText passwordSignUpET = (EditText) findViewById(R.id.passwordSignUpET);

            String name = "", branch = "";

            name = account.getDisplayName();
            rollnoSignUpTV.setText(account.getEmail().substring(0,9));
            nameSignUpTV.setText(name.substring(0, name.length()-31));
            emailSignUpTV.setText(account.getEmail());
//            Uri photoUri = account.getPhotoUrl();

            Toast.makeText(SignUpUser.this, "Enter Your New Password",
                    Toast.LENGTH_LONG).show();
            String curr = account.getEmail().substring(4,6);
            // GETTING BRANCH -> can be called from CYCLE MODEL
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
            branchSignUpTV.setText(branch);
            addUserHandler();
        }}
    }

    private void gotoMainActivity(){
        Toast.makeText(SignUpUser.this, "Please Select A vaild",
                Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("valid", "false");
        this.startActivity(intent);
    }

    // USED BY handleSignInResult() -> actual signup Happens here
    private void addUserHandler() {
        LinearLayout addUserLL = (LinearLayout) findViewById(R.id.signUpLL);
        final TextView nameSignUpET = (TextView) findViewById(R.id.nameSignUpTV);
        final TextView branchSignUpTV = (TextView) findViewById(R.id.branchSignUpTV);
        final TextView rollnoSignUpTV = (TextView) findViewById(R.id.rollNoSignUpTV);
        final TextView emailSignUpET = (TextView) findViewById(R.id.emailSignUpTV);
        final EditText passwordSignUpET = (EditText) findViewById(R.id.passwordSignUpET);
        final EditText passwordReEnterSignUpET = (EditText) findViewById(R.id.passwordReEnterSignUpET);
        addUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = passwordSignUpET.getText().toString();
                String passwordReEnter = passwordReEnterSignUpET.getText().toString();
                if(!passwordReEnter.equals(password)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpUser.this);
                    builder.setTitle("Invalid Password");
                    builder.setMessage("Password does not match");
                    builder.setCancelable(true);
                    builder.show();
                    passwordSignUpET.setText("");
                    passwordReEnterSignUpET.setText("");
                    passwordSignUpET.setError("Password does not match");
                    passwordReEnterSignUpET.setError("Password does not match");
                    return;
                }
                //        make sure password is of atleast length 8 and contains 1 special character
                if(!isValidPassword(password)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpUser.this);
                    builder.setTitle("Password is too weak");
                    builder.setMessage("Make sure password is of atleast length 8, contains 1 special character, 1 digit and 1 Capital Letter");
                    builder.setCancelable(true);
                    builder.show();
                    passwordSignUpET.setText("");
                    passwordReEnterSignUpET.setText("");
                    passwordSignUpET.setError("Password is too weak");
                    passwordReEnterSignUpET.setError("Password is too weak");
                    return;
                }
                Toast.makeText(SignUpUser.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("name", nameSignUpET.getText().toString());
                map.put("password", password);
                map.put("email", emailSignUpET.getText().toString());
                map.put("branch", branchSignUpTV.getText().toString());
                map.put("rollno", rollnoSignUpTV.getText().toString());
                map.put("cycleid", "");
                map.put("role", "student");
                // post request
                Call<Void> call = retrofitInterface.executeSignup(map);
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 201) {
                            nameSignUpET.setText("");
                            passwordSignUpET.setText("");
                            emailSignUpET.setText("");
//                            branchSignUpET.setText("");
//                            rollnoSignUpET.setText("");
                            Intent i = new Intent(SignUpUser.this, MainActivity.class);
                            startActivity(i);
                            Toast.makeText(SignUpUser.this, "Sign Up Successful, Now You Can Login",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUpUser.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SignUpUser.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    checking that password is strong
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
                "(?=.*[@#$%^&+=])" +     // at least 1 special character
                "(?=\\S+$)" +            // no white spaces
                ".{4,}" +                // at least 4 characters
                "$");
        pattern = Pattern.compile(PASSWORD_PATTERN.toString());
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

//    screen tap keyboard close
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}