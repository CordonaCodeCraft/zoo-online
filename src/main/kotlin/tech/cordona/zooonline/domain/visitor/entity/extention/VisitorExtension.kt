package tech.cordona.zooonline.domain.visitor.entity.extention

import tech.cordona.zooonline.domain.visitor.entity.Visitor

object VisitorExtension {

	fun Visitor.updateFavorites(favorites: Set<String>) = this.copy(
		favorites = this.favorites.addAll(favorites).let { this.favorites }
	)

	fun Visitor.removeFavorites(toBeRemoved: Set<String>) = this.copy(
		favorites = this.favorites.removeAll(toBeRemoved).let { this.favorites }
	)
}