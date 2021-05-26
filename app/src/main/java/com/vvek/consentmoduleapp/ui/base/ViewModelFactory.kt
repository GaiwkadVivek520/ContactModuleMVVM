package com.vvek.consentmoduleapp.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vvek.consentmoduleapp.data.api.ApiHelper
import com.vvek.consentmoduleapp.data.repository.MainRepository
import com.vvek.consentmoduleapp.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper, private val applicationContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper,applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}