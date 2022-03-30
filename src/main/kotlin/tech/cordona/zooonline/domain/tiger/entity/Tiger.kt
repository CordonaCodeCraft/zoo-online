package tech.cordona.zooonline.domain.tiger.entity

import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Tigers")
@TypeAlias("tiger")
data class Tiger(override val name: String, override val type: String) : Animal()
