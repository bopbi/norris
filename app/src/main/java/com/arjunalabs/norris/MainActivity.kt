package com.arjunalabs.norris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.arjunalabs.norris.model.Joke
import com.arjunalabs.norris.ui.theme.NorrisTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NorrisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RandomJokeContent()
                }
            }
        }
    }
}

@Composable
fun JokeContent(joke: Joke) {
    Column() {
        SubcomposeAsyncImage(
            model = joke.iconUrl,
            contentDescription = "icon url"
        ) {
            when (painter.state) {
                AsyncImagePainter.State.Empty -> Text(text = "Image Empty")
                is AsyncImagePainter.State.Error -> Text(text = "Error")
                is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
                is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
            }
        }
        Text(text = joke.value)
    }
}

@Composable
fun RandomJokeContent(viewModel: MainViewModel = MainViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val randomJoke = uiState.randomJoke
    if (randomJoke == null) {
        viewModel.fetchRandomJoke()
        CircularProgressIndicator()
    } else {
        JokeContent(joke = randomJoke)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NorrisTheme {
        JokeContent(
            Joke(
                id = "GIRljX-vRlu3t9aAUuO3rw",
                iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                url = "",
                value = "Stanford university recently received a scholarship from Chuck Norris"
            )
        )
    }
}
