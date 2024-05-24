package com.example.contactstask

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contactstask.ui.theme.ContactsTaskTheme
import com.google.android.material.snackbar.Snackbar

class MainActivity : ComponentActivity() {
    private lateinit var contacts: List<Contact>

    private val REQUEST_READ_CONTACTS: Int = 1231



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContactsTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Greeting("Android")


                    Column {
                        ContactsView("Contacts")
                        PlayButton()
                    }
                }
            }
        }


        val filter = IntentFilter()
        filter.addAction("com.example.MainBroadCastReceiver")
        registerReceiver(MainBroadCastReceiver(), filter, RECEIVER_EXPORTED)

        //Sending the broadcast
        val intent = Intent("com.example.MainBroadCastReceiver")
        sendBroadcast(intent)


    }


    //Checks if the service is currently running or not
    //Service runs in background without any interaction from user
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    //-----------Functionality to retrieve contacts-------------
    //Modified function to return list of contacts
    private fun loadContacts(): List<Contact> {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            contacts = getContactList(this)
            Log.d("Contacts", contacts.joinToString(separator = "\n"))
//            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
        } else {
            requestContactsPermission()
            Toast.makeText(this, "Permission!", Toast.LENGTH_SHORT).show()
        }
        return contacts
    }


    //Request Contacts permissions from user
    private fun requestContactsPermission() {
        // Check if the permission has already been granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission has already been granted, do something with the contact list
            val contacts = getContactList(this)
            Log.d("Contacts", contacts.joinToString(separator = "\n"))
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
            // Do something with the contact list
        } else {
            // Permission has not been granted, request it
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                // Explain why the app needs the permission
                // You can show a dialog or a Snackbar here
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "The app needs permission to access your contacts.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                    // Request the permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        REQUEST_READ_CONTACTS
                    )
                }.show()
            } else {
                // Request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_READ_CONTACTS
                )
            }
        }
    }


    //Did the user grant permissions or not
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission has been granted, do something with the contact list
            val contacts = getContactList(this)
            Log.d("Contacts", contacts.joinToString(separator = "\n"))
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
            // Do something with the contact list
        } else {
            // Permission has been denied
            // You can show a dialog or a Snackbar here to explain why the app needs the permission
        }
    }


    //Populate getContactsList
    @SuppressLint("Range")
    fun getContactList(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                //Get the id of Contact
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))

                //Get the name of Contact
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                //Get the phone number of Contact
                val hasPhoneNumber = it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0
                if (hasPhoneNumber) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    //Adding email retrieval here
                    var email: String? = null

                    //Create cursor for email
                    val emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf(id), null
                    )
                    emailCursor?.use { ec ->
                        if (ec.moveToNext()) {
                            email = ec.getString(ec.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                        }
                    }


                    phoneCursor?.use { pc ->
                        while (pc.moveToNext()) {
                            val phoneNumber = pc.getString(pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val contact = Contact(id, name, phoneNumber, email)
                            contacts.add(contact)
                        }
                    }
                    phoneCursor?.close()


                } else {
                    val contact = Contact(name, null, null,null)
                    contacts.add(contact)
                }
            }
        }
        cursor?.close()
        return contacts
    }


    //------------------All Code related to the UI------------------------------
    @Composable
    fun ContactsView(text: String, modifier: Modifier = Modifier) {

        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, //align Contacts title

        ) {
            Text(
                text = text,
                color = Color.Blue,
                fontSize = 34.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            //---------This is real data---------
            var contactsArray = loadContacts()

            //----------This is Dummy Data-------
//            val contactsArray = listOf(
//                Contact("1","John Doe", "123456789","johndoe@mail.com"),
//                Contact("2","Bill Ding", "987654321","billding@phone.com"),
//                Contact("3","Ella Vader", "5859685732","ellavader@desk.com"),
//                Contact("4","Barry Cade", "9094857463","barrycade@outsid.com"),
//                Contact("5","Jason Roth", "4049931234","jasonroth@test.com"),
//                Contact("6","Barb Dwyer", "8904536221","barbdwyer@dkkd.com")
//            )

            ContactList(contacts = contactsArray)

        }

    }

    //Creates composable for contacts based on list passed in and retrieved from from phone contacts
    @Composable
    fun ContactList(contacts: List<Contact>) {
        LazyColumn(
            modifier = Modifier.height(600.dp)
        ) {
            items(contacts) { contact ->

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )


                ) {


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),

                        ) {
                        Text(
                            text = "Id: ${contact.id}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Name: ${contact.name}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Phone Number: ${contact.phoneNumber}", //text to display
                            modifier = Modifier.fillMaxWidth(), //text element will hav full width
                            textAlign = TextAlign.Center //center align the text
                        )
                        Text(
                            text = "Email: ${contact.email}", //text to display
                            modifier = Modifier.fillMaxWidth(), //text element will hav full width
                            textAlign = TextAlign.Center //center align the text

                        )

                    }

                }
            }
        }
    }

    //----------------------Media Controls-----------------
    @Composable
    fun PlayButton(){
        var isPlaying by remember { mutableStateOf(false) }
        val buttonText = if (isPlaying) "Stop Music" else "Play Music"

        Button(
            onClick = {
                isPlaying = !isPlaying // Toggle the value of isPlaying
                musicButtonController()

                      },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue) // Change the background color here

        ) {

            Text(
                text = buttonText //change text on button click

            )
        }




    }

    private fun musicButtonController(){
        if (isMyServiceRunning(MainService::class.java)) {
               // button.text = "Stopped"
                stopService(Intent(this@MainActivity, MainService::class.java))
            } else {
                //button.text = "Started"
                startService(Intent(this@MainActivity, MainService::class.java))
            }
    }

    //-------------------------Set this up to show live preview----------------
    @Preview(showBackground = true)
    @Composable
    fun CustomViewPreview() {
        ContactsTaskTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
                ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ContactsView("Contacts")
                    PlayButton()

                }

            }

        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ContactsTaskTheme {
//        Greeting("Android")
//    }
//}


}