package com.application.clubs.dataclasses

data class DataAction(
    val name:String = "",
    val description:String = "",
    val images:List<DataGameDescription> = listOf(),
    val videos:List<DataGameDescription> = listOf()
)
