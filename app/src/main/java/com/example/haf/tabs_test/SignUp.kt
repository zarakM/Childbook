package com.example.haf.tabs_test

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.VolleyError
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.StringRequest
import android.R.attr.button
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.math.log


class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        var name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val contact = findViewById<EditText>(R.id.contact)
        val radio= findViewById<RadioGroup>(R.id.radioGroup1)
        val sel = radio.checkedRadioButtonId
        var gender = findViewById<RadioButton>(sel)
        val dob = findViewById<EditText>(R.id.name)
        val signup = findViewById<Button>(R.id.signup)
        val pass = findViewById<EditText>(R.id.pass)
        var encryptString=""
        var uri = "https://childbook.000webhostapp.com/parentSignup.php"
        var requestQueue = Volley.newRequestQueue(getApplicationContext())

        signup.setOnClickListener(View.OnClickListener {
            val names = name.text.toString();
            val emails = email.text.toString();
            val contacts = contact.text.toString();
            val genders = gender.text.toString();
            val dobs = dob.text.toString();
            try {
                encryptString = encrypt(pass.text.toString());
            } catch (e : Exception) {
                Log.d("encryption error", e.message)
            }

            val request = object : StringRequest(Request.Method.POST, uri, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Log.d("tagsignup",response)
                    val intent = Intent(applicationContext, parentActivity::class.java)
                    startActivity(intent)
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Log.d("errrr",error.message.toString())
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String,String> {
                       val param = HashMap<String,String>()
                    param.put("name",names)
                    param.put("email",emails)
                    param.put("contact",contacts)
                    param.put("gender",genders)
                    param.put("dob",dobs)
                    param.put("pass",encryptString)
                    return param
                }
            }
            requestQueue.add(request)
        })
    }
    @Throws(Exception::class)
    private fun encrypt(s: String): String {
        val key = generateKey("zarak")
        val c = Cipher.getInstance("AES")
        c.init(Cipher.ENCRYPT_MODE, key)
        val encval = c.doFinal(s.toByteArray())
        return Base64.encodeToString(encval, Base64.DEFAULT)

    }

    @Throws(Exception::class)
    private fun generateKey(s: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = s.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }
    }
