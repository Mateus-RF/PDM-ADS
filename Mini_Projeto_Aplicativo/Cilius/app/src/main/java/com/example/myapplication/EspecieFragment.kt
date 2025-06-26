package com.example.myapplication.ui.especie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.AppDatabase
import com.example.myapplication.ArvoreAdapter
import com.example.myapplication.DetalheArvoreActivity
import com.example.myapplication.databinding.FragmentEspecieBinding
import kotlinx.coroutines.launch

class EspecieFragment : Fragment() {

    private var _binding: FragmentEspecieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEspecieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewEspecies.layoutManager = GridLayoutManager(requireContext(), 2)

        lifecycleScope.launch {
            val arvoreDao = AppDatabase.getDatabase(requireContext()).arvoreDao()
            val lista = arvoreDao.listarTodas()

            val adapter = ArvoreAdapter(lista) { arvoreSelecionada ->
                val intent = Intent(requireContext(), DetalheArvoreActivity::class.java)
                intent.putExtra("id", arvoreSelecionada.id)
                intent.putExtra("especie", arvoreSelecionada.especie)
                intent.putExtra("data", arvoreSelecionada.dataRegistro)
                intent.putExtra("condicao", arvoreSelecionada.estadoConservacao)
                intent.putExtra("latitude", arvoreSelecionada.latitude)
                intent.putExtra("longitude", arvoreSelecionada.longitude)
                intent.putExtra("imagem", arvoreSelecionada.imagemUri)
                startActivity(intent)
            }
            binding.recyclerViewEspecies.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
