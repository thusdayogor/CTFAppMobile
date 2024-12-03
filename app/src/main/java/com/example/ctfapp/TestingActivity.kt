package com.example.ctfapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.ctfapp.crypto.CryptoHelper

class TestingActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        val editTextPinCode: EditText = findViewById(R.id.PasswodEditText)
        val buttonPinCode: Button = findViewById(R.id.EnterPinCodeButton)

        var pinCodeHash = getString(R.string.first) + getString(R.string.second)//916291


        buttonPinCode.setOnClickListener {
            val pinCode = editTextPinCode.text.toString().trim()

            if (pinCode == "") {
                Toast.makeText(this, "The pin code can't be an empty string!", Toast.LENGTH_LONG)
                    .show()
            }

            if (CryptoHelper.getMD5Hash(pinCode)!=pinCodeHash)
            {
                Toast.makeText(this, "The pin code doesn't match!", Toast.LENGTH_LONG)
                    .show()
            }
            else
            {
                Toast.makeText(this, "Welcome Tester!", Toast.LENGTH_LONG).show()
                val key = CryptoHelper.generateAESKey(pinCode)
                val decrypted = CryptoHelper.decrypt(getString(R.string.encryptValue),key)

                Log.d("Decrypted", decrypted)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}