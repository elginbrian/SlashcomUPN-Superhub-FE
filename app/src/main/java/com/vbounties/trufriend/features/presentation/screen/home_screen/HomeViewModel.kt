package com.vbounties.trufriend.features.presentation.screen.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbounties.trufriend.features.data.local.UserEntity
import com.vbounties.trufriend.features.data.remote.response.GetAllJournalResponse
import com.vbounties.trufriend.features.data.remote.response.JournalData2
import com.vbounties.trufriend.features.data.remote.response.UserEmotionResponse
import com.vbounties.trufriend.features.domain.repository.AuthRepository
import com.vbounties.trufriend.features.domain.repository.EmotionRepository
import com.vbounties.trufriend.features.domain.repository.JournalRepository
import com.vbounties.trufriend.features.domain.repository.UserRepository
import com.vbounties.trufriend.features.presentation.screen.profile_screen.UserState
import com.vbounties.trufriend.features.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val authRepository: AuthRepository
): ViewModel(){
    private val _userstate = MutableStateFlow(UserState())
    val userstate = _userstate.asStateFlow()

    private val _journalstate = MutableStateFlow(JournalState())
    val journalstate = _journalstate.asStateFlow()

    fun getUser(onFinished: (UserState) -> Unit) {
        viewModelScope.launch {
            authRepository.GetUserEntity().collect { result ->
                when(result) {
                    is Result.Success -> {
                        Log.d("ProfileViewModel", "getUser: ${result.data}")
                        _userstate.update { currentState ->
                            currentState.copy(
                                message = "Fetch Berhasil",
                                isLoading = false,
                                data = result.data ?: UserEntity("", "", "", "", "", "")
                            )
                        }
                        Log.d("ProfileViewModel", "userState: ${userstate.value}")
                        onFinished(userstate.value)
                    }
                    is Result.Error -> {
                        Log.d("ProfileViewModel", "getUser: ${result.data}")
                        _userstate.update { currentState ->
                            currentState.copy(
                                message = result.message ?: "Fetch Gagal",
                                isLoading = false
                            )
                        }
                    }
                    is Result.Loading -> {
                        Log.d("ProfileViewModel", "getUser: ${result.data}")
                        _userstate.update { currentState ->
                            currentState.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
            }
        }
    }

    fun getJournal(
        onFinished: (JournalState) -> Unit
    ){
        viewModelScope.launch {
            journalRepository.getJournals().collect{result ->
                when(result){
                    is Result.Success -> {
                        _journalstate.update {
                            it.copy(
                                isLoading = false,
                                message = "Fetch Berhasil",
                                data = result.data ?: GetAllJournalResponse(0, "default", listOf(
                                    JournalData2(
                                    id = "default",
                                    content = "default",
                                    "default",
                                    "default",
                                    "default",
                                    "default",
                                )
                                )
                                )
                            )
                        }
                        onFinished(journalstate.value)
                    }

                    is Result.Error -> {
                        _journalstate.update {
                            it.copy(
                                isLoading = false,
                                message = result.message ?: "Fetch Gagal"
                            )
                        }
                    }

                    is Result.Loading -> {
                        _journalstate.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
            }
        }
    }
}