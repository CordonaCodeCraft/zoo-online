package tech.cordona.zooonline.validation.annotation

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_AGE
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_AGE
import tech.cordona.zooonline.validation.validator.AgeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [AgeValidator::class])
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidAge(
	val message: String = "The animal's age must be an integer between $MIN_AGE and $MAX_AGE",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)