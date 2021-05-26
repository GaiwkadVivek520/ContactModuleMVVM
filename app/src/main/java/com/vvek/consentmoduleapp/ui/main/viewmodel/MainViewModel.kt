package com.vvek.consentmoduleapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vvek.consentmoduleapp.data.model.Contact
import com.vvek.consentmoduleapp.data.repository.MainRepository
import com.vvek.consentmoduleapp.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val users = MutableLiveData<Resource<List<Contact>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchContacts()
    }

    private fun fetchContacts() {
        if(mainRepository.getConsentFlag()) {      // Check for consent flag. get real contact list if its true
            compositeDisposable.add(
                mainRepository.getContacts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ userList ->
                        users.postValue(Resource.success(userList))
                    }, { throwable ->
                        users.postValue(Resource.error("Something Went Wrong", null))
                    })


            )
        }else{
            // Method to Get data from Local DB/Cache if flag is false
            // So for reference here we are calling same get Contacts method
            compositeDisposable.add(
                mainRepository.getContacts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ userList ->
                        users.postValue(Resource.success(userList))
                    }, { throwable ->
                        users.postValue(Resource.error("Something Went Wrong", null))
                    })
            )
        }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getContacts(): LiveData<Resource<List<Contact>>> {
        return users;
    }

    fun setConsentFlag(flag: Boolean) {
        mainRepository.setConsentFlag(flag)
    }

}