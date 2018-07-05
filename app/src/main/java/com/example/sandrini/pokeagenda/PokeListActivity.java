package com.example.sandrini.pokeagenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sandrini.pokeagenda.adapter.PokemonListItem;
import com.example.sandrini.pokeagenda.model.Pokemon;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokeListActivity extends AppCompatActivity {

    private ListView pokemonList;
    private ProgressBar progressBarList;
    private List<Pokemon> pokemons = new ArrayList<>();
    private PokemonListItem pokemonListItem;
    private Gson pokeGson = new Gson();
    private RequestQueue requestQueue;
    private final String KEY_URL = "http://10.0.2.2:8081/PokedexWS/ws/pokews/poke/list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);

        progressBarList = (ProgressBar) findViewById(R.id.progress_bar_list);
        requestQueue = Volley.newRequestQueue(this);
        loadPokemon();

        progressBarList.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_poke_button:
                Intent intent = new Intent(this, PokeFormActivity.class);
                startActivity(intent);
                break;
            case R.id.search_poke_button:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadPokemon(){
        final Gson pokeGson = new Gson();
        StringRequest request = new StringRequest(Request.Method.GET, KEY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("PokemonListActivity", response);
                pokemons = Arrays.asList(pokeGson.fromJson(response, Pokemon[].class));
                pokemonList = (ListView)findViewById(R.id.pokemon_list);
                PokemonListItem pokemonListItem = new PokemonListItem(getApplicationContext(), pokemons);
                pokemonList.setAdapter(pokemonListItem);
                progressBarList.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            // Se ocorrer erro na requisição ao WS, informa via Toast que ocorreu um erro. Loga o erro na console.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

}