package com.apptrick.app.data.api.helpers

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>
}
