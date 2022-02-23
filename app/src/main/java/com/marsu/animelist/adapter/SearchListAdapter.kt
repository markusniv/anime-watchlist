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
                // Load poster image
                Picasso.get().load(this.image).into(binding.singleSearchEntryImage)
                // Load title, if title longer than 15 characters cut it
                var title = this.title
                if (this.title.length > 15) title = "${this.title.take(15)}..."
                binding.singleSearchEntryTitle.text = title
                // Load type
                binding.singleSearchEntryType.text = this.type
                // Display year and season if TV, otherwise release date
                var airedText = "Started airing: "
                var airedDate = if (this.season != null) "${this.season} / ${this.year}"
                else "Unknown"
                if (this.type != "TV") {
                    airedText = "Released: "
                    airedDate = if (this.airedFrom != null) this.airedFrom.split("T")[0]
                    else "Unknown"
                }
                binding.singleSearchEntryAiredText.text = airedText
                binding.singleSearchEntryAired.text = airedDate
                // Load score if available
                if (this.score != null) binding.singleSearchEntryScore.text = this.score.toString()
                else binding.singleSearchEntryScore.text = "N/A"
            }
        }
    }

    override fun getItemCount(): Int = entryList.size

    fun setData(entry: List<Entry>) {
        this.entryList = entry
        notifyDataSetChanged()
    }

}