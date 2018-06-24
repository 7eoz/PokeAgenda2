package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    public static final String REQUEST_TAG =  "UserAutentication";
    private RequestQueue mQueue;
    EditText loginInput, pwdInput;
    Button loginButton;
    TextView signUpButton;
    Boolean registered;
    Trainer trainer = new Trainer();
    private static final String URL = "http://localhost:8081/PokedexWS/webresources/pokews/poke/login";
    CustomJSONObjectRequest jsonRequest;
    ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginInput = (EditText) findViewById(R.id.login_input);
        pwdInput = (EditText) findViewById(R.id.pwd_input);
        loginButton = (Button) findViewById(R.id.login_button);
        signUpButton = (TextView) findViewById(R.id.signup_button);
        progressBarLogin = (ProgressBar) findViewById(R.id.progress_bar_login);

        progressBarLogin.setVisibility(View.INVISIBLE);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, insert your login", Toast.LENGTH_SHORT).show();
                } else if (pwdInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please, insert yout password", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://192.168.25.6:8081/PokedexWS/webresources/pokews/poke/login/" +
                            loginInput.getText().toString() + "/" + pwdInput.getText().toString();
                    mQueue = CustomVolleyRequestQueue.getmInstance(LoginActivity.this.getApplicationContext()).getRequestQueue();
                    jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(),
                            LoginActivity.this, LoginActivity.this);
                    mQueue.add(jsonRequest);
                    progressBarLogin.setVisibility(View.VISIBLE);
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
        progressBarLogin.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        Log.e("error", error.getMessage());
    }

    @Override
    public void onResponse(Object trainerObject) {
        progressBarLogin.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
        try{
            JSONObject trainerJson = new JSONObject();
            trainerJson = (JSONObject) trainerObject;
            trainer.setId(trainerJson.getInt("id"));
            trainer.setUsername(trainerJson.getString("username"));
            trainer.setPassword(trainerJson.getString("password"));
            trainer.setEmail(trainerJson.getString("email"));
            if(trainer != null){
                Intent intent = new Intent(LoginActivity.this, WelcomeTrainerActivity.class);
                intent.putExtra("trainer", trainer);
                startActivity(intent);
                finish();
            };
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
