package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import java.util.List;

public class PokeListActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {

    private ListView pokemonList;
    private ProgressBar progressBarList;
    private List<Pokemon> pokemons = new ArrayList<>();
    private PokemonListItem pokemonListItem;
    private Gson pokeGson = new Gson();
    private RequestQueue requestQueue;
    private final String KEY_URL = "http://192.168.25.11:8081/PokedexWS/ws/pokews/poke/list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);

        pokemonList = (ListView) findViewById(R.id.pokemon_list);
        progressBarList = (ProgressBar) findViewById(R.id.progress_bar_list);

        requestQueue = Volley.newRequestQueue(PokeListActivity.this);

        progressBarList.setVisibility(View.VISIBLE);

        loadPokemon();

        /*pokemonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PokeListActivity.this, EditPokeActivity.class);
                Bundle params = new Bundle();
                intent.putExtras(params);
                startActivity(intent);
            }
        });*/


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

    // Método de requisição do WS buscando a lista de pokemon disponível no mesmo. Preenche um List<Pokemon> e passa-o para o PokemonListAdapter renderizar a lista customizada.
    // Enquanto carrega a lista para o List<Pokemon>, mostra um dialog.
    public void loadPokemon(){
        StringRequest request = new StringRequest(Request.Method.GET, KEY_URL, PokeListActivity.this, PokeListActivity.this );
                /*new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            // Se ocorrer erro na requisição ao WS, informa via Toast que ocorreu um erro. Loga o erro na console.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Ops! Ocorreu um erro!", Toast.LENGTH_LONG).show();
                Log.e("PokemonListActivity", error.getLocalizedMessage());
            }
        });*/
        requestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBarList.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(),"error: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object pokeObjects) {
        progressBarList.setVisibility(View.INVISIBLE);
        Log.i("response", pokeObjects.toString());
        try{
            JSONArray pokeJsonArray = new JSONArray(pokeObjects.toString());
            JSONObject pokeJson;
            Pokemon poke = new Pokemon();
            if (pokeJsonArray != null) {
                for (int i=0;i<pokeJsonArray.length();i++){
                    pokeJson = pokeJsonArray.getJSONObject(i);
                    poke.setName(pokeJson.getString("name"));
                    poke.setSpecies(pokeJson.getString("species"));
                    //poke.setImage(pokeJson.getString("image"));
                    pokemons.add(poke);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        pokemonListItem = new PokemonListItem(PokeListActivity.this, pokemons);
        pokemonList.setAdapter(pokemonListItem);
    }
}
