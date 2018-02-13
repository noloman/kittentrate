package kittentrate.data.viewmodel

/**
 * Created by Manuel Lorenzo on 11/02/2018.
 */
sealed class ResponseViewState {
    class None : ResponseViewState()
    data class Success<out T>(val item: T) : ResponseViewState()
    data class Error(val errorMessage: String?) : ResponseViewState()
}