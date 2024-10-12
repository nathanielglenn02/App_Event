package com.example.app_event.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("listEvents")
    val listEvents: List<EventItem> = listOf()
)

data class EventItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("ownerName")
    val ownerName: String = "",
    @SerializedName("beginTime")
    val beginTime: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("imageLogo")
    val imageLogo: String = "",
    @SerializedName("quota")
    val quota: Int = 0,
    @SerializedName("registrants")
    val registrants: Int = 0
)