package com.example.shemajamebeli4redo.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shemajamebeli4redo.R
import com.example.shemajamebeli4redo.databinding.BlankItemLayoutBinding
import com.example.shemajamebeli4redo.databinding.FirstTeamItemBinding
import com.example.shemajamebeli4redo.extensions.loadImg
import com.example.shemajamebeli4redo.models.TeamAction

class FirstTeamRecycler(
    private val summaries: MutableList<TeamAction>,
    private val actionTime: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(private val binding: FirstTeamItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind() {
            val model = summaries[adapterPosition]
            binding.playerImage.loadImg(model.action.player?.playerImage.toString())
            binding.name.text = model.action.player?.playerName


            when(model.actionType) {
                1 -> {
                    if (model.action.goalType == 1) {
                        binding.actionType.text = "$actionTime' Goal by"
                        binding.event3.setImageResource(R.drawable.goal)
                    } else {
                        binding.actionType.text = "$actionTime' Self goal by"
                        binding.event3.setImageResource(R.drawable.selfgoal)
                    }
                }
                2 -> {
                    binding.actionType.text = "$actionTime' Tripping"
                    binding.event3.setImageResource(R.drawable.yellocard)

                }
                3 -> {
                    binding.actionType.text = "$actionTime' Red card to"
                    binding.actionType.setTextColor(Color.RED)
                    binding.event3.setImageResource(R.drawable.group_91)
                }
                4 -> binding.actionType.text = "$actionTime' Substitution"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            FirstTeamItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )



    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = summaries.size




}