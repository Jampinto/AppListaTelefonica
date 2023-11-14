package com.example.listatelefonica.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listatelefonica.R
import com.example.listatelefonica.adapter.ContactoListAdapter
import com.example.listatelefonica.adapter.listener.ContactoOnClickListener
import com.example.listatelefonica.databinding.ActivityMainBinding
import com.example.listatelefonica.model.Contacto
import com.example.listatelefonica.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var adapter: ContactoListAdapter
    private var listaContactos: List<Contacto> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.reclycerViewContactos.layoutManager = LinearLayoutManager(this)

        viewModel.getListaContacto()

        observe()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                viewModel.getListaContacto()
            }
        }

        binding.buttonNovoContacto.setOnClickListener {
            launcher.launch(Intent(applicationContext, NovoContactoActivity::class.java))
        }

    }

    private fun observe() {
        viewModel.listaContacto().observe(this, Observer {
            listaContactos = it
            placeAdapter()
        })
    }

    private fun placeAdapter() {
        adapter = ContactoListAdapter(listaContactos, ContactoOnClickListener {
            val i = Intent(this, DetalhesContactoActivity::class.java)
            i.putExtra("id", it.id)
            launcher.launch(i)
        })
        binding.reclycerViewContactos.adapter = adapter
    }
}
