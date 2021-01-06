package com.manta.worldcup.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.manta.worldcup.R
import com.manta.worldcup.api.repository.Repository
import com.manta.worldcup.helper.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    //다른 액티비티로 가도 유지하기 위해(로그아웃을 하려면 유지할 필요가 있음) companion object로 뺀다.
    companion object {
        private var mGoogleSignInClient: GoogleSignInClient? = null;
        private lateinit var mAuth: FirebaseAuth
         fun initializeGoogleSigninClient(activity : AppCompatActivity) {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
            mAuth = FirebaseAuth.getInstance();

        }

    }

    private val RC_SIGN_IN = 0;

    private val mRepository: Repository by lazy {
        Repository(application);
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)

        sign_in_button.setOnClickListener {
            signIn();
        }

        //"여기" 텍스트를 누르면 개인정보이용약관으로 리다이렉트
        val mTransform = Linkify.TransformFilter { match, url -> ""; }
        val pattern = Pattern.compile(resources.getString(R.string.here));
        Linkify.addLinks(tv_login_link, pattern, resources.getString(R.string.personal_info_url), null, mTransform);

    }


    override fun onStart() {
        super.onStart()

        if (mGoogleSignInClient == null) {
            initializeGoogleSigninClient(this);
        } else {
            mGoogleSignInClient?.signOut()?.addOnSuccessListener {
                initializeGoogleSigninClient(this)
            }
        }

    }


    private fun onSignInSuccess(currentUser: FirebaseUser?) {
        finish();
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(javaClass.toString(), "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(javaClass.toString(), "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(javaClass.toString(), "signInWithCredential:success")

                    //웹서버에도 인증
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = authenticateWithWebServer(idToken);
                        if (result.isSuccessful) {
                            Log.d(javaClass.toString(), "authenticateWithWebServer:success")
                            //토큰을 SharedPreference에 저장
                            val pref = applicationContext.getSharedPreferences(Constants.PREF_FILENAME_TOKEN, Context.MODE_PRIVATE)
                            pref.edit().putString(Constants.PREF_TOKEN, result.body()).apply();

                            val user = mAuth.currentUser
                            onSignInSuccess(user)
                        } else
                            Snackbar.make(findViewById(R.id.parent), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(javaClass.toString(), "signInWithCredential:failure", task.exception)
                    // ...
                    Snackbar.make(findViewById(R.id.parent), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }

            }
    }

    private suspend fun authenticateWithWebServer(idToken: String) = mRepository.sendIdToken(idToken);


}