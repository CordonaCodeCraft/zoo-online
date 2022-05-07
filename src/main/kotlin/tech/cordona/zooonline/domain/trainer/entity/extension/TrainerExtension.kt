package tech.cordona.zooonline.domain.trainer.entity.extension

import org.bson.types.ObjectId
import tech.cordona.zooonline.domain.area.entity.Area
import tech.cordona.zooonline.domain.trainer.entity.Trainer

fun Trainer.assignTo(area: Area, animals: MutableSet<ObjectId>) = this.copy(area = area.name, animals = animals)