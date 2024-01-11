package com.example.noteappprac

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteappprac.databinding.ActivityRegistrationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class Registration : AppCompatActivity() {
    private val binding by lazy { ActivityRegistrationBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // navigation
        binding.rLogin.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
            finish()
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.register.setOnClickListener {

            val email = binding.rEmail.text.toString()
            val username = binding.rUsername.text.toString()
            val password = binding.rPassword.text.toString()
            val rpassword = binding.rrPassword.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || rpassword.isEmpty()) {
                Toast.makeText(this, "fill all", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty() || rpassword.isEmpty()) {
                Toast.makeText(this, "password mismatch", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "createUserWithEmail:success", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, LogIn::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleRegister.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseCredential(account.idToken)
                } catch (e: ApiException) {
                    Toast.makeText(
                        this,
                        "signInResult:failed code=${task.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun firebaseCredential(idToken: String?) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LogIn::class.java))
                    Toast.makeText(
                        this,
                        "signInWithCredential:success",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "signInResult:failed code=${task.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}