package com.example.pickupsport

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.signup.*
import kotlinx.android.synthetic.main.signup.view.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.text.TextWatcher

class user_signup : Fragment() {

    private var errorFields:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.signup, container, false)

        view.register_button.setOnClickListener{
            doAsync {
                Log.d("DEBUG", "button clicked");

                println("name: " + signupname_edit_text.text.toString())
                println("email: " + signupemail_edit_text.text.toString())
                println("phone: " + signupphone_edit_text.text.toString())
                println("passwd: " + signuppw_edit_text.text.toString())

                val url = "https://my-apad-project.appspot.com/app/signup"
                val map: HashMap<String, String> = hashMapOf(
                    "name" to signupname_edit_text.text.toString(),
                    "email" to signupemail_edit_text.text.toString(),
                    "phone" to signupphone_edit_text.text.toString(),
                    "password" to signuppw_edit_text.text.toString())

                val client = OkHttpClient()
                val req = OkHttpRequest(client)
                val resp = req.fetchPost(url, map)
                println("resp: " + resp)
                println(resp?.javaClass?.name)

                uiThread {
                    if (resp.equals("False")) {
                        println("Sign Up Failed")
                    } else {
                        // Navigate to the next Fragment.
                        println("Success")
                        (activity as NavigationHost).navigateTo(LoginFragment(), false)
                    }
                }
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (signupname_edit_text.text.toString().equals("")) {
//            println("name empty")
//            signupname.error = getString(R.string.error_mandatory)
//        } else if (signupemail_edit_text.text.toString().equals("")) {
//            println("email empty")
//            signupemail.error = getString(R.string.error_mandatory)
//        } else if (signupphone_edit_text.text.toString().equals("")) {
//            println("phone empty")
//            signupphone.error = getString(R.string.error_mandatory)
//        } else if (signuppw_edit_text.text.toString().equals("")) {
//            println("password empty")
//            signuppw.error = getString(R.string.error_mandatory)
//        } else {
//            println("no errors")
//            view.register_button.isEnabled
//        }




        signupname_edit_text.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                textValidations()
                println("do nothing")
            }

            override fun afterTextChanged(p0: Editable?) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

        })

        signupemail_edit_text.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                textValidations()
                println("do nothing")
            }

            override fun afterTextChanged(p0: Editable?) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

        })

        signupphone_edit_text.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                textValidations()
                println("do nothing")
            }

            override fun afterTextChanged(p0: Editable?) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

        })

        signuppw_edit_text.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                textValidations()
                println("do nothing")
            }

            override fun afterTextChanged(p0: Editable?) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(textValidations()) {
                    view.register_button.isEnabled = true
                } else {
                    view.register_button.isEnabled = false
                }
            }

        })
    }

    fun textValidations() : Boolean {
        if (!signupname_edit_text.text.toString().equals("") &&
            !signupemail_edit_text.text.toString().equals("") &&
            !signupphone_edit_text.text.toString().equals("") &&
            !signuppw_edit_text.text.toString().equals("")) {
            println("we are good")
            return true
        } else {
            println("no good")
            return false
        }
    }

}