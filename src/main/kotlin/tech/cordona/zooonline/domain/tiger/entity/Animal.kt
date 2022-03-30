package tech.cordona.zooonline.domain.tiger.entity

abstract class Animal : BaseEntity() {
	abstract val name: String
	abstract val type: String
}