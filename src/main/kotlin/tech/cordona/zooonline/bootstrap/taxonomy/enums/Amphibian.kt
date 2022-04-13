package tech.cordona.zooonline.bootstrap.taxonomy.enums

enum class Amphibian(val asString: String, val species: List<String>) {
	FROG(
		"Frog", listOf(
			"Golden Mantella",
			"Mountain Chicken",
			"Tomato Frog"
		)
	),
	SALAMANDER(
		"Salamander", listOf(
			"Hellbender",
			"Tiger Salamander",
			"Mudpuppy"
		)
	)
}