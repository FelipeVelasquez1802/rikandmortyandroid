package com.infinitum.labs.domain.common.model

data class DataResult<out T>(
    val data: T,
    val source: DataSource
)