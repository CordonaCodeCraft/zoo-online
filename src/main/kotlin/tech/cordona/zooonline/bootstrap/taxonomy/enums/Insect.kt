package tech.cordona.zooonline.bootstrap.taxonomy.enums

enum class Insect(val asString: String, val species: List<String>) {
	SPIDER(
		"Spider", listOf(
			"Black widow",
			"Brown widow",
			"Missouri tarantula"
		)
	),
	SCORPION(
		"Scorpion", listOf(
			"Bark scorpion",
			"Emperor scorpion",
			"Whip scorpion"
		)
	)
}