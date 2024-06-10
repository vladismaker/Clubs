package com.app.basket.team.api

import com.app.basket.team.dataclasses.DataPlaybook
import com.app.basket.team.dataclasses.EventsData
import com.app.basket.team.dataclasses.EventsRequest
import com.app.basket.team.dataclasses.TeamData
import com.app.basket.team.dataclasses.WorkplaceRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ClubsEndpoints {
    @POST("teams.php?action=get")
    suspend fun workplacePostData(@Body requestBody: WorkplaceRequest, @Header("Token") token: String): TeamData

    @POST("events.php?action=get")
    suspend fun eventsPostData(@Body requestBody: EventsRequest, @Header("Token") token: String): List<EventsData>

    @POST("playbook.php?action=get")
    suspend fun playbookPostData(@Body requestBody: WorkplaceRequest, @Header("Token") token: String): List<DataPlaybook>
}