package com.example.contactstask

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PhoneContactManager {
//
//    private val REQUEST_READ_CONTACTS: Int = 1231
//
//
//    fun loadContacts(context: Context){
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.READ_CONTACTS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            val contacts = getContactList(context)
//            Log.d("Contacts", contacts.joinToString(separator = "\n"))
////            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
//        } else {
//            requestContactsPermission(context);
//            Toast.makeText(context, "Permission!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    private fun getContactList(context: Context): List<Contact> {
//        val contacts = mutableListOf<Contact>()
//        val contentResolver = context.contentResolver
//        val cursor = contentResolver.query(
//            ContactsContract.Contacts.CONTENT_URI,
//            null, null, null, null
//        )
//        cursor?.use {
//            while (it.moveToNext()) {
//                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
//                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                val hasPhoneNumber =
//                    it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0
//                if (hasPhoneNumber) {
//                    val phoneCursor = contentResolver.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                        arrayOf(id),
//                        null
//                    )
//                    phoneCursor?.use { pc ->
//                        while (pc.moveToNext()) {
//                            val phoneNumber =
//                                pc.getString(pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                            val contact = Contact(name, phoneNumber)
//                            contacts.add(contact)
//                        }
//                    }
//                    phoneCursor?.close()
//                } else {
//                    val contact = Contact(name, null)
//                    contacts.add(contact)
//                }
//            }
//        }
//        cursor?.close()
//        return contacts
//    }
//
//
//
//    private fun requestContactsPermission(context:Context) {
//        // Check if the permission has already been granted
//        if (ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.READ_CONTACTS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission has already been granted, do something with the contact list
//            val contacts = getContactList(this)
//            Log.d("Contacts", contacts.joinToString(separator = "\n"))
//            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
//            // Do something with the contact list
//        } else {
//            // Permission has not been granted, request it
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    Manifest.permission.READ_CONTACTS
//                )
//            ) {
//                // Explain why the app needs the permission
//                // You can show a dialog or a Snackbar here
//                Snackbar.make(
//                    context.(android.R.id.content),
//                    "The app needs permission to access your contacts.",
//                    Snackbar.LENGTH_INDEFINITE
//                ).setAction("OK") {
//                    // Request the permission
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(Manifest.permission.READ_CONTACTS),
//                        REQUEST_READ_CONTACTS
//                    )
//                }.show()
//            } else {
//                // Request the permission
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.READ_CONTACTS),
//                    REQUEST_READ_CONTACTS
//                )
//            }
//        }
//    }
}