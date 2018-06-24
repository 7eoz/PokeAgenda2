package com.example.sandrini.pokeagenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditPokeActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {

    EditText pokemonNameEdit, pokemonSpeciesEdit, pokemonWeightEdit, pokemonHeightEdit;
    ImageView pokemonImageEdit;
    Button pokemonImageButton;
    Trainer trainer = new Trainer();
    Pokemon pokemon = new Pokemon();
    List<Pokemon> pokemons = new ArrayList<>();
    private static final int POKEMON_IMAGE_TAKE = 100;
    private static final int POKEMON_IMAGE_PICK = 101;
    Uri imageUri;
    String imagePath;
    PokemonHelper pokemonHelper;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG =  "GetPokemon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poke);

        pokemonHelper = new PokemonHelper(this);

        Intent intent = getIntent();
        if(intent != null) {
            Bundle params = intent.getExtras();
            if(params != null) {
                int pokeId = params.getInt("pokeId");
            }
        }

        /*pokemonNameEdit = (EditText) findViewById(R.id.pokemon_name_edit);
        pokemonSpeciesEdit = (EditText) findViewById(R.id.pokemon_species_edit);
        pokemonWeightEdit = (EditText) findViewById(R.id.pokemon_weight_edit);
        pokemonHeightEdit = (EditText) findViewById(R.id.pokemon_height_edit);
        pokemonImageEdit = (ImageView) findViewById(R.id.pokemon_image_edit);
        pokemonImageButton = (Button) findViewById(R.id.pokemon_image_button);
        */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA} , 123);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE} , 234);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE} , 345);
        }

        pokemonImageButton.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

                final MenuItem camera = menu.add("Camera");
                camera.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        openCamera();
                        return false;
                    }

                });


                final MenuItem gallery = menu.add("Gallery");
                gallery.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        openGalery();
                        return false;
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getmInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://192.168.25.34:8081/PokedexWS/webresources/pokews/poke/poke/fetch/";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(),
                this, this);
        mQueue.add(jsonRequest);
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
        Log.e("error", error.getMessage());
    }

    @Override
    public void onResponse(Object pokeObject) {
        try{
            Pokemon pokemon = new Pokemon();
            JSONObject pokeJson;
            pokeJson = (JSONObject) pokeObject;
            pokemon.setId(pokeJson.getInt("id"));
            pokemon.setName(pokeJson.getString("name"));
            pokemon.setWeight(pokeJson.getString("weight"));
            pokemon.setHeight(pokeJson.getString("height"));
            pokemon.setFavorite(pokemon.isFavorite());
            pokemon.setTrainerId(pokeJson.getInt("trainerid"));
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void openCamera() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = getExternalFilesDir(null) + "/" + System.currentTimeMillis();
        File imageFile = new File(imagePath);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(imageIntent, POKEMON_IMAGE_TAKE);
    }

    private void openGalery() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        imagePath = getExternalFilesDir(null) + "/" + System.currentTimeMillis();
        File imageFile = new File(imagePath);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(imageIntent, POKEMON_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == POKEMON_IMAGE_TAKE) {
            pokemonHelper.loadImage(imagePath);
        }

        else if (resultCode == RESULT_OK && requestCode == POKEMON_IMAGE_TAKE) {
            pokemonHelper.loadImage(imagePath);
        }
    }

}
