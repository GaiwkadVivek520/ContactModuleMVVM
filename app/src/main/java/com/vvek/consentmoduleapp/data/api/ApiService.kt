package com.vvek.consentmoduleapp.data.api

import com.vvek.consentmoduleapp.data.model.Contact
import io.reactivex.Single

interface ApiService {

    fun getContacts(): Single<List<Contact>>

}