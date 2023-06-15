package com.capstone.dietcare.ui.main.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ItemSearchBinding
import com.capstone.dietcare.ui.detail.DetailActivity
import com.capstone.dietcare.ui.detail.DetailParcel

class SearchAdapter(private val listSearch : List<DataItem>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder (private val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data : DataItem){
            val image = data.images?.removePrefix("\"")?.removeSuffix("\"")?.replaceAfter(".jpg", "" )?.replaceAfter(".JPG", "" )?.replaceAfter(".jpeg", "" )?.replaceAfter(".JPEG", "" )?.replaceAfter(".png", "" )?.replaceAfter(".PNG", "" )
            binding.tvTitleSearch.text = data.name
            binding.tvCtpSearch.text = "${data.calories} kcal • ${data.totalTime?.removePrefix("PT")} • ${data.recipeServings} Portion"
            Glide.with(binding.ivItemSearch).load(image).into(binding.ivItemSearch)
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount() = listSearch.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(listSearch[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            val recipeIntent = DetailParcel(
                listSearch[position].name!!,
                listSearch[position].calories.toString().toDouble()
            )
            intent.putExtra("RECIPE_INTENT", recipeIntent)
            holder.itemView.context.startActivity(intent)
        }
    }
}