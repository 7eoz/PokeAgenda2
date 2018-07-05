package com.example.sandrini.pokeagenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sandrini.pokeagenda.helper.PokemonImageHelper;
import com.example.sandrini.pokeagenda.model.Pokemon;
import com.example.sandrini.pokeagenda.model.Trainer;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PokeFormActivity extends AppCompatActivity {

    private static final String KEY_URL = "http://10.0.2.2:8081/PokedexWS/ws/pokews/poke/insert";
    private RequestQueue requestQueue;
    private String requestBody;
    private Gson gson;
    private int GALLERY_REQUEST = 123;
    private int CAMERA_REQUEST = 234;
    private int SAVE_PHOTO_REQUEST = 345;

    private ImageView pokeImage;
    private Button pokeImageButton;
    private String pokeImagePath;

    private EditText pokeNameEdit;
    private EditText pokeSpeciesEdit;
    private EditText pokeWeightEdit;
    private EditText pokeHeightEdit;
    private RatingBar pokeIsFavorite;

    private Pokemon pokemon;
    private Trainer trainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_form);

        pokeNameEdit = (EditText)findViewById(R.id.pokemon_name_edit);
        pokeSpeciesEdit = (EditText)findViewById(R.id.pokemon_species_edit);
        pokeWeightEdit = (EditText)findViewById(R.id.pokemon_weight_edit);
        pokeHeightEdit = (EditText)findViewById(R.id.pokemon_height_edit);
        pokeImage = (ImageView) findViewById(R.id.pokemon_image_edit);
        pokeImageButton = (Button) findViewById(R.id.pokemon_image_button);
        pokeIsFavorite = (RatingBar) findViewById(R.id.pokemon_favorite_edit);

        Intent intent = getIntent();
        trainer = (Trainer) intent.getSerializableExtra("trainer");

        pokemon = new Pokemon();

        requestQueue = Volley.newRequestQueue(this);

        pokeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(pokeImageButton);
                openContextMenu(pokeImageButton);
            }
        });

    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_edit_poke_menu, menu);
        return true;
    }

    // Opções do Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_poke_button:
                addPokemon();
                Intent saveIntent = new Intent(PokeFormActivity.this, WelcomeTrainerActivity.class);
                startActivity(saveIntent);
                finish();
                return true;
            case R.id.cancel_poke_button:
                Intent cancelIntent = new Intent(PokeFormActivity.this, WelcomeTrainerActivity.class);
                startActivity(cancelIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        final MenuItem camera = menu.add("Camera");
        camera.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ActivityCompat.checkSelfPermission(PokeFormActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PokeFormActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                }
                if (ActivityCompat.checkSelfPermission(PokeFormActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PokeFormActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_PHOTO_REQUEST);
                }
                //dispatchTakePictureIntent();
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pokeImagePath = getExternalFilesDir(null) + "/" + System.currentTimeMillis();
                File imageFile = new File(pokeImagePath);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(imageIntent, CAMERA_REQUEST);

                return false;
            }
        });
        final MenuItem gallery = menu.add("Gallery");
        gallery.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ActivityCompat.checkSelfPermission(PokeFormActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PokeFormActivity.this,
                            new String[]{Manifest.permission.CAMERA}, GALLERY_REQUEST);
                }
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

                return false;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    pokeImage.setImageBitmap(bitmap);
                    pokemon.setImage(PokemonImageHelper.getStringFromBitmap(bitmap));
                } catch (IOException e) {
                    Log.e("PokemonAddActivity",e.getLocalizedMessage());
                }
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if(pokeImagePath != null){
                Bitmap bitmap = BitmapFactory.decodeFile(pokeImagePath);
                Bitmap minimizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                pokeImage.setImageBitmap(minimizedBitmap);
                pokeImage.setScaleType(ImageView.ScaleType.FIT_XY);
                pokeImage.setTag(pokeImagePath);
            }
        }
    }


    public void addPokemon(){

        // Aqui passo o login (APENAS) para o cadastro do pokemon.
        //this.pokemon.setLoginTreinador(this.sharedPreferences.getString("treinador_login",null));

        if(pokeImage == null){
            Toast.makeText(getApplicationContext(),"Please, set a pokemon picture", Toast.LENGTH_SHORT).show();
        }else if(pokeNameEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please, set a pokemon name", Toast.LENGTH_SHORT).show();
        } else if(pokeSpeciesEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please, set a pokemon species", Toast.LENGTH_SHORT).show();
        }else if(pokeWeightEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please, set a pokemon weight", Toast.LENGTH_SHORT).show();
        } else if(pokeHeightEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please, set a pokemon height", Toast.LENGTH_SHORT).show();
        } else {
            pokemon.setName(pokeNameEdit.getText().toString());
            pokemon.setSpecies(pokeSpeciesEdit.getText().toString());
            pokemon.setWeight(pokeWeightEdit.getText().toString());
            pokemon.setHeight(pokeHeightEdit.getText().toString());
            pokemon.setImage(pokeImagePath);
            pokemon.setFavorite(pokeIsFavorite.getNumStars());
            pokemon.setTrainerId(trainer.getId());

            if(pokeIsFavorite.getNumStars() == 1){
                trainer.setFavPokemon(pokeImagePath);
            }

            gson = new Gson();
            requestBody = gson.toJson(pokemon);
            StringRequest request = new StringRequest(Request.Method.POST, KEY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("true")){
                        Toast.makeText(getApplicationContext(),"Pokemon registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent successIntent = new Intent(PokeFormActivity.this, WelcomeTrainerActivity.class);
                        successIntent.putExtra("trainer", trainer);
                        startActivity(successIntent);
                        finish();
                    } else if(response.equals("false")){
                        Toast.makeText(getApplicationContext(),"Pokemon already exists!", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Register failed!", Toast.LENGTH_SHORT).show();
                    Log.e("PokemonAddActivity", error.getLocalizedMessage());
                }
            })  {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
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

        }
    }
}
