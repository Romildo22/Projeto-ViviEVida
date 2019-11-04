package com.example.pvv.MenuLateral.JuntarAoProjeto

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pvv.Activitys.BaseActivity
import com.example.pvv.R

class SeJuntarProjeto : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juntar_projeto)

        //setando a toolbar e colocando o nome
        setUpToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
