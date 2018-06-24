package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PokeDetailActivity extends AppCompatActivity {

    Pokemon pokemon = new Pokemon();
    int pokeId;

    TextView pokemonName, pokemonSpecies, pokemonWeight, pokemonHeight, pokemonTrainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_detail);

        pokemonName = (TextView) findViewById(R.id.pokemon_name_detail);
        pokemonSpecies = (TextView) findViewById(R.id.pokemon_species_detail);
        pokemonWeight = (TextView) findViewById(R.id.pokemon_weight_detail);
        pokemonHeight = (TextView) findViewById(R.id.pokemon_height_detail);
        pokemonTrainer = (TextView) findViewById(R.id.pokemon_trainer_detail);


        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if (params != null){
                pokeId = params.getInt("pokemonId");

                pokemonName.setText(pokemon.getName());
                pokemonSpecies.setText(pokemon.getSpecies());
                pokemonWeight.setText(pokemon.getWeight());
                pokemonHeight.setText(pokemon.getHeight());
                pokemonTrainer.setText(pokemon.getTrainerId());

            }
        }
    }
}
