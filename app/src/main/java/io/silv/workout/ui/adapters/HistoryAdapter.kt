package io.silv.workout.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.silv.workout.databinding.HistoryRowBinding
import io.silv.workout.local.ExerciseEntity

class HistoryAdapter(
    private val deleteListener:(id: Int) -> Unit,
): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: HistoryRowBinding): RecyclerView.ViewHolder(binding.root){
        val llMain = binding.llMain
        val tvDate = binding.tvDate
        val tvNumber = binding.tvNumber
        val ivDelete = binding.ivDelete
    }

    private val callback = object : DiffUtil.ItemCallback<ExerciseEntity>() {
        override fun areItemsTheSame(oldItem: ExerciseEntity, newItem: ExerciseEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExerciseEntity, newItem: ExerciseEntity): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.tvDate.text = item.date
        holder.tvNumber.text = "$position"

        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.llMain.setBackgroundColor(Color.WHITE)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}