package com.erayterin.cryptocurrencytracker.service

import com.erayterin.cryptocurrencytracker.model.CryptoModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {



    //https://api.nomics.com/v1/
    @GET("prices?key=fa2e8bba9d5ace3ad04efd7dba499d0973c714af")

    fun getData(): Observable<List<CryptoModel>>

    //fun getData(): Call<List<CryptoModel>>
}