package com.application.clubs.api

import com.application.clubs.dataclasses.EventsData
import com.application.clubs.dataclasses.EventsRequest
import com.application.clubs.dataclasses.TeamData
import com.application.clubs.dataclasses.WorkplaceRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ClubsEndpoints {
    @POST("teams.php?action=get")
    suspend fun workplacePostData(@Body requestBody: WorkplaceRequest, @Header("Token") token: String): TeamData

    @POST("events.php?action=get")
    suspend fun eventsPostData(@Body requestBody: EventsRequest, @Header("Token") token: String): List<EventsData>
}