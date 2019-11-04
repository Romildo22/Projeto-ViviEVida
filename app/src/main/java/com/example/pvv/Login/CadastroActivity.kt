package com.example.pvv.Login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CadastroActivity : AppCompatActivity() {

    private var campoNome: EditText? = null
    private var campoEmail: EditText? = null
    private var campoSenha: EditText? = null
    private var campoConfirma: EditText? = null
    private var botaoCadastrar: Button? = null
    private var progressBar: ProgressBar? = null

    private var usuario: Usuario? = null

    private var autenticacao: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        inicializarComponentes()

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Cadastro - Vivi é Vida"
        toolbar.titleMarginStart = 110
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Cadastrar usuario
        progressBar!!.visibility = View.GONE
        botaoCadastrar!!.setOnClickListener {
            val textoNome = campoNome!!.text.toString()
            val textoEmail = campoEmail!!.text.toString()
            val textoSenha = campoSenha!!.text.toString()
            val textoConfirma = campoConfirma!!.text.toString()

            if (!textoNome.isEmpty()) {
                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        if (!textoConfirma.isEmpty()) {
                            if (textoConfirma == textoSenha) {

                                usuario = Usuario()
                                usuario!!.nome = textoNome
                                usuario!!.email = textoEmail
                                usuario!!.senha = textoSenha
                                usuario!!.campoConfirma = textoConfirma
                                cadastrar(usuario)

                            } else {
                                Toast.makeText(this@CadastroActivity, "Preencha  a senha correta!",
                                        Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Toast.makeText(this@CadastroActivity, "Confirme a senha!",
                                    Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@CadastroActivity, "Preencha a senha!",
                                Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@CadastroActivity, "Preencha o E-mail!",
                            Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this@CadastroActivity, "Preencha o nome!",
                        Toast.LENGTH_SHORT).show()
            }
        }


    }

    /**
     * Método abaixo responsável por cadastrar usuário com e-mail e senha
     * e fazer validações ao fazer o cadastro
     */

    fun cadastrar(usuario: Usuario?) {
        progressBar!!.visibility = View.VISIBLE
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao
        autenticacao!!.createUserWithEmailAndPassword(
                usuario!!.email!!,
                usuario.senha!!
        ).addOnCompleteListener(
                this
        ) { task ->
            if (task.isSuccessful) {
                progressBar!!.visibility = View.GONE
                Toast.makeText(this@CadastroActivity,
                        "Cadastro com sucesso",
                        Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()


            } else {
                progressBar!!.visibility = View.GONE

                var erroExcecao = ""
                try {
                    throw task.getException()!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    erroExcecao = "Digite uma senha mais forte!"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    erroExcecao = "Por favor, digite um e-mail válido"
                } catch (e: FirebaseAuthUserCollisionException) {
                    erroExcecao = "Esta conta já foi cadastrada"
                } catch (e: Exception) {
                    erroExcecao = "ao cadastrar usuário: " + e.message
                    e.printStackTrace()
                }

                Toast.makeText(this@CadastroActivity,
                        "Erro:$erroExcecao",
                        Toast.LENGTH_SHORT).show()


            }
        }


    }

    fun inicializarComponentes() {

        campoNome = findViewById(R.id.editCadastroNome)
        campoEmail = findViewById(R.id.editCadastroEmail)
        campoSenha = findViewById(R.id.editCadastroSenha)
        campoConfirma = findViewById(R.id.editConfirmarSenha)
        botaoCadastrar = findViewById(R.id.buttonEntrar)
        progressBar = findViewById(R.id.progressCadastro)

        campoNome!!.requestFocus()


    }

}
