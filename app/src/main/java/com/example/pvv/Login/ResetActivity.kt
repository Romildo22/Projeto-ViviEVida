package com.example.pvv.Login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.support.v7.widget.Toolbar

import com.example.pvv.Login.model.Usuario
import com.example.pvv.R
import com.google.firebase.auth.FirebaseAuth

class ResetActivity : AppCompatActivity() {

    private val campoEmail2: EditText? = null
    private val usuario: Usuario? = null
    private val buttonEnviar: Button? = null
    private val progressBar: ProgressBar? = null
    private var mToolbar: Toolbar? = null
    private var ResetPasswordSendEmailButton: Button? = null
    private var ResetEmailInput: EditText? = null
    private var autenticacao: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Recuperar senha"
        toolbar.titleMarginStart = 110
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        autenticacao = FirebaseAuth.getInstance()

        ResetPasswordSendEmailButton = findViewById<View>(R.id.buttonEnviar) as Button
        ResetEmailInput = findViewById<View>(R.id.campoEmail2) as EditText

        ResetPasswordSendEmailButton!!.setOnClickListener {
            val userEmail = ResetEmailInput!!.text.toString()

            if (TextUtils.isEmpty(userEmail)) {
                Toast.makeText(this@ResetActivity, " Por favor, digite um email valido",
                        Toast.LENGTH_SHORT).show()
            } else {
                autenticacao!!.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ResetActivity, "Por favor verifique seu Email",
                                Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ResetActivity, LoginActivity::class.java))
                    } else {
                        val message = task.exception!!.message
                        Toast.makeText(this@ResetActivity, "Erro:$message",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
