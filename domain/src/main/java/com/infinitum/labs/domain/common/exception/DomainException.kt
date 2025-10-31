package com.infinitum.labs.domain.common.exception

abstract class DomainException(
    message: String,
    cause: Throwable? = null,
    val errorCode: String? = null
) : Exception(message, cause) {

    override fun toString(): String {
        val codePrefix = errorCode?.let { "[$it] " } ?: ""
        return "${this::class.simpleName}: $codePrefix$message"
    }
}

abstract class ValidationException(
    val fieldName: String,
    val invalidValue: Any?,
    message: String,
    errorCode: String? = null
) : DomainException(
    message = "$fieldName validation failed: $message",
    errorCode = errorCode
) {
    override fun toString(): String {
        val codePrefix = errorCode?.let { "[$it] " } ?: ""
        return "${this::class.simpleName}: $codePrefix$fieldName='$invalidValue' - $message"
    }
}

abstract class NotFoundException(
    val resourceType: String,
    val identifier: Any,
    message: String,
    errorCode: String? = null
) : DomainException(
    message = "$resourceType not found: $message",
    errorCode = errorCode
) {
    override fun toString(): String {
        val codePrefix = errorCode?.let { "[$it] " } ?: ""
        return "${this::class.simpleName}: $codePrefix$resourceType with identifier '$identifier' not found"
    }
}

abstract class RepositoryException(
    val service: String,
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(
    message = "$service: $message",
    cause = cause,
    errorCode = errorCode
) {
    override fun toString(): String {
        val codePrefix = errorCode?.let { "[$it] " } ?: ""
        val causeMsg = cause?.let { " (caused by: ${it.message})" } ?: ""
        return "${this::class.simpleName}: $codePrefix$service - $message$causeMsg"
    }
}