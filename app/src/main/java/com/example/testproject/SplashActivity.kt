package com.example.testproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_splash.*

private const val TAG = "Splash Activity"
class SplashActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private val RC_SIGN_IN = 1004
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        signInOnClick.setOnClickListener {
            val customLayout : AuthMethodPickerLayout = AuthMethodPickerLayout
                .Builder(R.layout.login_method)
                .setFacebookButtonId(R.id.login_facebook)
                .setGoogleButtonId(R.id.login_google)
                .setPhoneButtonId(R.id.login_phone)
                .build()

            startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                    .setAvailableProviders(arrayListOf(
                        AuthUI.IdpConfig.FacebookBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                .setAuthMethodPickerLayout(customLayout)
                .build(),RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            user = FirebaseAuth.getInstance().currentUser
            Log.d(TAG, "onActivityResult: Success")
        }
    }
}