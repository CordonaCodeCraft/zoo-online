package tech.cordona.zooonline.validation.annotation

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_TRAINING_POINTS
import tech.cordona.zooonline.validation.validator.TrainingPointsValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [TrainingPointsValidator::class])
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidTrainingPoints(
	val message: String = "The training points must be an integer between $MIN_TRAINING_POINTS and $MAX_TRAINING_POINTS",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)
