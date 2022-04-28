package exceptions

class InvalidEntityException(override val message: String) : RuntimeException(message)