package com.manta.worldcup.activity

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.manta.worldcup.R
import com.manta.worldcup.adapter.ViewPageAdapter
import com.manta.worldcup.api.repository.Repository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mViewPagerAdapter: ViewPageAdapter;
    private lateinit var mGoogleSignInClient : GoogleSignInClient;
    private lateinit var mAuth: FirebaseAuth
    private val RC_SIGN_IN = 0;

    private val mRepository : Repository by lazy{
        Repository(application);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        mViewPagerAdapter = ViewPageAdapter(supportFragmentManager, lifecycle);

        vp_mainPager.adapter = mViewPagerAdapter;

        TabLayoutMediator(tl_mainTab, vp_mainPager) { tab, position ->
                when (position) {
                    0 -> { tab.text = "토픽"}
                    1 -> {tab.text = "내 이미지"}
                }
        }.attach()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        signIn();
    }

    override fun onStart() {
        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = mAuth.currentUser
//        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
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
                        if(result.isSuccessful){
                            Log.d(javaClass.toString(), "authenticateWithWebServer:success")
                            val user = mAuth.currentUser
                            updateUI(user)
                        }
                        else
                            Snackbar.make(findViewById(R.id.parent), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(javaClass.toString(), "signInWithCredential:failure", task.exception)
                    // ...
                    Snackbar.make(findViewById(R.id.parent), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

            }
    }

    private suspend fun authenticateWithWebServer(idToken : String) = mRepository.sendIdToken(idToken);



}