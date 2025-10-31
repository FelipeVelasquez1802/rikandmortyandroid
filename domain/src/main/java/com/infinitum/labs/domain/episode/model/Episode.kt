package com.infinitum.labs.domain.episode.model

import com.infinitum.labs.domain.episode.exception.EpisodeException

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
) {
    init {
        validate()
    }

    private fun validate() {
        validateId()
        validateName()
        validateAirDate()
        validateEpisodeCode()
        validateUrl()
        validateCreated()
    }

    private fun validateId() {
        if (id < 1) {
            throw EpisodeException.InvalidEpisodeId(episodeId = id)
        }
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw EpisodeException.InvalidEpisodeName(name = name)
        }
    }

    private fun validateAirDate() {
        if (airDate.isBlank()) {
            throw EpisodeException.InvalidEpisodeAirDate(airDate = airDate)
        }
    }

    private fun validateEpisodeCode() {
        if (episode.isBlank()) {
            throw EpisodeException.InvalidEpisodeCode(episodeCode = episode)
        }
    }

    private fun validateUrl() {
        if (url.isBlank()) {
            throw EpisodeException.InvalidEpisodeUrl(url = url)
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw EpisodeException.InvalidEpisodeUrl(url = url)
        }
    }

    private fun validateCreated() {
        if (created.isBlank()) {
            throw EpisodeException.InvalidEpisodeCreatedDate(created = created)
        }
    }
}
