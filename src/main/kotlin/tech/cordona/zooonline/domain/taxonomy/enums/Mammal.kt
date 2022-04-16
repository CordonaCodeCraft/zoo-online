package tech.cordona.zooonline.domain.taxonomy.enums

enum class Mammal(val asString: String, val species: List<String>) {
	CARNIVORE(
		"Carnivore", listOf(
			"Andean bear",
			"Grizzly bear",
			"Amur tiger",
			"Amur leopard",
			"Snow leopard",
			"Cheetah",
			"Jaguar",
			"Lion",
			"Puma",
			"Spotted hyena"
		)
	),
	ELEPHANT(
		"Elephant", listOf(
			"African elephant",
			"Indian elephant"
		)
	),
	MONKEY(
		"Monkey", listOf(
			"Gorilla",
			"Proboscis",
			"Emperor tamarin",
			"Cotton top tamarin"
		)
	),
	HOOFED(
		"Hoofed", listOf(
			"Addax",
			"Gerenuk",
			"Somali wild ass",
			"Gazelle"
		)
	),
	POUCHED(
		"Pouched", listOf(
			"Red kangaroo",
			"Tammar wallaby"
		)
	)
}