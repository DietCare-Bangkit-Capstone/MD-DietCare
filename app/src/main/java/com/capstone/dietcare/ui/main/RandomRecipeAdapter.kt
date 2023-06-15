package com.capstone.dietcare.ui.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ItemHomeRecipeBinding
import com.capstone.dietcare.ui.detail.DetailActivity
import com.capstone.dietcare.ui.detail.DetailParcel

class RandomRecipeAdapter (private val listRandom : List<DataItem>) : RecyclerView.Adapter<RandomRecipeAdapter.RandomRecipeViewHolder>() {
    class RandomRecipeViewHolder (private val binding: ItemHomeRecipeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (data : DataItem){
            val image = data.images?.removePrefix("\"")?.removeSuffix("\"")?.replaceAfter(".jpg", "" )?.replaceAfter(".JPG", "" )?.replaceAfter(".jpeg", "" )?.replaceAfter(".JPEG", "" )?.replaceAfter(".png", "" )?.replaceAfter(".PNG", "" )
            binding.tvTitleRandomRecipe.text = data.name
            Glide.with(binding.ivRandomRecipe).load(image).into(binding.ivRandomRecipe)
            binding.tvCalRandomRecipe.text =  "${data.calories} kcal"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomRecipeViewHolder {
        val binding = ItemHomeRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RandomRecipeViewHolder(binding)
    }

    override fun getItemCount() = listRandom.size

    override fun onBindViewHolder(holder: RandomRecipeViewHolder, position: Int) {
        holder.bind(listRandom[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            val recipeIntent = DetailParcel(
                listRandom[position].name!!,
                listRandom[position].calories.toString().toDouble()
            )
            intent.putExtra("RECIPE_INTENT", recipeIntent)
            holder.itemView.context.startActivity(intent)
        }
    }

}