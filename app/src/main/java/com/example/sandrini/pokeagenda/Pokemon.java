package com.example.sandrini.pokeagenda;

public class Pokemon {
	
	private int id;
	private String name;
	private String species;
	private String weight;
	private String height;
	private String image;
	private int isFavorite;
	private int trainerId;
	
	public Pokemon() {
		
	}
	
	public Pokemon(int id, String name, String species, String weight, String height, String image,
			int isFavorite, int trainerId) {
		super();
		this.id = id;
		this.name = name;
		this.species = species;
		this.weight = weight;
		this.height = height;
		this.image = image;
		this.trainerId = trainerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {	return height; }

	public void setHeight(String height) {
		this.height = height;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int isFavorite() {
		return isFavorite;
	}

	public void setFavorite(int isFavorite) {
		this.isFavorite = isFavorite;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}
	
	

}
