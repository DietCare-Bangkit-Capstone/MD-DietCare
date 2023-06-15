package com.capstone.dietcare.ui.recommendation

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dietcare.data.remote.ml.DataItem
import com.capstone.dietcare.databinding.ItemRecommendationRecipeBinding

class Rec5PagerAdapter(private val recItem : List<DataItem>): RecyclerView.Adapter<Rec5PagerAdapter.Rec5ViewHolder>() {
    class Rec5ViewHolder (private val binding: ItemRecommendationRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (data : DataItem){
            val image = data.images?.removePrefix("\"")?.removeSuffix("\"")?.replaceAfter(".jpg", "" )?.replaceAfter(".JPG", "" )?.replaceAfter(".jpeg", "" )?.replaceAfter(".JPEG", "" )?.replaceAfter(".png", "" )?.replaceAfter(".PNG", "" )
            binding.tvItemRecipeTitle.text = data.name
            binding.tvCPFItemRecipe.text = "Carbohydrate: ${data.carbohydrateContent}g • Protein: ${data.proteinContent}g • Fat: ${data.fatContent}g"
            binding.tvCaloriesItemRecipe.text = "${data.calories} kcal"
            binding.tvTimeItemRecipe.text = data.totalTime?.removePrefix("PT")?.replace("H", " hr ")?.replace("M", " min")
            binding.tvPortionItemRecipe.text = data.recipeServings.toString().removeSuffix(".0") + " portion"
            Glide.with(binding.ivItemRecipe).load(image).into(binding.ivItemRecipe)

            val ingredientArray = data.recipeIngredientParts?.removePrefix("\"")?.removeSuffix("\"")?.split("\", \"")?.toTypedArray()
            if (ingredientArray != null) {
                var tidyIngridient = ""
                for (part : String in ingredientArray){
                    tidyIngridient = tidyIngridient + "• ${part}\n"
                }
                binding.tvIngridientsItemRecipe.text = tidyIngridient
            }

            val stepArray = data.recipeInstructions?.removeSuffix(")")?.removePrefix("\"")?.removeSuffix("\"")?.removeSuffix("\"\n")?.replace("\", \n\"", "\", \"")?.split("\", \"")?.toTypedArray()
            if (stepArray != null){
                var tidyStep = ""
                for (i in 1..stepArray.size){
                    tidyStep = tidyStep + "${i}. ${stepArray[i-1]}\n"
                }
                binding.tvStepsItemRecipe.text = tidyStep
            }

            binding.btnEat.setOnClickListener {
                val recom = RecIntent(
                    data.name,
                    image,
                    data.calories.toString().toDouble(),
                    data.totalTime?.removePrefix("PT"),
                    data.recipeServings.toString().toDouble()
                )
                val recIntent = Intent(itemView.context, RecommendationActivity::class.java)
                recIntent.putExtra("RECINTENT", recom)
                itemView.context.startActivity(recIntent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Rec5ViewHolder {
        val binding = ItemRecommendationRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Rec5ViewHolder(binding)
    }

    override fun getItemCount()=recItem.size

    override fun onBindViewHolder(holder: Rec5ViewHolder, position: Int) {
        holder.bind(recItem[position])
    }
}