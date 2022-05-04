package tech.cordona.zooonline.domain.user.entity.extension

import org.bson.types.ObjectId
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import tech.cordona.zooonline.domain.doctor.entity.Doctor
import tech.cordona.zooonline.domain.guard.entity.Guard
import tech.cordona.zooonline.domain.trainer.entity.Trainer
import tech.cordona.zooonline.domain.visitor.entity.Visitor
import tech.cordona.zooonline.domain.user.entity.User
import tech.cordona.zooonline.domain.user.model.AuthenticatedUserDetails
import tech.cordona.zooonline.domain.user.model.UserModel

fun User.asModel() = UserModel(
	firstName = this.firstName,
	middleName = this.middleName,
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

fun User.asTrainer(area: String, animals: List<ObjectId>) = Trainer(
	userId = this.id!!,
	firstName = this.firstName,
	middleName = this.middleName,
	lastName = this.lastName,
	area = area,
	animals = animals.toMutableSet()
)

fun User.asDoctor(area: String, animals: List<ObjectId>) = Doctor(
	userId = this.id!!,
	firstName = this.firstName,
	middleName = this.middleName,
	lastName = this.lastName,
	area = area,
	animals = animals.toMutableSet()
)

fun User.asGuard(area: String, cells: Set<ObjectId>) = Guard(
	userId = this.id!!,
	firstName = this.firstName,
	middleName = this.middleName,
	lastName = this.lastName,
	area = area,
	cells = cells
)

fun User.withEncodedPassword(encoder: BCryptPasswordEncoder) = this.copy(password = encoder.encode(this.password))
