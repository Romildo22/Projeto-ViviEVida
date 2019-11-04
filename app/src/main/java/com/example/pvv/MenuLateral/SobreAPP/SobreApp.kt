package com.example.pvv.MenuLateral.SobreAPP

import android.os.Bundle
import com.example.pvv.Activitys.BaseActivity
import com.example.pvv.R

class SobreApp : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre_app)

        setUpToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
