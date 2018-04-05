package com.example.haf.tabs_test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.android.volley.VolleyError
import org.json.JSONException
import android.widget.Toast
import org.json.JSONObject
import org.json.JSONArray
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.JsonObjectRequest
import android.R.id.button2
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.EditText
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class loginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var uri = "https://childbook.000webhostapp.com/parent_login.php"
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        var encryptString = ""
        var requestQueue = Volley.newRequestQueue(getApplicationContext())
        val signupForParent = findViewById<Button>(R.id.signup)
        val login = findViewById<Button>(R.id.login)

        signupForParent.setOnClickListener({
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        })

        login.setOnClickListener(View.OnClickListener {
            val name = username.text.toString();
            val pass = password.text.toString();

            try {
                encryptString = encrypt(pass);
            } catch (e : Exception) {
                Log.d("encryption error", e.message)
            }

            var uri = "https://childbook.000webhostapp.com/parentLogin.php"
            val request = object : StringRequest(Request.Method.POST, uri, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Log.d("par3",response)
                    Log.d("par2",encryptString)
                    Log.d("par1",name)

                    if (response=="yes")
                    {
                        val intent = Intent(applicationContext,ParentsActivity::class.java)
                        intent.putExtra("username",name)
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(applicationContext,"Entered wrong credential", Toast.LENGTH_LONG).show()
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Log.d("tagparentlogin",""+error.message)
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param = HashMap<String, String>()
                    param.put("user",name)
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
