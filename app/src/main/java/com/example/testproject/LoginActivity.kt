package com.example.testproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG ="MainAcc"
class LoginActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private val FB_SIGN_IN = 1004
    private val GG_SIGN_IN = 1005
    private val email="email"
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callbackManager = CallbackManager.Factory.create()

        login_google.setOnClickListener {
            val provider = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(provider)
                    .build(),
                GG_SIGN_IN
            )
        }

        login_facebook.setOnClickListener {
            Log.d(TAG, "onCreateView: Start to call Facebook API")
            LoginManager.getInstance().logInWithReadPermissions(this, listOf(email))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d(TAG, "onSuccess:")
                    getUserProfile(result?.accessToken, result?.accessToken?.userId)
                }
                override fun onCancel() {
                    Log.d(TAG, "onCancel: ")
                }
                override fun onError(error: FacebookException?) {
                    Log.e(TAG, "onError: $error")
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FB_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        } else if (requestCode == GG_SIGN_IN) {
            user = FirebaseAuth.getInstance().currentUser
        }
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {
        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(
            token,
            "/$userId/",
            parameters,
            HttpMethod.GET
        ) { response ->
            val jsonObject = response.jsonObject

            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true)
                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
            }

            // Facebook Id
            if (jsonObject.has("id")) {
                val facebookId = jsonObject.getString("id")
                Log.i("Facebook Id: ", facebookId.toString())
            } else {
                Log.i("Facebook Id: ", "Not exists")
            }

            // Facebook First Name
            if (jsonObject.has("first_name")) {
                val facebookFirstName = jsonObject.getString("first_name")
                Log.i("Facebook First Name: ", facebookFirstName)
            } else {
                Log.i("Facebook First Name: ", "Not exists")
            }

            // Facebook Middle Name
            if (jsonObject.has("middle_name")) {
                val facebookMiddleName = jsonObject.getString("middle_name")
                Log.i("Facebook Middle Name: ", facebookMiddleName)
            } else {
                Log.i("Facebook Middle Name: ", "Not exists")
            }

            // Facebook Last Name
            if (jsonObject.has("last_name")) {
                val facebookLastName = jsonObject.getString("last_name")
                Log.i("Facebook Last Name: ", facebookLastName)
            } else {
                Log.i("Facebook Last Name: ", "Not exists")
            }

            // Facebook Name
            if (jsonObject.has("name")) {
                val facebookName = jsonObject.getString("name")
                Log.i("Facebook Name: ", facebookName)
            } else {
                Log.i("Facebook Name: ", "Not exists")
            }

            // Facebook Profile Pic URL
            if (jsonObject.has("picture")) {
                val facebookPictureObject = jsonObject.getJSONObject("picture")
                if (facebookPictureObject.has("data")) {
                    val facebookDataObject = facebookPictureObject.getJSONObject("data")
                    if (facebookDataObject.has("url")) {
                        val facebookProfilePicURL = facebookDataObject.getString("url")
                        Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                    }
                }
            } else {
                Log.i("Facebook Profile Pic URL: ", "Not exists")
            }

            // Facebook Email
            if (jsonObject.has("email")) {
                val facebookEmail = jsonObject.getString("email")
                Log.i("Facebook Email: ", facebookEmail)
            } else {
                Log.i("Facebook Email: ", "Not exists")
            }
        }.executeAsync()
    }


}