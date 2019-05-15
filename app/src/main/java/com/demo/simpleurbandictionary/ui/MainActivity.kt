package com.demo.simpleurbandictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.simpleurbandictionary.R
import com.demo.simpleurbandictionary.databinding.ActivityMainBinding
import com.demo.simpleurbandictionary.util.DataSourceProvider

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    companion object {
        private val SORT_TYPE = arrayOf("Sort By", "Thumbs Up", "Thumbs Down")
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: DictionaryAdapter

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            DictionaryViewModel.FACTORY(DataSourceProvider.getDictionaryInstance())
        )
            .get(DictionaryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // spinner set up
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_view, SORT_TYPE)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = spinnerAdapter
        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        viewModel.sortDictionaryResults(DictionaryViewModel.Sort.THUMBS_UP)
                    }
                    2 -> {
                        viewModel.sortDictionaryResults(DictionaryViewModel.Sort.THUMBS_DOWN)
                    }
                    else -> {
                        viewModel.sortDictionaryResults(DictionaryViewModel.Sort.NONE)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // NOP
            }
        }

        // recycler view set up
        recyclerAdapter = DictionaryAdapter()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recyclerAdapter

        // search view
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
        binding.searchView.setOnQueryTextListener(this)

        // view models observables
        viewModel.isLoadingLiveData.observe(this, Observer {
            binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.errorEmptySearchLiveData.observe(this, Observer {
            Toast.makeText(this, getString(R.string.empty_error), Toast.LENGTH_LONG).show()
        })
        viewModel.searchResultLiveDate.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_results_found), Toast.LENGTH_LONG).show()
            } else {
                recyclerAdapter.updateAdapter(it)
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        recyclerAdapter.updateAdapter(null)
        query?.let { viewModel.searchDictionary(it) }
        binding.searchView.clearFocus()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}
