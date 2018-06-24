package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private static final String REQUEST_TAG = "CreateUser";
    Trainer trainer = new Trainer();
    EditText loginCreateInput, emailCreateInput, pwdCreateInput;
    Button createUserButton;
    private RequestQueue mQueue;
    JsonRequest jsonRequest;
    ProgressBar progressBarCreateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        loginCreateInput = (EditText) findViewById(R.id.login_create_input);
        emailCreateInput = (EditText) findViewById(R.id.email_create_input);
        pwdCreateInput = (EditText) findViewById(R.id.pwd_create_input);
        createUserButton = (Button) findViewById(R.id.create_user_button);
        progressBarCreateUser = (ProgressBar) findViewById(R.id.progress_bar_create_user);

        progressBarCreateUser.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginCreateInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, set a login", Toast.LENGTH_SHORT).show();
                } else if (emailCreateInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, set an email", Toast.LENGTH_SHORT).show();
                } else if (pwdCreateInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, set a password", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://192.168.25.6:8081/PokedexWS/webresources/pokews/poke/insert/trainer";
                    /*trainer.setUsername(loginCreateInput.getText().toString());
                    trainer.setEmail(emailCreateInput.getText().toString());
                    trainer.setPassword(pwdCreateInput.getText().toString());*/

                    trainer.setUsername("gato");
                    trainer.setEmail("gato");
                    trainer.setPassword("gato");

                    Map<String, String> params = new HashMap();
                    params.put("username", trainer.getUsername());
                    params.put("email", trainer.getEmail());
                    params.put("password", trainer.getPassword());
                    params.put("Content-Type", "application/json; charset=utf-8");

                    try {
                        JSONObject jsonObject;
                        Gson gson = new Gson();
                        String trainerToGson = gson.toJson(trainer);
                        jsonObject = new JSONObject(trainerToGson);
                        //String trainerToGson = "{\"username\":"+ "\"" + trainer.getUsername()+ "\"" + "," +
                        //"\"email\":"+ "\"" + trainer.getEmail() + "\"" + "," +
                        //"\"username\":"+ "\"" + trainer.getPassword()+ "\"" + "}";
                        //createUserButton.setText(trainerToGson);


                        mQueue = CustomVolleyRequestQueue.getmInstance(CreateUserActivity.this.getApplicationContext()).getRequestQueue();
                        /*jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObject,
                                CreateUserActivity.this, CreateUserActivity.this);
                       jsonRequest.setTag(REQUEST_TAG);
                        mQueue.add(jsonRequest);
                        progressBarCreateUser.setVisibility(View.VISIBLE);*/

                        List<Trainer> trainerList = new ArrayList<>();
                        int i = 0;
                        Trainer newTrainer = new Trainer();
                        newTrainer = trainerList.get(i);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        progressBarCreateUser.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object trainerObject) {
        progressBarCreateUser.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateUserActivity.this, WelcomeTrainerActivity.class);
                startActivity(intent);
                finish();
    }
}
