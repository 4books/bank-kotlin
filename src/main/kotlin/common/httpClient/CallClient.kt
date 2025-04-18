package com.example.common.httpClient

import com.example.common.exception.CustomException
import com.example.common.exception.ErrorCode
import okhttp3.*
import org.springframework.stereotype.Component

@Component
class CallClient (
    private val httpClient: OkHttpClient
) {
    fun GET(uri: String, headers: Map<String, String> = emptyMap()): String {
        val requestBuilder = Request.Builder().url(uri)
        headers.forEach {(key, value) -> requestBuilder.addHeader(key, value)}
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    fun POST(uri: String, headers: Map<String, String> = emptyMap(), body: RequestBody): String {
        val requestBuilder = Request.Builder().url(uri).post(body)
        headers.forEach {(key, value) -> requestBuilder.addHeader(key, value)}
        val request = requestBuilder.build()

        return resultHandler(httpClient.newCall(request).execute())
    }

    private fun resultHandler(response: Response): String {
        response.use{
            if(!it.isSuccessful) {
                val msg = "Http ${it.code}: ${it.body?.string() ?: "Unknown error"}"
                throw CustomException(ErrorCode.FAILED_TO_CALL_CLIENT, msg)
            }

            return it.body?.string() ?: throw CustomException(ErrorCode.CALL_RESULT_BODY_NULL)
        }
    }
}
