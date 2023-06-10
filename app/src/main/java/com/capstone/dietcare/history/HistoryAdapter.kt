package com.capstone.dietcare.history

import android.content.Intent
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dietcare.data.helper.DateHelper.withDateFormat
import com.capstone.dietcare.data.user.DataUser
import com.capstone.dietcare.databinding.ItemHistoryBinding
import com.capstone.dietcare.detail.DetailActivity

class HistoryAdapter(private val listHistory: List<DataUser>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder (private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (history: DataUser){
            val date = history.date?.removeRange(10,19)
            val hour = history.date?.removeRange(0,11)?.removeRange(5,8)
            binding.tvDateHistory.text = date?.withDateFormat()
            binding.tvTotalCalHistory.text = ""
            binding.tvTimeHistory.text = hour
            if (history.m1name != null){
                binding.tvNotChoose.visibility = View.GONE
                binding.cvItem1History.visibility = View.VISIBLE
                binding.tvTitleItem1History.text = history.m1name
                binding.tvCtp1History.text = "${history.m1cal} kcal • ${history.m1time} • ${history.m1port?.toInt()} Portion"
                binding.tvTotalCalHistory.text = " - Total calories : ${history.m1cal?.toInt()} Kcal"
                Glide.with(binding.ivItem1History).load(history.m1img).into(binding.ivItem1History)
                if (history.m2name != null){
                    binding.cvItem1History.visibility = View.VISIBLE
                    binding.cvItem2History.visibility = View.VISIBLE
                    binding.tvTitleItem2History.text = history.m2name
                    binding.tvCtp2History.text = "${history.m2cal} kcal • ${history.m2time} • ${history.m2port?.toInt()} Portion"
                    binding.tvTotalCalHistory.text = " - Total calories : ${history.m1cal?.plus(history.m2cal!!)?.toInt()} Kcal"
                    Glide.with(binding.ivItem2History).load(history.m2img).into(binding.ivItem2History)
                    if (history.m3name != null){
                        binding.cvItem1History.visibility = View.VISIBLE
                        binding.cvItem2History.visibility = View.VISIBLE
                        binding.cvItem3History.visibility = View.VISIBLE
                        binding.tvTitleItem3History.text = history.m3name
                        binding.tvCtp3History.text = "${history.m3cal} kcal • ${history.m3time} • ${history.m3port?.toInt()} Portion"
                        binding.tvTotalCalHistory.text = " - Total calories : ${history.m1cal?.plus(history.m2cal!!)?.plus(history.m3cal!!)?.toInt()} Kcal"
                        Glide.with(binding.ivItem3History).load(history.m3img).into(binding.ivItem3History)
                        if (history.m4name != null){
                            binding.cvItem1History.visibility = View.VISIBLE
                            binding.cvItem2History.visibility = View.VISIBLE
                            binding.cvItem3History.visibility = View.VISIBLE
                            binding.cvItem4History.visibility = View.VISIBLE
                            binding.tvTitleItem4History.text = history.m4name
                            binding.tvCtp4History.text = "${history.m4cal} kcal • ${history.m4time} • ${history.m4port?.toInt()} Portion"
                            binding.tvTotalCalHistory.text = " - Total calories : ${history.m1cal?.plus(history.m2cal!!)?.plus(history.m3cal!!)?.plus(history.m4cal!!)?.toInt()} Kcal"
                            Glide.with(binding.ivItem4History).load(history.m4img).into(binding.ivItem4History)
                            if (history.m5name != null){
                                binding.cvItem1History.visibility = View.VISIBLE
                                binding.cvItem2History.visibility = View.VISIBLE
                                binding.cvItem3History.visibility = View.VISIBLE
                                binding.cvItem4History.visibility = View.VISIBLE
                                binding.cvItem5History.visibility = View.VISIBLE
                                binding.tvTitleItem5History.text = history.m5name
                                binding.tvCtp5History.text = "${history.m5cal} kcal • ${history.m5time} • ${history.m5port?.toInt()} Portion"
                                binding.tvTotalCalHistory.text = " - Total calories : ${history.m1cal?.plus(history.m2cal!!)?.plus(history.m3cal!!)?.plus(history.m4cal!!)?.plus(history.m5cal!!)?.toInt()} Kcal"
                                Glide.with(binding.ivItem5History).load(history.m5img).into(binding.ivItem5History)
                            }else{
                                binding.cvItem5History.visibility = View.GONE
                            }
                        }else{
                            binding.cvItem4History.visibility = View.GONE
                            binding.cvItem5History.visibility = View.GONE
                        }
                    }else{
                        binding.cvItem3History.visibility = View.GONE
                        binding.cvItem4History.visibility = View.GONE
                        binding.cvItem5History.visibility = View.GONE
                    }
                }else{
                    binding.cvItem2History.visibility = View.GONE
                    binding.cvItem3History.visibility = View.GONE
                    binding.cvItem4History.visibility = View.GONE
                    binding.cvItem5History.visibility = View.GONE
                }
            } else{
                binding.tvNotChoose.visibility = View.VISIBLE
                binding.cvItem1History.visibility = View.GONE
                binding.cvItem2History.visibility = View.GONE
                binding.cvItem3History.visibility = View.GONE
                binding.cvItem4History.visibility = View.GONE
                binding.cvItem5History.visibility = View.GONE
                binding.tvTotalCalHistory.text = " - Total calories : 0 Kcal"
            }

            binding.cvItem1History.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("RECIPENAME", history.m1name)
                itemView.context.startActivity(intent)
            }
            binding.cvItem2History.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("RECIPENAME", history.m2name)
                itemView.context.startActivity(intent)
            }
            binding.cvItem3History.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("RECIPENAME", history.m3name)
                itemView.context.startActivity(intent)
            }
            binding.cvItem4History.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("RECIPENAME", history.m4name)
                itemView.context.startActivity(intent)
            }
            binding.cvItem5History.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("RECIPENAME", history.m5name)
                itemView.context.startActivity(intent)
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = listHistory.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }
}