package com.example.mvvm_rpg_ficha.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_rpg_ficha.dataBase.AppDataBase
import com.example.mvvm_rpg_ficha.databinding.ActivityHomeBinding
import com.example.mvvm_rpg_ficha.ui.adapter.RecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeActivity : AppCompatActivity() {

    private val scope = MainScope()
    private val job = Job()
    private val dao by lazy {
        val db = AppDataBase.instancia(this)
        db.fichaDao()
    }

    private val adapter by lazy {
        RecyclerViewAdapter(
            this,
            emptyList()
        )
    }
    private val fichaDao by lazy {
        AppDataBase.instancia(
            this
        ).fichaDao()
    }
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)
        confinguracoesRecyclerView()
        configuraFab()

        lifecycleScope.launch {
            fichaDao.buscaTodos().collect { fichas ->
                adapter.atualiza(fichas)
            }
        }
    }

    private fun configuraFab() {
        val afb = binding.floatingActionButton
        afb.setOnClickListener {
            val intent = Intent(this, FormularioPersonagem::class.java)
            startActivity(intent)
        }
    }

    private fun confinguracoesRecyclerView() {
        val recyclerView = binding.activityHomeRecyclerView
        recyclerView.adapter = adapter
        adapter.clickCharInformacoes = { ficha ->
            val intent = Intent(this, InformacoesDoPersonagem::class.java).apply {
                putExtra(CHAVE_FICHA_ID, ficha.id)
            }
            startActivity(intent)
        }
        adapter.quandoClicarEmEditar = { ficha ->
            val intent = Intent(this, FormularioPersonagem::class.java).apply {
                putExtra(CHAVE_FICHA_ID, ficha.id)
                putExtra(ACTION_TYPE, "EDITAR")
            }
            startActivity(intent)
        }
        adapter.quandoClicarEmRemover = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    fichaDao.delete(it)
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
