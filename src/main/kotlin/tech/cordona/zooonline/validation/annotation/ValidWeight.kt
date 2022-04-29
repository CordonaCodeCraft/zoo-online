package tech.cordona.zooonline.validation.annotation

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_WEIGHT
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_WEIGHT
import tech.cordona.zooonline.validation.validator.WeightValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [WeightValidator::class])
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidWeight(
	val message: String = "The animal's weight must be a double between $MIN_WEIGHT and $MAX_WEIGHT",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)