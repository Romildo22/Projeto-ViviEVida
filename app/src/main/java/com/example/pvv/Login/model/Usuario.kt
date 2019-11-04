package com.example.pvv.Login.model

import com.example.pvv.Login.helper.ConfiguracaoFirebase


class Usuario {
    var id: String? = null
    var nome: String? = null
    var email: String? = null
    var senha: String? = null
    var caminhoFoto: String? = null
    var campoConfirma: String? = null

    fun salvar() {
        val firebaseRef = ConfiguracaoFirebase.firebase
        val usuariosRef = firebaseRef.child("usuarios").child(id!!)
        usuariosRef.setValue(this)
    }
}
