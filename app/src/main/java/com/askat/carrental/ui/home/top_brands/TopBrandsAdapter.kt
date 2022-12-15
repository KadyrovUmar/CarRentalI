package com.askat.carrental.ui.home.top_brands

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.askat.carrental.databinding.RvBrandBinding

class TopBrandsAdapter(
    private val onClickTopBrands: OnClick
) : RecyclerView.Adapter<TopBrandsAdapter.TopBrandsViewHolder>() {

    private val list: ArrayList<Int> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun addBrand(list: Int) {
        this.list.add(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBrandsViewHolder {
        return TopBrandsViewHolder(
            RvBrandBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: TopBrandsViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.itemView.setOnClickListener {
            onClickTopBrands.onClickTopBrands(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class TopBrandsViewHolder(private val binding: RvBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun onBind(model : Int) {
                binding.ImgBrand.setImageResource(model)
            }
    }

    interface OnClick {
        fun onClickTopBrands(pos: Int)
    }
}