package tech.cordona.zooonline.validation.annotation.validtrainingpoints

import tech.cordona.zooonline.validation.ValidationConstraints.MAX_TRAINING_POINTS
import tech.cordona.zooonline.validation.ValidationConstraints.MIN_TRAINING_POINTS
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TrainingPointsValidator : ConstraintValidator<ValidTrainingPoints, Int> {
	override fun isValid(value: Int, context: ConstraintValidatorContext?) =
		value in MIN_TRAINING_POINTS..MAX_TRAINING_POINTS
}