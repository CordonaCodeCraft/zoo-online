package tech.cordona.zooonline.domain.taxonomy.enums

enum class Mammal(val species: List<String>) {
	CARNIVORE(
		listOf(
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
		listOf(
			"African elephant",
			"Indian elephant"
		)
	),
	MONKEY(
		listOf(
			"Gorilla",
			"Proboscis",
			"Emperor tamarin",
			"Cotton top tamarin"
		)
	),
	HOOFED(
		listOf(
			"Addax",
			"Gerenuk",
			"Somali wild ass",
			"Gazelle"
		)
	),
	POUCHED(
		listOf(
			"Red kangaroo",
			"Tammar wallaby"
		)
	)
}