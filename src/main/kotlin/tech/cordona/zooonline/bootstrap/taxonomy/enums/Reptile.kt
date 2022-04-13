package tech.cordona.zooonline.bootstrap.taxonomy.enums

enum class Reptile(val asString: String, val species: List<String>) {
	ALLIGATOR(
		"Alligator", listOf(
			"American Alligator",
			"Chinese Alligator"
		)
	),
	CROCODILE(
		"Crocodile", listOf(
			"Dwarf Caiman",
			"Malayan Gharial"
		)
	),
	LIZARD(
		"Lizard", listOf(
			"Banded Gila Monster",
			"Chinese Crocodile Lizard",
			"Scheltopusik"
		)
	),
	SNAKE(
		"Snake", listOf(
			"Armenian Viper",
			"Ball Python",
			"King Cobra"
		)
	),
	TURTLE(
		"Turtle", listOf(
			"Aldabra Tortoise",
			"Box Turtle",
			"Asian Giant Pond Turtle"
		)
	)
}