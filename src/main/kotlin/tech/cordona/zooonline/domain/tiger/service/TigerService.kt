package tech.cordona.zooonline.domain.tiger.service

import tech.cordona.zooonline.domain.tiger.entity.Tiger

interface TigerService {
	fun saveAll(entities: List<Tiger>): List<Tiger>
	fun getAll() : List<Tiger>
}