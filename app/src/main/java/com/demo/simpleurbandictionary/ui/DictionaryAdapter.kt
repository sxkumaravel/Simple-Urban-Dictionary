package com.demo.simpleurbandictionary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.simpleurbandictionary.databinding.DictionaryViewBinding
import com.demo.simpleurbandictionary.model.Search

/**
 * Adapter to bind the recycler view to show the list of dictionary search items.
 * Created on 2019-05-14.
 *
 * @author kumars
 */
class DictionaryAdapter : RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {

    private val items: MutableList<Search> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DictionaryViewBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /**
     * To bind the new set of data's to the adapter
     */
    fun updateAdapter(searchList: List<Search>?) {
        items.clear()
        searchList?.let {
            items.addAll(searchList)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: DictionaryViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}