package com.vvek.consentmoduleapp.data.api

import com.vvek.consentmoduleapp.data.model.Contact
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

class ApiServiceImpl : ApiService {

    override fun getContacts(): Single<List<Contact>> {
        return Rx2AndroidNetworking.get("FetchContactApiPath")          //To fetch contacts from server
            .build()
            .getObjectListSingle(Contact::class.java)
    }

}