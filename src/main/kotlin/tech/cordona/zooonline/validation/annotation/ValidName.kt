package tech.cordona.zooonline.validation.annotation

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_NAME_LENGTH
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_NAME_LENGTH
import tech.cordona.zooonline.validation.validator.NameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [NameValidator::class])
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidName(
	val message: String = "The name must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH characters long",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)
