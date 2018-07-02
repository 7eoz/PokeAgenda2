package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sandrini.pokeagenda.model.Pokemon;
import com.example.sandrini.pokeagenda.model.Trainer;

public class WelcomeTrainerActivity extends AppCompatActivity {


    TextView welcomeMessage;
    ImageView favoritePokemonImage;
    Trainer trainer = new Trainer();
    Pokemon pokemon = new Pokemon();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_trainer);

        welcomeMessage = (TextView) findViewById(R.id.welcome_message);
        favoritePokemonImage = (ImageView) findViewById(R.id.favorite_pokemon_image);

        Intent intent = getIntent();
        trainer = (Trainer) intent.getSerializableExtra("trainer");
        //welcomeMessage.setText("Welcome, " + trainer.getUsername());
        //favoritePokemonImage.setImageResource(pokemon.getImage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome_trainer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_poke_button:
                Intent addPokeIntent = new Intent(WelcomeTrainerActivity.this, PokeFormActivity.class);
                startActivity(addPokeIntent);
                finish();
                return true;
            case R.id.search_poke_button:
                Intent searchPokeIntent = new Intent(WelcomeTrainerActivity.this, PokeListActivity.class);
                startActivity(searchPokeIntent);
                break;
            case R.id.list_poke_button:
                Intent listPokeIntent = new Intent(WelcomeTrainerActivity.this, PokeListActivity.class);
                startActivity(listPokeIntent);
                break;
            case R.id.leave_poke_button:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
