package com.infinitum.labs.data.character.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmCharacter : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var status: String = ""
    var species: String = ""
    var type: String = ""
    var gender: String = ""
    var originName: String = ""
    var originUrl: String = ""
    var locationName: String = ""
    var locationUrl: String = ""
    var image: String = ""
    var episodesJson: String = ""
    var url: String = ""
    var created: String = ""
    var page: Int = 0
    var timestamp: Long = 0L
}