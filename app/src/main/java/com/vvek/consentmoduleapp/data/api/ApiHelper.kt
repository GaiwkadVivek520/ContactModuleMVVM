package com.vvek.consentmoduleapp.data.api

class ApiHelper(private val apiService: ApiService) {

    fun getContacts() = apiService.getContacts()

}