package com.marsu.animelist.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marsu.animelist.R
import com.marsu.animelist.adapter.SearchListAdapter
import com.marsu.animelist.databinding.FragmentSearchBinding
import com.marsu.animelist.model.Entry
import com.marsu.animelist.network.EntryApi
import com.marsu.animelist.viewmodel.SearchViewModel
import com.marsu.animelist.viewmodel.SearchViewModelFactory

private lateinit var binding: FragmentSearchBinding
private lateinit var adapter: SearchListAdapter

class SearchFragment : Fragment() {

    private val sSearchViewModel : SearchViewModel by viewModels {
        SearchViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        val resultObserver = Observer<List<Entry>> { entries ->
            adapter.setData(entries)
        }

        sSearchViewModel.observableEntries.observe(viewLifecycleOwner, resultObserver)
        adapter = SearchListAdapter()
        val recyclerView = binding.searchListRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)

        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.
        SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    sSearchViewModel.getSearchResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

}