package com.example.pvv.MenuLateral.SobreProjeto

import android.os.Bundle
import com.example.pvv.Activitys.BaseActivity
import com.example.pvv.R

class SobreProjeto : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre_projeto)

        //setando a toolbar e colocando o nome
        setUpToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
