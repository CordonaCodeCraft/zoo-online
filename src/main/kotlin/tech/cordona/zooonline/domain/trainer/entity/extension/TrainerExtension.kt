package tech.cordona.zooonline.domain.trainer.entity.extension

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.trainer.entity.Trainer

object TrainerExtension {
	fun Trainer.reassigned(area: Area, animals: MutableSet<ObjectId>) = this.copy(area = area.name, animals = animals)
}