package com.example.domains.auth.service

import com.example.common.exception.CustomException
import com.example.common.exception.ErrorCode
import com.example.common.jwt.JwtProvider
import com.example.common.logging.Logging
import com.example.common.transaction.Transactional
import com.example.interfaces.OAuthServiceInterface
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class AuthService (
    private val oAuthServices: Map<String, OAuthServiceInterface>,
    private val jwtProvider: JwtProvider,
    private val logger : Logger = Logging.getLogger(AuthService::class.java),
    private val transaction: Transactional
) {

    fun handleAuth(state: String, code: String): String = Logging.logFor(logger) { log ->
        val provider = state.lowercase()
        log["provider"] = provider

        val callService = oAuthServices[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

        val accessToken = callService.getToken(code)
        val userInfo = callService.getUserInfo(accessToken.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)

        transaction.run {

        }

        //userinfo
        return ""
    }
}