package com.example.mvvm_rpg_ficha.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_rpg_ficha.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        // Inicializa o ProgressBar
        progressBar = binding.progressBar
    }

    override fun onResume() {
        super.onResume()
        configBotaoEntrar()
    }

    private fun configBotaoEntrar() {
        val entrar = binding.mainActivityBotaoEntrar
        entrar.setOnClickListener {
            showLoadingAndStartHomeActivity()
        }
    }

    private fun showLoadingAndStartHomeActivity() {
        progressBar.visibility = View.VISIBLE // Mostrar o ProgressBar
        // Simular um atraso para o carregamento
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            progressBar.visibility = View.GONE // Ocultar o ProgressBar
        }, 1000) // Atraso de 500 milissegundos (0.5 segundos)
    }
}
