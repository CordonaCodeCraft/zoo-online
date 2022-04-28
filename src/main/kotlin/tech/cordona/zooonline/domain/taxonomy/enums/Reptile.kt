package tech.cordona.zooonline.domain.taxonomy.enums

enum class Reptile(val species: List<String>) {
	ALLIGATOR(
		listOf(
			"American Alligator",
			"Chinese Alligator"
		)
	),
	CROCODILE(
		listOf(
			"Dwarf Caiman",
			"Malayan Gharial"
		)
	),
	LIZARD(
		listOf(
			"Banded Gila Monster",
			"Chinese Crocodile Lizard",
			"Scheltopusik"
		)
	),
	SNAKE(
		listOf(
			"Armenian Viper",
			"Ball Python",
			"King Cobra"
		)
	),
	TURTLE(
		listOf(
			"Aldabra Tortoise",
			"Box Turtle",
			"Asian Giant Pond Turtle"
		)
	)
}