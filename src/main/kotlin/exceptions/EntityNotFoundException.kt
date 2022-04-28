package exceptions

class EntityNotFoundException(override val message: String) : RuntimeException(message)