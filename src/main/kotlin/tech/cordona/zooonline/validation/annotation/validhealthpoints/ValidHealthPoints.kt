package tech.cordona.zooonline.validation.annotation.validhealthpoints

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_HEALTH_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_HEALTH_POINTS
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [HealthPointsValidator::class])
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidHealthPoints(
	val message: String = "The health points must be an integer between $MIN_HEALTH_POINTS and $MAX_HEALTH_POINTS",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)
