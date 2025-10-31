package com.infinitum.labs.data.location.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmLocation : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var type: String = ""
    var dimension: String = ""
    var residentsJson: String = ""
    var url: String = ""
    var created: String = ""
    var page: Int = 0
    var timestamp: Long = 0L
}