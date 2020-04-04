package com.example.memesocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.client.features.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

	val client = HttpClient {
		install(WebSockets)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		launch {
			client.wss(
				method = HttpMethod.Get,
				host = "memebattle-socket.herokuapp.com",
				path = "/api/v1"
			) { // this: DefaultClientWebSocketSession

				// Send text frame.
				send(Frame.Text("Hello World"))

				// Receive frame.
				val frame = incoming.receive()
				when (frame) {
					is Frame.Text   -> println(frame.readText())
					is Frame.Binary -> println(frame.readBytes())
				}
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		cancel()
	}
}
