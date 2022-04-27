package tech.cordona.zooonline.domain.visitor.entity.extension

import tech.cordona.zooonline.domain.visitor.entity.Visitor

fun Visitor.updateFavorites(favorites: Set<String>) = this.copy(
	favorites = this.favorites.addAll(favorites).let { this.favorites }
)

fun Visitor.removeFavorites(toBeRemoved: Set<String>) = this.copy(
	favorites = this.favorites.removeAll(toBeRemoved).let { this.favorites }
)