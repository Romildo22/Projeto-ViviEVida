package com.example.pvv.MenuLateral.Doacoes

import android.os.Bundle
import com.example.pvv.Activitys.BaseActivity
import com.example.pvv.R

class Doacoes : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doacoes)

        setUpToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
