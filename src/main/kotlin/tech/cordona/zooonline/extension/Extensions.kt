package tech.cordona.zooonline.extension

object Extensions {

	fun Collection<Any>.stringify() = this.map { it.toString() }
}