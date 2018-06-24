package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PokeListActivity extends AppCompatActivity {

    ListView pokemonList;
    String[] name = {"Bulbassaur", "Charmander", "Squartle"};
    String[] species = {"Pokemon Semente", "Pokemon Lagarto", "Pokemon Mini Tartaruga"};
    Integer[] image = new Integer[5];
    List<Pokemon> pokemons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);

        pokemonList = (ListView) findViewById(R.id.pokemon_list);

        int i = 0;
        for(Pokemon p:pokemons){
            name[i] = p.getName();
            species[i] = p.getSpecies();
        }

        ListItem pokemonAdapter = new ListItem(PokeListActivity.this, name, species, image);
        pokemonList.setAdapter(pokemonAdapter);

        pokemonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PokeListActivity.this, EditPokeActivity.class);
                Bundle params = new Bundle();
                intent.putExtras(params);
                startActivity(intent);
            }
        });


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
                Intent intent = new Intent(this, EditPokeActivity.class);
                startActivity(intent);
                break;
            case R.id.search_poke_button:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
