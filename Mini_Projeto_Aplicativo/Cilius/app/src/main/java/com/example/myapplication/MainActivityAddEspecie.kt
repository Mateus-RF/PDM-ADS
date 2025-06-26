package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityMainAddEspecieBinding
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.io.File

class MainActivityAddEspecie : AppCompatActivity() {

    private lateinit var binding: ActivityMainAddEspecieBinding
    private var currentPhotoUri: Uri? = null
    private val CAMERA_REQUEST = 1001


    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAddEspecieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* ---------- 1. Spinner de condição ---------- */
        ArrayAdapter.createFromResource(
            this,
            R.array.condicao_array,
            R.layout.spinner_item
        ).also { ad ->
            ad.setDropDownViewResource(R.layout.spinner_item)
            binding.condicaoSpinner.adapter = ad
        }

        /* ---------- 2. DatePicker ---------- */
        binding.dataRegistro.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    cal.set(y, m, d)
                    binding.dataRegistro.setText(sdf.format(cal.time))
                },
                cal[Calendar.YEAR],
                cal[Calendar.MONTH],
                cal[Calendar.DAY_OF_MONTH]
            ).show()
        }

        /* ---------- 3. Se veio para editar ---------- */
        val id = intent.getIntExtra("id", -1)
        if (id != -1) { // Se o id for diferente de -1 nos dados recebidos do Intent vai jogar as informcoes recebidas nos campos da tela de adicionar Especie
            // popula campos
            binding.especiePlanta.setText(intent.getStringExtra("especie"))
            binding.dataRegistro.setText(intent.getStringExtra("data"))
            binding.latitude.setText(intent.getFloatExtra("latitude", 0f).toString())
            binding.longitude.setText(intent.getFloatExtra("longitude", 0f).toString())
            val imageUri = intent.getStringExtra("image")
            currentPhotoUri = imageUri?.let { Uri.parse(it) }

            val imageView = findViewById<ImageView>(R.id.imageView2)

            if (!imageUri.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.nature_icon) // imagem padrão temporária
                    .error(R.drawable.nature_icon)       // se falhar, usa essa
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.nature_icon)
            }
            // seleciona item do spinner pelo texto recebido
            val cond = intent.getStringExtra("condicao") ?: ""
            val idx = (0 until binding.condicaoSpinner.count).firstOrNull {
                binding.condicaoSpinner.getItemAtPosition(it).toString() == cond
            } ?: 0
            binding.condicaoSpinner.setSelection(idx)
            binding.cadastrarEspecie.text = "Salvar alterações"
        }

        /* ---------- 4. Botão salvar / atualizar ---------- */
        binding.cadastrarEspecie.setOnClickListener {

            // 4.1 — leitura correta dos campos
            val especieTxt   = binding.especiePlanta.text.toString().trim()
            val dataTxt      = binding.dataRegistro.text.toString().trim()
            val latTxt       = binding.latitude.text.toString().trim()
            val lonTxt       = binding.longitude.text.toString().trim()
            val condicaoTxt  = binding.condicaoSpinner.selectedItem.toString()
            val fotoFinal = currentPhotoUri?.toString()

            if (especieTxt.isEmpty() || dataTxt.isEmpty() || latTxt.isEmpty() || lonTxt.isEmpty()) {
                toast("Todos os campos devem ser preenchidos!")
                return@setOnClickListener
            }
            // 4.2 — valida latitude
            val lat = latTxt.toFloatOrNull()
            if (lat == null || lat !in -33.75..5.27) {
                toast("Latitude inválida ou fora da área do Brasil!")
                return@setOnClickListener
            }
            // 4.3 — valida longitude
            val lon = lonTxt.toFloatOrNull()
            if (lon == null || lon !in -73.99..-34.79) {
                toast("Longitude inválida ou fora da área do Brasil!")
                return@setOnClickListener
            }
            try {
                val db = AppDatabase.getDatabase(this)
                val arvoreDao = db.arvoreDao()

                lifecycleScope.launch {
                    val arvore = Arvore(
                        id = if (id != -1) id else 0,
                        especie = especieTxt,
                        dataRegistro = dataTxt,
                        estadoConservacao = condicaoTxt,
                        latitude = lat,
                        longitude = lon,
                        imagemUri = fotoFinal
                    )
                    if (id != -1) { // Se o id for diferente de -1 nos dados recebidos do Intent vai Atualizar a planta especifica
                        arvoreDao.atualizar(arvore)
                        Toast.makeText(this@MainActivityAddEspecie, "Espécie atualizada!", Toast.LENGTH_SHORT).show()
                    } else {
                        arvoreDao.inserir(arvore)
                        Toast.makeText(this@MainActivityAddEspecie, "Espécie cadastrada!", Toast.LENGTH_SHORT).show()
                    }

                    finish()
                }
                startActivity(Intent(this, MainHomeActivity::class.java))
                finish()
            } catch (e: Exception) {
                toast("Erro: ${e.message}")
            }
        }
        /* ---------- 5. Botão para abrir a Camera e tirar Foto  ---------- */
        binding.button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_REQUEST)
            } else {
                abrirCamera()
            }
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    // Função para abrir a camera
    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = File.createTempFile("IMG_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        currentPhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
        startActivityForResult(intent, CAMERA_REQUEST)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            currentPhotoUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.nature_icon)
                    .into(binding.imageView2)
            }
        } else {
            toast("Foto não capturada.")
            currentPhotoUri = null
        }
    }
    // Função para pedir permissão ao usuario para ativar a camera
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera()
            } else {
                toast("Permissão da câmera negada.")
            }
        }
    }
}
