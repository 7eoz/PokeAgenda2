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
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PokeFormActivity extends AppCompatActivity {

    // Definições de variáveis utilizadas nesta activity.

    public static final String PREFERENCES = "preferences";
    private static final String KEY_URL = "http://192.168.25.34:8081/PokedexWS/ws/pokews/poke/insert";
    private RequestQueue requestQueue;
    private String requestBody;
    private Gson gson;
    private int GALLERY_REQUEST = 123;
    private int CAMERA_REQUEST = 234;

    private ImageView pokeImage;
    private Button pokeImageButton;
    private String pokeImagePath;

    private EditText pokeNameEdit;
    private EditText pokeSpeciesEdit;
    private EditText pokeWeightEdit;
    private EditText pokeHeightEdit;
    private Pokemon pokemon;

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
               /* if (ActivityCompat.checkSelfPermission(PokeFormActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PokeFormActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_PHOTO_REQUEST);
                }
                //dispatchTakePictureIntent();*/
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
                    Toast.makeText(getApplicationContext(), "Imagem salva!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("PokemonAddActivity",e.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), "Ops! Ocorreu um erro!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if(pokeImagePath != null){
                Bitmap bitmap = BitmapFactory.decodeFile(pokeImagePath);
                Bitmap minimizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                pokeImage.setImageBitmap(minimizedBitmap);
                pokeImage.setScaleType(ImageView.ScaleType.FIT_XY);
                pokeImage.setTag(pokeImagePath);
                Toast.makeText(getApplicationContext(), "Imagem salva!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void addPokemon(){

        // Aqui passo o login (APENAS) para o cadastro do pokemon.
        //this.pokemon.setLoginTreinador(this.sharedPreferences.getString("treinador_login",null));

        if(pokeImage == null){
            Toast.makeText(getApplicationContext(),"Please, set a pokemon picture", Toast.LENGTH_SHORT).show();
        }else if(pokeNameEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Digite um nome do pokemon!", Toast.LENGTH_SHORT).show();
        } else if(pokeSpeciesEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Digite a espécie do pokemon!", Toast.LENGTH_SHORT).show();
        }else if(pokeWeightEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Digite o peso do pokemon!", Toast.LENGTH_SHORT).show();
        } else if(pokeHeightEdit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Digite a altura do pokemon!", Toast.LENGTH_SHORT).show();
        } else {
            pokemon.setName(pokeNameEdit.getText().toString());
            pokemon.setSpecies(pokeSpeciesEdit.getText().toString());
            pokemon.setWeight(pokeWeightEdit.getText().toString());
            pokemon.setHeight(pokeHeightEdit.getText().toString());
            pokemon.setImage(pokeImagePath);
            pokemon.setTrainerId(1);

            gson = new Gson();
            requestBody = gson.toJson(pokemon);
            StringRequest request = new StringRequest(Request.Method.POST, KEY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Pokemon pokemon = gson.fromJson(response,Pokemon.class);
                    //Intent successIntent = new Intent(PokeFormActivity.this, WelcomeTrainerActivity.class);
                    Toast.makeText(getApplicationContext(),"result was: " + response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Pokemon registered successfully!", Toast.LENGTH_SHORT).show();
                    //startActivity(successIntent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        if(error.networkResponse.statusCode==409){
                            Toast.makeText(getApplicationContext(),new String(error.networkResponse.data,"UTF-8"), Toast.LENGTH_LONG).show();
                            Log.e("PokemonAddActivity", error.getLocalizedMessage());
                        } else {
                            Toast.makeText(getApplicationContext(),"Ops! Ocorreu um erro inesperado!", Toast.LENGTH_LONG).show();
                            Log.e("PokemonAddActivity", error.getLocalizedMessage());
                        }
                    } catch (UnsupportedEncodingException e){
                        Log.e("PokemonAddActivity", e.getLocalizedMessage());
                    }
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
