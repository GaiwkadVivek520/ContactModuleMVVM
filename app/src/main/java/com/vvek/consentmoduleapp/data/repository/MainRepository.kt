package com.vvek.consentmoduleapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.vvek.consentmoduleapp.data.api.ApiHelper
import com.vvek.consentmoduleapp.data.model.Contact
import io.reactivex.Single
import java.util.*


class MainRepository(private val apiHelper: ApiHelper, private val applicationContext: Context) {

    fun getContactsFromApi(): Single<List<Contact>> {
        return apiHelper.getContacts()
    }

    fun getContacts(): Single<List<Contact>> {
        val list : ArrayList<Contact> = ArrayList()
        val items: Single<List<Contact>> = Single.just(list)


        val cursor: Cursor = applicationContext.contentResolver
            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)!!
        Log.i("TAG", "fetchContacts: cursor.getCount() is " + cursor.count)
        if ((cursor?.count ?: 0) > 0) {
            while (cursor.moveToNext()) {
                val name: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact = Contact(0,  name, phone,"")
                Log.i("TAG", "fetchContacts: phone is $phone")
                list.add(contact)
            }
        }
        cursor?.close()

        return items

    }

    fun getConsentFlag():Boolean {
        val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean("flag",false)
    }

    fun setConsentFlag(flag: Boolean) {
            val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(
                "myPrefs",
                Context.MODE_PRIVATE
            )

            val editor = sharedPref.edit()
            editor.putBoolean("flag", !flag)
            editor.apply()
    }

}