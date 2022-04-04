package tech.cordona.zooonline.domain.animals.entity.structs

// Type is the class: mammal, reptile etc
data class TaxonomyDetails(
	val type: String,
	val subType: String,
	val specie: String,
	val domain: String = "Eukaryote",
	val kingdom: String = "Animalia",
	val phylum: String = "Animal",
)