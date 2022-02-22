package com.marsu.animelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marsu.animelist.databinding.SearchViewBinding
import com.marsu.animelist.model.Entry
import com.squareup.picasso.Picasso

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    private var entryList = emptyList<Entry>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = SearchViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: SearchViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(entryList[position]) {
                Picasso.get().load(this.image).into(binding.singleSearchEntryImage)
                binding.singleSearchEntryTitle.text = this.title
                if (this.airing) {
                    binding.singleSearchEntryAiring.text = "Yes"
                } else {
                    binding.singleSearchEntryAiring.text = "No"
                }
            }
        }
    }

    override fun getItemCount(): Int = entryList.size

    fun setData(entry: List<Entry>) {
        this.entryList = entry
        notifyDataSetChanged()
    }

}