package com.infinitum.labs.domain.location.model

import com.infinitum.labs.domain.location.exception.LocationException

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
) {
    init {
        validate()
    }

    private fun validate() {
        validateId()
        validateName()
        validateType()
        validateDimension()
        validateUrl()
        validateCreated()
    }

    private fun validateId() {
        if (id < 1) {
            throw LocationException.InvalidLocationId(locationId = id)
        }
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw LocationException.InvalidLocationName(name = name)
        }
    }

    private fun validateType() {
        if (type.isBlank()) {
            throw LocationException.InvalidLocationType(type = type)
        }
    }

    private fun validateDimension() {
        if (dimension.isBlank()) {
            throw LocationException.InvalidLocationDimension(dimension = dimension)
        }
    }

    private fun validateUrl() {
        if (url.isBlank()) {
            throw LocationException.InvalidLocationUrl(url = url)
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw LocationException.InvalidLocationUrl(url = url)
        }
    }

    private fun validateCreated() {
        if (created.isBlank()) {
            throw LocationException.InvalidLocationCreatedDate(created = created)
        }
    }
}