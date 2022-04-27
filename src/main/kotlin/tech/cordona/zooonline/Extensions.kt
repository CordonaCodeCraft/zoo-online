package tech.cordona.zooonline

import tech.cordona.zooonline.security.dto.TokenWrapper
import java.util.*

object Extensions {

	fun Collection<Any>.stringify() = this.map { it.toString() }

	fun String.withEmptySpace() = this.replace("-", " ")

	fun String.getFirstName() = this.splitBySpace()[0]

	fun String.getLastName() = this.splitBySpace()[1]

	fun String.buildEmail() = this.splitBySpace()
		.let { list -> "${list[0]}.${list[1]}@zoo-online.com" }
		.lowercase(Locale.getDefault())

	fun String.buildPassword() = this.replace(" ", "")

	fun String.extractJwtToken() = this.substring("Bearer ".length)

	fun String?.isAuthorizationHeader() = this != null && this.startsWith("Bearer ")

	fun String.isGoodFor(tokenWrapper: TokenWrapper) = this == "" || this != tokenWrapper.token

	fun String.asTitlecase() = this.lowercase(Locale.getDefault())
		.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
		.replace("_", " ")

	private fun String.splitBySpace() = this.split(" ")
}