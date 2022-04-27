package tech.cordona.zooonline.domain.taxonomy.enums

enum class Bird(val species: List<String>) {
	CRANE(
		listOf(
			"Sarus Crane",
			"Stanley Crane",
			"White-naped Crane"
		)
	),
	GULL(
		listOf(
			"Cape Thick-knee",
			"Horned Puffin",
			"Tufted Puffin"
		)
	),
	OWL(
		listOf(
			"Eastern Screech Owl",
			"Great Horned Owl"
		)
	),
	PERCHING(
		listOf(
			"Bali Mynah",
			"Blue-faced Honeyeater",
			"Collie's Jay"
		)
	),
	BIRD_PREDATOR(
		listOf(
			"Bald Eagle",
			"Bateleur Eagle",
			"Cinereous Vulture"
		)
	)
}