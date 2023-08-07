package com.example.GreenRidersHBTU.RetrofitApiCalls;

import com.example.GreenRidersHBTU.Model.LoginResult;
import com.example.GreenRidersHBTU.Model.Student;
import com.example.GreenRidersHBTU.Scanners.scannerView;
import com.example.GreenRidersHBTU.Model.Cycle;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    String str = scannerView.cycleid;

    // Email and Password is stored in hashmap
    @POST("/users/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

//    @POST("/user/signup")
//    Call<Void> executeSignup (@Body HashMap<String, String> map);

    //    @GET(("/cycles/"+str)

    // Getting details of cycle having cycleid as params, it also requires authorization(in headers).
    @GET("/cycles/{cycleid}")
    Call<Cycle> getCycle(@Header("Authorization") String AUTH_KEY, @Path("cycleid") String cycleid);



    // Getting details of user having id as params, it also requires authorization(in headers).
    // map contains map.put("cycleid", cycleid) -> converted into JSON;
    @PATCH("/users/{id}")
    Call<Void> setRented(@Header("Authorization") String AUTH_KEY,@Path("id") String _id,
                         @Body HashMap<String, String> map);
    //        Updating Rented Status
    //        map.put("cycleid", ""), map.put("stdid", ""), map.put("stdname", ""), map.put("email", "");
    @PATCH("/users/{stdid}")
    Call<Void> removeRented(@Header("Authorization") String AUTH_KEY,@Path("stdid") String abc,
                            @Body HashMap<String, String> map);
    // Updating Status to rented
    //    map.put("status", "rented"), map.put("stdid", _id), map.put("stdname", name), map.put("email", email);
    @PATCH("/cycles/{cycleid}")
    Call<Void> setRentedUser(@Header("Authorization") String AUTH_KEY,@Path("cycleid") String cycleid,
                             @Body HashMap<String, String> map);

//    NOT USED
    @PATCH("/cycles/{cycleid}")
    Call<Void> removeRentedUser(@Header("Authorization") String AUTH_KEY,@Path("cycleid") String cycleid,
                                @Body HashMap<String, String> map);

    // SIGNING UP
    /*          map.put("name", nameSignUpET.getText().toString());
                map.put("password", password);
                map.put("email", emailSignUpET.getText().toString());
                map.put("branch", branchSignUpTV.getText().toString());
                map.put("rollno", rollnoSignUpTV.getText().toString());
                map.put("cycleid", "");
                map.put("role", "student");
    */
    @POST("/users/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    // ADDING cycle using QR

    // HashMap Contains empty cycle object
    // -> map.put("cycleid", qrTV.getText().toString()), map.put("status", ""),
    //    map.put("stdid", ""), map.put("stdname", ""), map.put("email", "");
    @POST("/cycles/add")
    Call<Void> executeCycleSignup(@Header("Authorization") String AUTH_KEY, @Body HashMap<String, String> map);

    // DELETE CYCLE
    @DELETE("/cycles/{cycleid}")
    Call<Void> executeDeleteCycle(@Header("Authorization") String AUTH_KEY,@Path("cycleid") String abc);


    // DELETE user
    @DELETE("/users/{email}")
    Call<Void> executeDelete(@Header("Authorization") String AUTH_KEY,@Path("email") String abc);


    // UPDATE password
    // HashMap contains new Password map.put("password", new Password);
    @PATCH("/users/password/{id}")
    Call<Void> executeChangePassword(@Header("Authorization") String AUTH_KEY,@Path("id") String abc,@Body HashMap<String, String> map);



    // GET all rented cycles
    @GET("/cycles/all")
    Call<List<Cycle>> getRentedCycle(@Header("Authorization") String AUTH_KEY);
//    @PATCH("cycles/{cycleid}")
//    Call<Cycle> getCycle(@Path("cycleid") String id);

    // DELETE users yearwise
    @DELETE("/users/byyear/{year}")
    Call<List<Student>> deleteYearWise(@Header("Authorization") String AUTH_KEY,@Path("year") String year);
}
