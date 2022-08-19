package com.example.coffeapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeapplication.mainModule.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginCoffee: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_coffee)
        auth = Firebase.auth
        val registerText: TextView= findViewById(R.id.registerText)
        registerText.setOnClickListener{
         val intent = Intent(this,RegisterCoffee::class.java)
            startActivity(intent)
        }
        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener{
            performLogin()
        }


    }

    private fun performLogin() {
        val email: EditText = findViewById(R.id.loginEditText)
        val password: EditText = findViewById(R.id.passwordEditText)

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success

                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(baseContext, "success",
                        Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error occurred ${it.localizedMessage}",Toast.LENGTH_SHORT)
                    .show()
            }
    }
}