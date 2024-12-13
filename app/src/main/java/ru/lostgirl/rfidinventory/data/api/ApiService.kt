package ru.lostgirl.rfidinventory.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import ru.lostgirl.rfidinventory.data.api.models.Data
import ru.lostgirl.rfidinventory.data.api.models.Item

interface ApiService {
    @GET("full/")
    suspend fun getAll(): Response<Data>

    @PUT("items/")
    suspend fun saveItems(@Body items: List<Item>): Response<Unit>
}