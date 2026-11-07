package com.example.stockmarket

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MyWebSocketListener(
    private val onMessage: (String) -> Unit,
    private val onOpen: () -> Unit = {},
    private val onClose: () -> Unit = {},
    private val onError: (String) -> Unit = {}
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        onOpen()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessage(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        onClose()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        onError(t.message ?: "Unknown error")
    }
}