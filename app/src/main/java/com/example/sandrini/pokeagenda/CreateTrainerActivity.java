package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class CreateTrainerActivity extends AppCompatActivity /*implements Response.Listener, Response.ErrorListener*/{

    private EditText registerUsername;
    private EditText registerEmail;
    private EditText registerPassword;
    private Button registerTrainerButton;
    private ProgressBar registerTrainerProgressbar;
    private static final String KEY_URL = "http://192.168.25.11:8081/PokedexWS/ws/pokews/trainer/insert";
    private String requestBody;
    private Gson trainerGson;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trainer);

        registerUsername = (EditText) findViewById(R.id.register_username);
        registerEmail = (EditText) findViewById(R.id.register_email);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerTrainerButton = (Button) findViewById(R.id.register_trainer_button);
        registerTrainerProgressbar = (ProgressBar) findViewById(R.id.register_trainer_progressbar);

        requestQueue = Volley.newRequestQueue(this);

        registerTrainerProgressbar.setVisibility(View.INVISIBLE);

        registerTrainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTrainer();
                registerTrainerProgressbar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void registerTrainer(){
        final String username = registerUsername.getText().toString();
        final String email = registerEmail.getText().toString();
        final String password = registerPassword.getText().toString();
        Trainer trainer = new Trainer(0, username,email, password);

        try {
                trainerGson = new Gson();
                // Adiciona o objeto Pokemon no body da requisição do WS.
                requestBody = trainerGson.toJson(trainer);
                StringRequest request = new StringRequest(Request.Method.POST, KEY_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        registerTrainerProgressbar.setVisibility(View.INVISIBLE);
                        if(response.equals("true")){
                            Toast.makeText(getApplicationContext(), "Registered successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CreateTrainerActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(response.equals("false")){
                            Toast.makeText(getApplicationContext(), "Sorry, but user already exists!", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        registerTrainerProgressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Register failed!", Toast.LENGTH_LONG).show();
                    }
                })  {
                    // Define o body da requisição como JSON.
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    // Define o body da requisição como JSON.
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            return null;
                        }
                    }
                };
                requestQueue.add(request);
                registerTrainerProgressbar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

