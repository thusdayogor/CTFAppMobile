package com.example.ctfapp.crypto

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

object CryptoHelper
{

    private val fixedIV = byteArrayOf(
        0x4E.toByte(), 0xF3.toByte(), 0x7A.toByte(), 0x8C.toByte(),
        0xD1.toByte(), 0xB5.toByte(), 0x5E.toByte(), 0xA2.toByte(),
        0x63.toByte(), 0x45.toByte(), 0x88.toByte(), 0x9A.toByte()
    )

    public fun generateAESKey(pinCode: String): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val keyBytes = digest.digest(pinCode.toByteArray(Charsets.UTF_8))
        return SecretKeySpec(keyBytes, "AES")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    public fun decrypt(encryptedText: String, secretKey: SecretKey): String {
        val parts = encryptedText.split(":")
        val encryptedBytes = Base64.getDecoder().decode(parts[1])

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmSpec = GCMParameterSpec(128, fixedIV)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return String(decryptedBytes, Charsets.UTF_8)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun encrypt(plainText: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val gcmSpec = GCMParameterSpec(128, fixedIV)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)

        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        Log.d("Encrypted", encryptedBytes.toString())

        return Base64.getEncoder().encodeToString(fixedIV) + ":" + Base64.getEncoder().encodeToString(encryptedBytes)
    }


    public fun getMD5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}