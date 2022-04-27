package tech.cordona.zooonline.domain.taxonomy.enums

enum class Insect(val species: List<String>) {
	SPIDER(
		listOf(
			"Black widow",
			"Brown widow",
			"Missouri tarantula"
		)
	),
	SCORPION(
		listOf(
			"Bark scorpion",
			"Emperor scorpion",
			"Whip scorpion"
		)
	)
}