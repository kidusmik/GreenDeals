package com.kidusmichaelworku.discountdealslist.services

import retrofit2.http.GET

/**
 * Sends a GET request to LinkMyDeal API
 * Returns a [Response] object
 */
interface DealsService {
    @GET("/getOffers/?API_KEY=c174b7f5abadf7e1ab08123212fa26ad&format=json")
    suspend fun getOffers(): Response
}