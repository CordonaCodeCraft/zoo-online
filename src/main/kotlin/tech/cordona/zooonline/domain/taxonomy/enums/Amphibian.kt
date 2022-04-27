package tech.cordona.zooonline.domain.taxonomy.enums

enum class Amphibian(val species: List<String>) {
	FROG(
		listOf(
			"Golden Mantella",
			"Mountain Chicken",
			"Tomato Frog"
		)
	),
	SALAMANDER(
		listOf(
			"Hellbender",
			"Tiger Salamander",
			"Mudpuppy"
		)
	)
}