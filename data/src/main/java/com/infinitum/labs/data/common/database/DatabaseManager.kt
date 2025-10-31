package com.infinitum.labs.data.common.database

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration

internal class DatabaseManager(private val context: Context) {

    companion object {
        private const val DATABASE_NAME = "rickandmorty"

        const val COLLECTION_CHARACTERS = "characters"
        const val COLLECTION_LOCATIONS = "locations"
        const val COLLECTION_EPISODES = "episodes"
    }

    private var database: Database? = null

    init {
        CouchbaseLite.init(context)
    }

    @Synchronized
    fun getDatabase(): Database {
        return database ?: run {
            val config = DatabaseConfiguration().apply {
                directory = context.filesDir.absolutePath
            }
            Database(DATABASE_NAME, config).also {
                database = it
                android.util.Log.d("DatabaseManager", "✅ Database path: ${it.path}")
                android.util.Log.d("DatabaseManager", "✅ Database directory: ${context.filesDir.absolutePath}")
            }
        }
    }

    @Synchronized
    fun close() {
        database?.close()
        database = null
    }

    @Synchronized
    fun deleteDatabase() {
        database?.delete()
        database = null
    }
}