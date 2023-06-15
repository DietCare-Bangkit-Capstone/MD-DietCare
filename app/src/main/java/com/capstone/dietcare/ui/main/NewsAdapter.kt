package com.capstone.dietcare.ui.main

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dietcare.data.helper.DateHelper.withNewsDateFormat
import com.capstone.dietcare.data.remote.news.DataItem
import com.capstone.dietcare.databinding.ItemNewsBinding
import com.capstone.dietcare.ui.detail.DetailActivity
import com.capstone.dietcare.ui.main.webview.WebviewActivity

class NewsAdapter(private val itemNews : List<DataItem>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder (private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (news: DataItem){
            binding.tvNewsTitle.text = news.title?.trim()
            binding.tvNewsDate.text = news.isoDate?.replaceAfter('T', "")?.removeSuffix("T")?.withNewsDateFormat() + " at " + news.isoDate?.replaceBefore("T", "")?.removePrefix("T")?.removeSuffix(".000Z")?.removeRange(5,8)
            Glide.with(binding.ivNewsItem).load(news.image?.small).into(binding.ivNewsItem)
            binding.icShareNews.setOnClickListener{
                val sharedText = "[${news.title}]\n${news.contentSnippet}\n\n${news.link}"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,sharedText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                itemView.context.startActivity(shareIntent)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WebviewActivity::class.java)
                intent.putExtra("NEWS_LINK", news.link)
                itemView.context.startActivity(intent)
            }
            binding.icBrowseNews.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(news.link)
                itemView.context.startActivity(intent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int = itemNews.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(itemNews[position])
    }
}