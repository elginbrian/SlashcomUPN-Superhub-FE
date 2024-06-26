package com.vbounties.trufriend.features.presentation.screen.home_screen

import com.vbounties.trufriend.features.data.remote.response.GetAllJournalResponse
import com.vbounties.trufriend.features.data.remote.response.JournalData2

data class JournalState(
    val isLoading: Boolean = true,
    val message: String = "default",
    val data: GetAllJournalResponse = GetAllJournalResponse(
        status = 0,
        message = "default",
        data = listOf(JournalData2(
            id = "default",
            content = "default",
            "default",
            "default",
            "default",
            "default",
        ))
    )
)
