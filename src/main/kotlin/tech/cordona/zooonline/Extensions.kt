package tech.cordona.zooonline

import tech.cordona.zooonline.domain.animal.entity.Animal
import tech.cordona.zooonline.domain.animal.model.AnimalToVisitor
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.area.model.AreaToVisitor
import tech.cordona.zooonline.domain.cell.entity.Cell
import tech.cordona.zooonline.domain.cell.model.CellToVisitor
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.security.dto.TokenWrapper
import tech.cordona.zooonline.security.user.entity.User
import tech.cordona.zooonline.security.user.model.AuthenticatedUserDetails
import tech.cordona.zooonline.security.user.model.UserModel

object Extensions {

	fun UserModel.asEntity() = User(
		firstName = this.firstName,
		lastName = this.lastName,
		email = this.email,
		password = this.password,
	)

	fun User.asModel() = UserModel(
		firstName = this.firstName,
		lastName = this.lastName,
		email = this.email,
		password = this.password
	)

	fun User.asVisitor() = Visitor(
		userId = this.id!!,
		firstName = this.firstName,
		lastName = this.lastName,
		favorites = mutableSetOf()
	)

	fun User.asAuthenticatedUser() = AuthenticatedUserDetails(
		id = this.id!!,
		email = this.email,
		userPassword = this.password,
		authority = this.authority
	)

	fun Cell.toVisitor(animals: List<AnimalToVisitor>) = CellToVisitor(
		animalGroup = this.animalGroup,
		animalType = this.animalType,
		specie = this.specie,
		animals = animals
	)

	fun Area.toVisitor(species: List<String>) = AreaToVisitor(
		areaName = this.animalType,
		species = species
	)

	fun Animal.toVisitor() = AnimalToVisitor(
		name = this.name,
		gender = this.gender,
		age = this.age,
		weight = this.weight
	)

	fun Visitor.updateFavorites(favorites: Set<String>) = this.copy(
		favorites = this.favorites.addAll(favorites).let { this.favorites }
	)

	fun Visitor.removeFavorites(toBeRemoved: Set<String>) = this.copy(
		favorites = this.favorites.removeAll(toBeRemoved).let { this.favorites }
	)

	fun String.withEmptySpace() = this.replace("-", " ")
	fun String.extractJwtToken() = this.substring("Bearer ".length)
	fun String?.isAuthorizationHeader() = this != null && this.startsWith("Bearer ")
	fun String.isGoodFor(tokenWrapper: TokenWrapper) = this == "" || this != tokenWrapper.token
}