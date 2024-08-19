package com.example.mvvm_rpg_ficha.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mvvm_rpg_ficha.R
import com.example.mvvm_rpg_ficha.dataBase.AppDataBase
import com.example.mvvm_rpg_ficha.databinding.ActivityInformacoesDoPersonagemBinding
import com.example.mvvm_rpg_ficha.model.Ficha
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class InformacoesDoPersonagem : AppCompatActivity() {

    private var fichaId: Long = 0L
    private var ficha: Ficha? = null

    private val binding by lazy {
        ActivityInformacoesDoPersonagemBinding.inflate(layoutInflater)
    }

    private val fichaDao by lazy {
        AppDataBase.instancia(this).fichaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Informoções do personagem"
        tentaCarregarFicha()
        configuraBotoesIncrementoDecremento()
        configuraPopUpEditar()

        if (fichaId != 0L) {
            lifecycleScope.launch {
                val ficha = fichaDao.buscaPorId(fichaId).firstOrNull()
                if (ficha != null) {
                    preencheCampos(ficha)
                    this@InformacoesDoPersonagem.ficha = ficha
                } else {
                    Log.e("InformacoesDoPersonagem", "Ficha não encontrada para o ID: $fichaId")
                    finish()
                }
            }
        } else {
            Log.e("InformacoesDoPersonagem", "ID da ficha inválido: $fichaId")
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_informacoes_classes_personagem, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_classe_remover -> {
                lifecycleScope.launch {
                    ficha?.let {
                        fichaDao.delete(it)
                        finish()
                    }
                }
            }

            R.id.menu_detalhes_classe_editar -> {
                Intent(this, FormularioPersonagem::class.java).apply {
                    putExtra(CHAVE_FICHA_ID, fichaId)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraPopUpEditar() {
        val actionType = intent.getStringExtra(ACTION_TYPE)
        if (actionType == "EDITAR" && fichaId != 0L) {
            lifecycleScope.launch {
                fichaDao.buscaPorId(fichaId)?.collect { Ficha ->
                    ficha?.let { preencheCampos(it) }
                }
            }
        }
    }

    private suspend fun buscaFicha(): Long {
        val ficha = fichaDao.buscaPorId(fichaId).firstOrNull()
        Log.d("InformacoesDoPersonagem", "Fetched ficha: $ficha")
        return ficha?.id ?: 0L
    }

    private fun tentaCarregarFicha() {
        lifecycleScope.launch {
            fichaId = intent.getLongExtra(CHAVE_FICHA_ID, 0L)
        }
    }

    private fun preencheCampos(ficha: Ficha) {
        with(binding) {
            personagemNome.text = "Nome: ${ficha.nome.toString()}"
            racaPersonagem.text = "Raça: ${ficha.raca.toString()}"
            generoPersonagem.text = "Classe: ${ficha.classe.toString()}"
            inputForca.text = ficha.forca.toString()
            inputDestreza.text = ficha.destreza.toString()
            inputCarisma.text = ficha.carisma.toString()
            inputSabedoria.text = ficha.sabedoria.toString()
            inputConstituicao.text = ficha.constituicao.toString()
            inputInteligencia.text = ficha.inteligencia.toString()
            ficha.imgClass?.let { imagemChar.setImageResource(it) }
        }
    }

    private fun configuraBotoesIncrementoDecremento() {
        configuraBotaoIncrementoDecremento(
            binding.inputForca,
            binding.btnIncrementForca,
            binding.btnDecrementForca
        )
        configuraBotaoIncrementoDecremento(
            binding.inputDestreza,
            binding.btnIncrementDestreza,
            binding.btnDecrementDestreza
        )
        configuraBotaoIncrementoDecremento(
            binding.inputConstituicao,
            binding.btnIncrementConstituicao,
            binding.btnDecrementConstituicao
        )
        configuraBotaoIncrementoDecremento(
            binding.inputInteligencia,
            binding.btnIncrementInteligencia,
            binding.btnDecrementInteligencia
        )
        configuraBotaoIncrementoDecremento(
            binding.inputSabedoria,
            binding.btnIncrementSabedoria,
            binding.btnDecrementSabedoria
        )
        configuraBotaoIncrementoDecremento(
            binding.inputCarisma,
            binding.btnIncrementCarisma,
            binding.btnDecrementCarisma
        )
    }

    private fun configuraBotaoIncrementoDecremento(
        editText: TextView,
        incrementButton: ImageButton,
        decrementButton: ImageButton
    ) {
        incrementButton.setOnClickListener {
            val currentValue = StringToInt(editText.text.toString()) ?: 0
            editText.setText((currentValue + 1).toString())
        }

        decrementButton.setOnClickListener {
            val currentValue = StringToInt(editText.text.toString()) ?: 0
            if (currentValue > 0) {
                editText.setText((currentValue - 1).toString())
            }
        }
    }

    private fun StringToInt(input: String): Int? {
        return try {
            input.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }
}
