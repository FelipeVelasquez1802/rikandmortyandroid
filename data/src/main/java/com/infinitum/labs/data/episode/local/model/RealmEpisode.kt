package com.infinitum.labs.data.episode.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmEpisode : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var airDate: String = ""
    var episode: String = ""
    var charactersJson: String = ""
    var url: String = ""
    var created: String = ""
    var page: Int = 0
    var timestamp: Long = 0L
}