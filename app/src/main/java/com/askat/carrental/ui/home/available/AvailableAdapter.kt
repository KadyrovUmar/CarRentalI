package com.askat.carrental.ui.home.available

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.askat.carrental.databinding.RvCarsBinding

class AvailableAdapter(
    private val onClickAvailable: OnClick
) : RecyclerView.Adapter<AvailableAdapter.AvailableViewHolder>() {

    private val list: ArrayList<Int> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list : Int) {
        this.list.add(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableViewHolder {
        return AvailableViewHolder(
            RvCarsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AvailableViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.itemView.setOnClickListener {
            onClickAvailable.onClickAvailable(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class AvailableViewHolder(private val binding : RvCarsBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun onBind(model: Int) {
                binding.imgCar.setImageResource(model)
            }
    }

    interface OnClick {
        fun onClickAvailable(pos: Int)
    }

}