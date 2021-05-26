package com.vvek.consentmoduleapp.ui.main.view

import android.Manifest
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.vvek.consentmoduleapp.R
import com.vvek.consentmoduleapp.data.api.ApiHelper
import com.vvek.consentmoduleapp.data.api.ApiServiceImpl
import com.vvek.consentmoduleapp.data.model.Contact
import com.vvek.consentmoduleapp.ui.base.ViewModelFactory
import com.vvek.consentmoduleapp.ui.main.adapter.MainAdapter
import com.vvek.consentmoduleapp.ui.main.viewmodel.MainViewModel
import com.vvek.consentmoduleapp.utils.Status
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()

        if(!checkPermissionGranted()) {
            askPermissions()
        }else{
            setupViewModel()
            setupObserver()
        }

    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

        btnregister.setOnClickListener {
            contentResolver
                .registerContentObserver(
                    ContactsContract.Contacts.CONTENT_URI, true,
                    MyContentObserver(mainViewModel)
                )
        }
    }

    private fun setupObserver() {
        mainViewModel.getContacts().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(contacts: List<Contact>) {
        adapter.addData(contacts)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()), applicationContext)
        ).get(MainViewModel::class.java)
    }

    private fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS), 1
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_CONTACTS
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        setupViewModel()
                        setupObserver()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun checkPermissionGranted(): Boolean {
        val hasPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        return hasPermission == PackageManager.PERMISSION_GRANTED
    }


    class MyContentObserver(mainViewModel: MainViewModel) : ContentObserver(null) { //Class for ContentObserver to get callback for change in contact
        private val mainViewModelForObserver = mainViewModel
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            mainViewModelForObserver.setConsentFlag(true)       // Contact list is updated
        }

        override fun deliverSelfNotifications(): Boolean {
            return true
        }
    }

}
