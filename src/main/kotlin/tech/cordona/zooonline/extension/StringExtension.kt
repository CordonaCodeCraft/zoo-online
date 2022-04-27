package tech.cordona.zooonline.extension

import tech.cordona.zooonline.security.dto.TokenWrapper
import java.util.Locale

object StringExtension {

	fun String.withEmptySpace() = this.replace("-", " ")

	fun String.getFirstName() = this.splitBySpace()[0]

	fun String.getMiddleName() = this.splitBySpace()[1]

	fun String.getLastName() = this.splitBySpace()[2]

	fun String.buildEmail() =
		"${this.getFirstName()}.${this.getLastName()}@zoo-online.com".lowercase(Locale.getDefault())

	fun String.buildPassword() = "${this.getFirstName()}${this.getLastName()}"

	fun String.extractJwtToken() = this.substring("Bearer ".length)

	fun String?.isAuthorizationHeader() = this != null && this.startsWith("Bearer ")

	fun String.isGoodFor(tokenWrapper: TokenWrapper) = this == "" || this != tokenWrapper.token

	fun String.asTitlecase() = this.lowercase(Locale.getDefault())
		.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
		.replace("_", " ")

	private fun String.splitBySpace() = this.split(" ")
}