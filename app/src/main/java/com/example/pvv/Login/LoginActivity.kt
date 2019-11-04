package com.example.pvv.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.example.pvv.Activitys.MainActivity
import com.example.pvv.Login.helper.ConfiguracaoFirebase
import com.example.pvv.Login.model.Usuario
import com.example.pvv.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var campoEmail: EditText? = null
    private var campoSenha: EditText? = null
    private var botaoEntrar: Button? = null
    private var progressBar: ProgressBar? = null

    private var usuario: Usuario? = null
    private var autenticacao: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        verificarUsuarioLogado()
        inicializarComponentes()

        //Fazer login do usuario
        progressBar!!.visibility = View.GONE
        botaoEntrar!!.setOnClickListener {
            val textoEmail = campoEmail!!.text.toString()
            val textoSenha = campoSenha!!.text.toString()

            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {


                    usuario = Usuario()
                    usuario!!.email = textoEmail
                    usuario!!.senha = textoSenha
                    validarLogin(usuario)


                } else {
                    Toast.makeText(this@LoginActivity, "Preencha a senha!",
                            Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(this@LoginActivity, "Preencha o e-mail!",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao
        if (autenticacao!!.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }


    fun validarLogin(usuario: Usuario?) {

        progressBar!!.visibility = View.VISIBLE
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao

        autenticacao!!.signInWithEmailAndPassword(
                usuario!!.email!!,
                usuario.senha!!
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar!!.visibility = View.GONE
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@LoginActivity,
                        "Erro ao fazer login",
                        Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE

            }
        }
    }

    fun abrirReset(view: View) {
        val i = Intent(this@LoginActivity, ResetActivity::class.java)
        startActivity(i)
    }


    fun abrirCadastro(view: View) {
        val i = Intent(this@LoginActivity, CadastroActivity::class.java)
        startActivity(i)
    }


    fun inicializarComponentes() {


        campoEmail = findViewById(R.id.editLoginEmail)
        campoSenha = findViewById(R.id.editLoginSenha)
        botaoEntrar = findViewById(R.id.buttonEntrar)
        progressBar = findViewById(R.id.progressLogin)

        campoEmail!!.requestFocus()


    }


}
