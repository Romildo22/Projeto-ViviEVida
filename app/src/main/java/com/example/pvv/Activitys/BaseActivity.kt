package com.example.pvv.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.pvv.Login.LoginActivity
import com.example.pvv.Login.helper.ConfiguracaoFirebase
import com.example.pvv.MenuLateral.JuntarAoProjeto.SeJuntarProjeto
import com.example.pvv.MenuLateral.Mapa.Mapa
import com.example.pvv.MenuLateral.SobreAPP.SobreApp
import com.example.pvv.MenuLateral.SobreProjeto.SobreProjeto
import com.example.pvv.MenuLateral.WebView
import com.example.pvv.R.*
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : livroandroid.lib.activity.BaseActivity() {

    protected var drawerLayout: DrawerLayout? = null
    private var autenticacao: FirebaseAuth? = null
    //private var usuario: Usuario? = null

    private val permissoes = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    // Configura a Toolbar
    @SuppressLint("ResourceAsColor")
    protected fun setUpToolbar() {
        val toolbar = findViewById(id.toolbar) as Toolbar
        if (toolbar != null) {
            toolbar.title = "Projeto Vivi é Vida"
            toolbar.titleMarginStart = 100
            setSupportActionBar(toolbar)
        }
    }
    // Configura o nav drawer
    protected fun setupNavDrawer() {
        // Drawer Layout
        val actionBar = getSupportActionBar()

        // Ícone do menu do Nav Drawer
        actionBar!!.setHomeAsUpIndicator(drawable.ic_menu)
        actionBar.setDisplayHomeAsUpEnabled(true)

        drawerLayout = findViewById<DrawerLayout>(id.drawer_layout)

        val navigationView = findViewById<NavigationView>(id.nav_view)

        //Verificação do drawerLayout
        if (navigationView != null && drawerLayout != null) {
            // Atualiza a imagem e os textos do header
            setNavViewValues(navigationView, string.id_projeto, string.id_pFrase, drawable.logotipo1)
            // Trata o evento de clique no menu
            navigationView.setNavigationItemSelectedListener { menuItem ->
                // Seleciona a linha
                //menuItem.setChecked(true);
                // Fecha o menu
                drawerLayout!!.closeDrawers()
                // Trata o evento do menu
                onNavDrawerItemSelected(menuItem)
                true
            }
        }
    }

    // Trata o evento do menu lateral
    private fun onNavDrawerItemSelected(menuItem: MenuItem) {
        when (menuItem.itemId) {

            id.btn_SobreProjeto-> {
              val intent = Intent(context, SobreProjeto::class.java)
                startActivity(intent)
                //Toast.makeText(this,"Configurar tela sobre Eventos",Toast.LENGTH_LONG).show()
            }
            id.btn_JuntarProjeto -> {
                val intent = Intent(context, SeJuntarProjeto::class.java)
                startActivity(intent)
                //Toast.makeText(this,"Configurar tela sobre juntar-se",Toast.LENGTH_LONG).show()
            }
            id.btn_ONGs -> {
                val intent = Intent(context, Mapa::class.java)
                startActivity(intent)
                //Toast.makeText(this,"Configurar tela sobre o mapa das ONG'S",Toast.LENGTH_LONG).show()
            }
            id.btn_Instagram -> { toast("Sessão sendo encerrada")
                val intent = Intent(context, WebView::class.java)
                intent.putExtra("valor", "1")
                startActivity(intent)
            }
            id.btn_Facebook-> {
                val intent = Intent(context, WebView::class.java)
                 intent.putExtra("valor", "2")
                 startActivity(intent)
            }
            id.btn_App -> {
                val intent = Intent(context, SobreApp::class.java)
                startActivity(intent)
               // Toast.makeText(this,"tela sobre o APP",Toast.LENGTH_LONG).show()
            }
            id.btn_Sair ->{
                toast("Sessão sendo encerrada")
                val intent = Intent(context, LoginActivity::class.java)
                deslogarUsuario()
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                // Trata o clique no botão que abre o menu
                if (drawerLayout != null) {
                    openDrawer()
                    return true
                }
        }
        return super.onOptionsItemSelected(item)
    }

    // Abre o menu lateral
    protected fun openDrawer() {
        if (drawerLayout != null) {
            drawerLayout!!.openDrawer(GravityCompat.START)
        }
    }

    // Fecha o menu lateral
    protected fun closeDrawer() {
        drawerLayout?.closeDrawer(GravityCompat.START)
    }

    // Atualiza os dados do header do Navigation View
    // recebendo NOME,EMAIL e IMAGEM
    fun setNavViewValues(navView: NavigationView, titulo: Int, subtitulo: Int, img: Int) {
        val headerView = navView.getHeaderView(0)
        val txt1Cima = headerView.findViewById(id.id_txt1) as TextView
        val txt2Baixo = headerView.findViewById(id.id_txt2) as TextView
        val imgLogo = headerView.findViewById(id.id_img) as ImageView
        txt1Cima.setText(titulo)
        txt2Baixo.setText(subtitulo)
        imgLogo.setImageResource(img)
    }

    fun deslogarUsuario() {
        autenticacao = ConfiguracaoFirebase.firebaseAutenticacao
        try {
            autenticacao?.signOut()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
