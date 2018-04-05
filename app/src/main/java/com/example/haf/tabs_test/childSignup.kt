package com.example.haf.tabs_test

import android.app.Activity
import android.support.v4.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_parents.view.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class childSignup : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_child_signup, container, false)
        return rootView
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        val name = rootView.findViewById<EditText>(R.id.cname)
        val radio= rootView.findViewById<RadioGroup>(R.id.radioGroup1)
        //var gender = rootView.findViewById<RadioButton>(radio.checkedRadioButtonId)
        val dob = rootView.findViewById<EditText>(R.id.cdob)
        val pass = rootView.findViewById<EditText>(R.id.cpass)
        val signup = rootView.findViewById<Button>(R.id.csignup)

        var encryptString=""
        var uri = "http://childbook.000webhostapp.com/childSignup.php"
        var requestQueue = Volley.newRequestQueue(activity)

        signup.setOnClickListener(View.OnClickListener {
            val names = name.text.toString();
            //val genders = gender.text.toString();
            val dobs = dob.text.toString();
            try {
                encryptString = encrypt(pass.text.toString());
            } catch (e : Exception) {
                Log.d("encryption error", e.message)
            }

            val request = object : StringRequest(Request.Method.POST, uri, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    if(response == "entered")
                    {
                        val intent = Intent(activity,MainActivity::class.java)
                        intent.putExtra("username",names)
                        startActivity(intent)
                    }

                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {

                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param = HashMap<String, String>()
                    param.put("name",names)
                    param.put("pass",encryptString)
                    //      param.put("gender",genders)
                    param.put("dob",dobs)
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
