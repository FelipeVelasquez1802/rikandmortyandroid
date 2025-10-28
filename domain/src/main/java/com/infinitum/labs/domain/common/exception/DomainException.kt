package com.infinitum.labs.domain.common.exception

/**
 * Base exception for all domain exceptions in the application.
 *
 * This is the root of the domain exception hierarchy, providing a common
 * contract for all business logic exceptions. All domain exceptions should
 * inherit from this class to maintain consistency and enable unified error handling.
 *
 * Domain exceptions represent violations of business rules and domain invariants.
 * They speak the ubiquitous language of the domain and should provide clear,
 * business-oriented error messages.
 *
 * @property message Human-readable description of what went wrong
 * @property cause The underlying cause of this exception (if any)
 * @property errorCode Optional error code for categorizing exceptions
 */
abstract class DomainException(
    message: String,
    cause: Throwable? = null,
    val errorCode: String? = null
) : Exception(message, cause) {

    /**
     * Returns a detailed description of the exception including error code if present.
     */
    override fun toString(): String {
        val codePrefix = errorCode?.let { "[$it] " } ?: ""
        return "${this::class.simpleName}: $codePrefix$message"
    }
}

/**
 * Base exception for validation failures in domain entities.
 *
 * Thrown when a domain entity or value object fails to meet its invariants
 * during construction or modification. These exceptions indicate data quality
 * issues that violate business rules.
 *
 * @property fieldName The name of the field that failed validation
 * @property invalidValue The value that failed validation (optional)
 * @property message Detailed explanation of why the validation failed
 */
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

/**
 * Base exception for resource not found errors.
 *
 * Thrown when a requested domain entity or aggregate cannot be found
 * by its identifier or search criteria. These exceptions typically
 * result in 404 responses at the API level.
 *
 * @property resourceType The type of resource that was not found (e.g., "Character", "Episode")
 * @property identifier The identifier used to search for the resource
 */
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

/**
 * Base exception for infrastructure/repository failures.
 *
 * Thrown when there are problems accessing external resources like
 * databases, APIs, or file systems. These exceptions typically indicate
 * temporary failures that might be resolved by retrying.
 *
 * @property service The name of the service/repository that is unavailable
 */
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