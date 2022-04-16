package tech.cordona.zooonline.domain.taxonomy.enums

enum class Bird(val asString: String, val species: List<String>) {
	CRANE(
		"Crane", listOf(
			"Sarus Crane",
			"Stanley Crane",
			"White-naped Crane"
		)
	),
	GULL(
		"Gull", listOf(
			"Cape Thick-knee",
			"Horned Puffin",
			"Tufted Puffin"
		)
	),
	OWL(
		"Owl", listOf(
			"Eastern Screech Owl",
			"Great Horned Owl"
		)
	),
	PERCHING(
		"Perching", listOf(
			"Bali Mynah",
			"Blue-faced Honeyeater",
			"Collie's Jay"
		)
	),
	PREDATOR(
		"Predator", listOf(
			"Bald Eagle",
			"Bateleur Eagle",
			"Cinereous Vulture"
		)
	)
}