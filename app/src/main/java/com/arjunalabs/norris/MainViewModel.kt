package com.arjunalabs.norris

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjunalabs.norris.model.Joke
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

data class MainScreenUIState(
    val randomJoke: Joke?
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUIState(randomJoke = null))
    val uiState: StateFlow<MainScreenUIState> = _uiState.asStateFlow()

    // Called from the UI
    @OptIn(ExperimentalStdlibApi::class)
    fun fetchRandomJoke() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("https://api.chucknorris.io/jokes/random")
            val request = Request.Builder().url(url).build()
            val response = OkHttpClient().newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string() ?: ""
                val moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<Joke> = moshi.adapter()
                runCatching {
                    val randomJoke = jsonAdapter.fromJson(json)
                    _uiState.update { currentState ->
                        currentState.copy(
                            randomJoke = randomJoke
                        )
                    }
                }

            }
        }
    }
}
