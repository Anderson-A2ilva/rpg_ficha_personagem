package com.example.mvvm_rpg_ficha.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_rpg_ficha.R
import com.example.mvvm_rpg_ficha.databinding.UiCardRecyclerViewBinding
import com.example.mvvm_rpg_ficha.model.Ficha

class RecyclerViewAdapter(
    private val context: Context,
    private val fichas: List<Ficha> = listOf(),
    var clickCharInformacoes: (ficha: Ficha) -> Unit = {},
    var quandoClicarNoItem: (ficha: Ficha) -> Unit = {},
    var quandoClicarEmEditar: (ficha: Ficha) -> Unit = {},
    var quandoClicarEmRemover: (ficha: Ficha) -> Unit = {}
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val fichasList = fichas.toMutableList()

    inner class ViewHolder(private val binding: UiCardRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {
        private lateinit var ficha: Ficha


        init {
            itemView.setOnClickListener {
                if (::ficha.isInitialized) {
                    clickCharInformacoes(ficha)
                }
            }
        }

        fun vincula(ficha: Ficha) {
            this.ficha = ficha
            val nome = binding.cardNomePersonagem
            nome.text = ficha.nome
            val raca = binding.cardRacaPersonagem
            raca.text = ficha.raca
            val classe = binding.cardClassePersonagem
            classe.text = ficha.classe
            val imagemChar = binding.cardIconPersonagem
            ficha.imgClass?.let { imagemChar.setImageResource(it) }
            //val genero =  não esquecer de implementar

            itemView.setOnLongClickListener {
                PopupMenu(context, itemView).apply {
                    menuInflater.inflate(
                        R.menu.menu_informacoes_classes_personagem,
                        menu
                    )
                    setOnMenuItemClickListener(this@ViewHolder)
                }.show()
                true
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_detalhes_classe_editar -> {
                        quandoClicarEmEditar(ficha)
                    }

                    R.id.menu_detalhes_classe_remover -> {
                        quandoClicarEmRemover(ficha)
                    }
                }
            }
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UiCardRecyclerViewBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(fichasList[position])
    }

    override fun getItemCount(): Int = fichasList.size

    fun atualiza(ficha: List<Ficha>) {
        fichasList.clear()
        fichasList.addAll(ficha)
        notifyDataSetChanged()
    }

}




