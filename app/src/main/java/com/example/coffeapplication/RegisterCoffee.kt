package com.example.coffeapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.toast

class RegisterCoffee: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_coffee)
        //Initialize firebase auth
        auth = Firebase.auth

        val loginText: TextView = findViewById(R.id.loginText)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginCoffee::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.register_button)
        registerButton.setOnClickListener {
            performSignUp()
        }
    }
    private fun performSignUp(){
        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordRegisterEditText)

        if(email.text.isEmpty() || password.text.isEmpty()){
             Toast.makeText(this,"please fill all fields",Toast.LENGTH_SHORT)
                 .show()
            return
        }
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)

            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

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