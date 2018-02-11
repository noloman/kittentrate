package kittentrate.data.viewmodel

/**
 * Created by Manuel Lorenzo on 23/11/2017.
 */
sealed class NetworkViewState {
    class None : NetworkViewState()
    class Loading : NetworkViewState()
    class Success<out T>(val item: T) : NetworkViewState()
    class Error(val errorMessage: String?) : NetworkViewState()
}