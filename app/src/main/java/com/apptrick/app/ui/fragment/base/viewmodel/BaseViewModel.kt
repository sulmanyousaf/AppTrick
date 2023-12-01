package com.apptrick.app.ui.fragment.base.viewmodel

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apptrick.app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

fun Fragment.progressDialog(): AlertDialog {
    return MaterialAlertDialogBuilder(requireContext()).apply {
        setView(R.layout.loading_layout)
    }.create().apply {
        setCancelable(false)
        window?.let {
//            it.setDimAmount(0F)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}

fun AlertDialog.visibility(visible: Boolean) {
    if (visible) show() else dismiss()
}

fun <T> Fragment.observeLiveData(data: LiveData<T>, observer: (T) -> Unit) {
    data.observe(viewLifecycleOwner, observer)
}

open class BaseViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val baseEventsChannel = Channel<BaseEvent>()
    val baseEvents = baseEventsChannel.receiveAsFlow()

    fun showLoader() {
        _progress.postValue(true)
    }

    fun hideLoader() {
        _progress.postValue(false)
    }

    suspend fun sendError(error: String?) {
        hideLoader()
        baseEventsChannel.send(BaseEvent.ShowErrorMessage(error.orEmpty()))
    }

    sealed class BaseEvent {
        data class ShowErrorMessage(val msg: String) : BaseEvent()
    }
}