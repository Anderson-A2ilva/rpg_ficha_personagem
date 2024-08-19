package com.example.mvvm_rpg_ficha.ui.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mvvm_rpg_ficha.R
import com.example.mvvm_rpg_ficha.dataBase.AppDataBase
import com.example.mvvm_rpg_ficha.databinding.ActivityFormularioPersonagemBinding
import com.example.mvvm_rpg_ficha.databinding.DialogClassesFomularioBinding
import com.example.mvvm_rpg_ficha.model.Ficha
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormularioPersonagem : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var idFicha = 0L

    private val binding by lazy {
        ActivityFormularioPersonagemBinding.inflate(layoutInflater)
    }

    private val fichaDao by lazy {
        val db = AppDataBase.instancia(this)
        db.fichaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Criação da ficha"
        configuraBotaoSalvar()
        configuraAlertDialogFormulario()
        configuraBotoesIncrementoDecremento()
        editaFicha()
    }

    override fun onResume() {
        super.onResume()
        tentaBuscarFicha()
    }

    private fun editaFicha() {
        scope.launch {
            idFicha = intent.getLongExtra(CHAVE_FICHA_ID, 0L)
            if (idFicha != 0L) {
                fichaDao.buscaPorId(idFicha)?.collect{ficha ->
                    withContext(Dispatchers.Main) {
                        ficha?.let { preencheFicha(it) }
                    }
                }
            }
        }
    }

    private fun tentaBuscarFicha() {
        scope.launch {
            fichaDao.buscaPorId(idFicha)?.collect {ficha ->
                withContext(Dispatchers.Main) {
                    ficha?.let { preencheFicha(it) }
                }
            }
        }
    }

    private fun preencheFicha(ficha: Ficha) {
        if (supportActionBar != null) {
            supportActionBar?.title = if (idFicha > 0) "Edição da ficha" else "Criação da ficha"
        }
        with(binding) {
            cardNomePersonagemFormulario.setText(ficha.nome)
            cardClassePersonagemFormulario.setText(ficha.classe)
            cardRacaPersonagemFormulario.setText(ficha.raca)
            ficha.imgClass?.let {
                cardImagemPersonagemFormulario.setImageResource(
                    it
                )
            }
            ficha.forca?.let { inputForca.setText(it.toString()) }
            ficha.destreza?.let { inputDestreza.setText(it.toString()) }
            ficha.constituicao?.let { inputConstituicao.setText(it.toString()) }
            ficha.inteligencia?.let { inputInteligencia.setText(it.toString()) }
            ficha.sabedoria?.let { inputSabedoria.setText(it.toString()) }
            ficha.carisma?.let { inputCarisma.setText(it.toString()) }
        }
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.formularioBotaoSalvar

        botaoSalvar.setOnClickListener {
            val fichaNova = criarFicha()
            lifecycleScope.launch {
                fichaDao.salva(fichaNova)
                finish()
            }
        }
    }

    fun StringToInt(input: String): Int? {
        return try {
            input.toInt()
        } catch (e: NumberFormatException) {
            null
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
        editText: EditText,
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

    private fun configuraAlertDialogFormulario() {
        binding.cardClassePersonagemFormulario.setOnClickListener {
            val classeFormularioBinding = DialogClassesFomularioBinding.inflate(layoutInflater)
            AlertDialog.Builder(this)
                .setView(classeFormularioBinding.root)
                .setPositiveButton("confirmar") { _, _ ->
                    val selectedId = classeFormularioBinding.radioGroupClass.checkedRadioButtonId
                    if (selectedId != -1) {
                        val selectedRadioButton =
                            classeFormularioBinding.root.findViewById<RadioButton>(selectedId)
                        val selectedClass = selectedRadioButton.text.toString()
                        binding.cardClassePersonagemFormulario.setText(selectedClass)

                        val imagemClasse = configuraClasseImagem(selectedClass)
                        binding.cardImagemPersonagemFormulario.setImageResource(imagemClasse)
                    }
                }
                .setNegativeButton("cancelar")
                { _, _ -> }
                .show()
        }
    }

    private fun configuraClasseImagem(classe: String): Int {
        return when (classe) {
            "Guerreiro" -> R.drawable.guerreiro
            "Mago" -> R.drawable.mago
            "Ladino" -> R.drawable.ladino
            "Bardo" -> R.drawable.bardo
            "Bruxo" -> R.drawable.bruxo
            "Clerigo" -> R.drawable.clerigo
            "Monge" -> R.drawable.monge
            "Paladino" -> R.drawable.paladino
            "Caçador" -> R.drawable.cacadora
            else -> R.drawable.default_imagem
        }

    }

    private fun criarFicha(): Ficha {
        val nomePersonagem = binding.cardNomePersonagemFormulario.text.toString()
        val classePersonagem = binding.cardClassePersonagemFormulario.text.toString()
        val racaPersonagem = binding.cardRacaPersonagemFormulario.text.toString()
        val forca = StringToInt(binding.inputForca.text.toString()) ?: 0
        val destreza = StringToInt(binding.inputDestreza.text.toString()) ?: 0
        val contituicao = StringToInt(binding.inputConstituicao.text.toString()) ?: 0
        val inteligencia = StringToInt(binding.inputInteligencia.text.toString()) ?: 0
        val sabedoria = StringToInt(binding.inputSabedoria.text.toString()) ?: 0
        val carisma = StringToInt(binding.inputCarisma.text.toString()) ?: 0

        val imgemClass = configuraClasseImagem(classePersonagem)

        val fichaNova = Ficha(
            idFicha,
            nome = nomePersonagem,
            classe = classePersonagem,
            raca = racaPersonagem,
            forca = forca,
            destreza = destreza,
            constituicao = contituicao,
            inteligencia = inteligencia,
            sabedoria = sabedoria,
            carisma = carisma,
            imgClass = imgemClass
        )
        return fichaNova
    }

}