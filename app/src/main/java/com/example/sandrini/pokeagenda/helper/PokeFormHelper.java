package com.example.sandrini.pokeagenda.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.sandrini.pokeagenda.PokeFormActivity;
import com.example.sandrini.pokeagenda.R;
import com.example.sandrini.pokeagenda.model.Pokemon;

public class PokeFormHelper {
    private final EditText nameField;
    private final EditText speciesField;
    private final EditText wightField;
    private final EditText heightField;
    private final RatingBar favField;
    private Pokemon pokemon;
    private final ImageView imageField;


    public PokeFormHelper(PokeFormActivity edit) {
        nameField = (EditText) edit.findViewById(R.id.pokemon_name_edit);
        speciesField = (EditText) edit.findViewById(R.id.pokemon_species_edit);
        wightField = (EditText) edit.findViewById(R.id.pokemon_weight_edit);
        heightField = (EditText) edit.findViewById(R.id.pokemon_height_edit);
        favField = (RatingBar) edit.findViewById(R.id.pokemon_favorite_edit);
        imageField = (ImageView) edit.findViewById(R.id.pokemon_image_edit);
        pokemon = new Pokemon();
    }

    public Pokemon getPokemon() {
        pokemon.setName(nameField.getText().toString());
        pokemon.setSpecies(speciesField.getText().toString());
        pokemon.setWeight(wightField.getText().toString());
        pokemon.setHeight(heightField.getText().toString());
        pokemon.setFavorite(favField.getProgress());
        pokemon.setImage((String) imageField.getTag());
        return pokemon;
    }

    public void fillForm(Pokemon pokemon) {
        nameField.setText(pokemon.getName());
        speciesField.setText(pokemon.getSpecies());
        wightField.setText(pokemon.getWeight());
        heightField.setText(pokemon.getHeight());
        favField.setProgress(pokemon.isFavorite());
        loadCameraImage(pokemon.getImage());
        this.pokemon = pokemon;
    }

    public void loadCameraImage(String imagePath) {
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap minimizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            imageField.setImageBitmap(minimizedBitmap);
            imageField.setScaleType(ImageView.ScaleType.FIT_XY);
            imageField.setTag(imagePath);
        }
    }

    public  void loadGalleryImage() {

    }
}
