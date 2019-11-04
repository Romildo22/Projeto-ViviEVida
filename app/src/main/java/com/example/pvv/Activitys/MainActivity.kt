package com.example.pvv.Activitys


import android.os.Bundle
import android.support.v4.view.ViewPager
import com.example.pvv.Fragment.EventosFragment
import com.example.pvv.Fragment.ProximosFragment
import com.example.pvv.R
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setando a toolbar e colocando o nome
        setUpToolbar()
        setupNavDrawer()


        //Configurar abas
        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager,
            FragmentPagerItems.with(this)
                .add(R.string.fragment1, EventosFragment::class.java!!)
                .add(R.string.fragment2, ProximosFragment::class.java!!)
                .create()
        )
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = adapter

        val viewPagerTab = findViewById<SmartTabLayout>(R.id.viewPagerTab)
        viewPagerTab.setViewPager(viewPager)
    }
}
