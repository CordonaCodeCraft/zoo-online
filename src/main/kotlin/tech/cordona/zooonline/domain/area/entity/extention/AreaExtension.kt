package tech.cordona.zooonline.domain.area.entity.extention

import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.model.AreaToVisitor

object AreaExtension {

	fun Area.toVisitor(species: List<String>) = AreaToVisitor(
		areaName = this.name,
		species = species
	)
}