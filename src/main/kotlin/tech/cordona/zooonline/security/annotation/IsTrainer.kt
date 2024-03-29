package tech.cordona.zooonline.security.annotation

import org.springframework.security.access.prepost.PreAuthorize

@Target(
	AnnotationTarget.FUNCTION,
	AnnotationTarget.PROPERTY_GETTER,
	AnnotationTarget.PROPERTY_SETTER,
	AnnotationTarget.ANNOTATION_CLASS,
	AnnotationTarget.CLASS,
	AnnotationTarget.ANNOTATION_CLASS
)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasAuthority('TRAINER')")
annotation class IsTrainer()