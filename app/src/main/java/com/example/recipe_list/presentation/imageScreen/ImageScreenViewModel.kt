package com.example.recipe_list.presentation.imageScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_list.R
import com.example.recipe_list.common.Constants
import com.example.recipe_list.common.Resource
import com.example.recipe_list.common.StringResourcesManager
import com.example.recipe_list.domain.useCase.DownloadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ImageScreenViewModel @Inject constructor(
    private val downloadImageUseCase: DownloadImageUseCase,
    private val stringResourcesManager: StringResourcesManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _url: MutableState<String> = mutableStateOf("")
    val url: State<String> = _url

    private val _toastMessages = MutableSharedFlow<String>()
    val toastMessages: SharedFlow<String> = _toastMessages

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        _url.value = checkNotNull(savedStateHandle[Constants.URL])
    }

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    fun downloadImage() {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val dateTime = dateFormat.format(Date())
        val fileName = "%s-%s.jpg".format(url.hashCode(), dateTime)
        viewModelScope.launch {
            downloadImageUseCase(_url.value, fileName).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _toastMessages.emit(
                        stringResourcesManager.getStringResourceById(
                            R.string.download_started
                        )
                    )
                    is Resource.Success -> {
                        _toastMessages.emit(
                            stringResourcesManager.getStringResourceById(
                                R.string.download_completed_successfully
                            )
                        )
                    }
                    is Resource.Error -> _toastMessages.emit(
                        stringResourcesManager.getStringResourceById(
                            R.string.load_error
                        )
                    )
                }
            }
        }
    }

}