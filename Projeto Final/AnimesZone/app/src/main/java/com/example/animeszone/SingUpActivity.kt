package com.example.animeszone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SingUpActivity : AppCompatActivity() {
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sing_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authService = AuthService()

        findViewById<Button>(R.id.BtnSignUpCadastrar).setOnClickListener {
            val uname = findViewById<EditText>(R.id.SignUpUsername).text.toString()
            val email = findViewById<EditText>(R.id.SignUpEmail).text.toString()
            val pswd = findViewById<EditText>(R.id.SignUpPassword).text.toString()

            if(uname.isEmpty() || email.isEmpty() || pswd.isEmpty()){
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authService.registerUser(this, uname, email, pswd) { success, message ->
                if (!success) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    return@registerUser
                }

                // Se sucesso, mostra mensagem e atualiza a tela
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("username", uname)
                }
                startActivity(intent)
                finish()
            }
        }

        findViewById<Button>(R.id.BtnSignUpLogin).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}