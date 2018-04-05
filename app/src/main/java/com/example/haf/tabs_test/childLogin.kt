package com.example.haf.tabs_test

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class childLogin : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_child_login, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val username = view.findViewById<EditText>(R.id.name)
        val password = view.findViewById<EditText>(R.id.pass)
        var encryptString = ""
        var requestQueue = Volley.newRequestQueue(activity)
        val login = view.findViewById<Button>(R.id.login)

        login.setOnClickListener(View.OnClickListener {
            val name = username.text.toString();
            val pass = password.text.toString();

            try {
                encryptString = encrypt(pass);
            } catch (e : Exception) {
                Log.d("encryption error", e.message)
            }

            var uri = "https://childbook.000webhostapp.com/childLogin.php"
            val request = object : StringRequest(Request.Method.POST, uri, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    if (response=="yes")
                    {
                        val intent = Intent(activity,MainActivity::class.java)
                        intent.putExtra("username",name)
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(activity,"Entered wrong credential", Toast.LENGTH_LONG).show()
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
