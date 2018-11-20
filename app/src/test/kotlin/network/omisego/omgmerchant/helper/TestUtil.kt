package network.omisego.omgmerchant.helper

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.File

fun File.mockEnqueueWithHttpCode(mockWebServer: MockWebServer, responseCode: Int = 200) {
    mockWebServer.enqueue(MockResponse().apply {
        setBody(this@mockEnqueueWithHttpCode.readText())
        setResponseCode(responseCode)
    })
}
