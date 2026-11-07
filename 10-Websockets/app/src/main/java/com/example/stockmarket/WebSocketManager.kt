package com.example.stockmarket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class WebSocketManager(
    private val url: String,
    private val listener: MyWebSocketListener
) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closed")
        webSocket = null
    }

    fun isConnected(): Boolean = webSocket != null
}