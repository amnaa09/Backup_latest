package com.example.sammrabatool.solutions5d.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.sammrabatool.solutions5d.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sammrabatool.solutions5d.R;
import com.example.sammrabatool.solutions5d.verification.VerificationCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
Button signup;
TextInputEditText userId, company, instance;
String userStr, companyStr, instanceStr, message, userID;
    boolean user_valid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        signup=(Button)findViewById(R.id.signup);
        userId=(TextInputEditText)findViewById(R.id.SignUp_userid);
        company=(TextInputEditText)findViewById(R.id.SignUp_company);
        instance=(TextInputEditText)findViewById(R.id.SignUp_instance);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userStr=userId.getText().toString();
                companyStr=company.getText().toString();
                instanceStr=instance.getText().toString();
              //  Toast.makeText(Signup.this, "data="+userStr+companyStr+instanceStr, Toast.LENGTH_SHORT).show();

                RequestQueue MyRequestQueue = Volley.newRequestQueue(Signup.this);

                String url = "http://"+instanceStr+".5dsurf.com/app/webservice/verifiyuserfirsttime/"+userStr+"/"+companyStr;

                StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.

                        try {
                            JSONObject data = new JSONObject(response.toString());
                                user_valid = data.getBoolean("valid_user");
                                message = data.getString("message");
                                userID=data.getString("user_id");

                            if(user_valid==true) {
                                user_valid=false;
                                //-----------------sharer ppref
                                Intent intent=new Intent(Signup.this, VerificationCode.class);
                                intent.putExtra("userID",userID);
                                intent.putExtra("instance",instanceStr);
                                startActivity(intent);
                            }

                            else {

                                Toast.makeText(Signup.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(Signup.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }




                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.

                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(Signup.this, "Error:"+message, Toast.LENGTH_SHORT).show();

                    }
                })



             {
                   /*   @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username", userStr); //Add the data you'd like to send to the server.
                        MyData.put("companycode", companyStr);

                   //     Toast.makeText(Signup.this, "in function", Toast.LENGTH_SHORT).show();
                        return MyData;
                    }



                   @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                       params.put("Content-Type", "application/json; charset=utf-8");
                       params.put("User-agent", "My useragent");

                        return params;
                    }
                    */
                };

                MyStringRequest.setShouldCache(false);
                MyRequestQueue.add(MyStringRequest);

            }
        });
    }
}
