import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

suspend fun checkWebsite(url: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            false
        }
    }
}

fun main() = runBlocking {
    val websites = listOf(
        "https://www.google.com",
        "https://www.facebook.com",
        "https://www.github.com",
        "https://www.twitter.com",
        "https://www.instagram.com",
        "https://www.microsoft.com",
        "https://www.apple.com",
        "https://www.wikipedia.org",
        "https://www.reddit.com",
        "https://www.stackoverflow.com"
    )

    val results = websites.map { url ->
        async { url to checkWebsite(url) }
    }.awaitAll()

    results.forEach { (url, isAvailable) ->
        println("Сайт $url ${if (isAvailable) "доступен" else "недоступен"}")
    }
}