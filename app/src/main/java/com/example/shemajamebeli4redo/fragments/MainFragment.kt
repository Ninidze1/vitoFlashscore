package com.example.shemajamebeli4redo.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shemajamebeli4redo.App
import com.example.shemajamebeli4redo.R
import com.example.shemajamebeli4redo.adapters.MainRecyclerAdapter
import com.example.shemajamebeli4redo.base.BaseFragment
import com.example.shemajamebeli4redo.databinding.MainFragmentBinding
import com.example.shemajamebeli4redo.extensions.changeColor
import com.example.shemajamebeli4redo.extensions.loadImg
import com.example.shemajamebeli4redo.extensions.showToast
import com.example.shemajamebeli4redo.models.Match
import com.example.shemajamebeli4redo.models.ResultHandle
import com.example.shemajamebeli4redo.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainFragment : BaseFragment<MainFragmentBinding, MainViewModel>(
    MainFragmentBinding::inflate,
    MainViewModel::class.java
) {

    private lateinit var adapter: MainRecyclerAdapter

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        viewModel.init()
        setInfo()
        mainRecyclerInit()

        binding?.firstFavourite?.setOnClickListener {
            firstToFavourites()
        }

        binding?.secondFavourite?.setOnClickListener {
            secondToFavourites()
        }

        binding?.overview?.setOnClickListener {
            binding!!.overview.changeColor(R.color.main_color)
            binding!!.statistic.changeColor(R.color.text_gray)
            binding!!.lineup.changeColor(R.color.text_gray)
        }

        binding!!.statistic.setOnClickListener {
            binding!!.statistic.changeColor(R.color.main_color)
            binding!!.overview.changeColor(R.color.text_gray)
            binding!!.lineup.changeColor(R.color.text_gray)
        }

        binding!!.lineup.setOnClickListener {
            binding!!.lineup.changeColor(R.color.main_color)
            binding!!.statistic.changeColor(R.color.text_gray)
            binding!!.overview.changeColor(R.color.text_gray)
        }
    }

    private fun setInfo() {
        viewModel._matchInfo.observe(viewLifecycleOwner, {
            when (it.status) {
                ResultHandle.Companion.Status.SUCCESS -> {
                    val model = it.data!!.match

                    binding!!.teamFirstImage.loadImg(model.team1.teamImage)
                    binding!!.teamSecondImage.loadImg(model.team2.teamImage)

                    binding!!.fieldTextView.text = model.stadiumAdress
                    binding!!.dateTextView.text = getDataTime(model.matchDate)
                    binding!!.firstTeamName.text = model.team1.teamName
                    binding!!.secondTeamName.text = model.team2.teamName
                    binding!!.duration.text = App.context.getString(R.string.time_format, model.matchTime.roundToInt().toString())

                    binding!!.scoreTextView.text = App.context.getString(R.string.result, model.team1.score, model.team2.score)

                    binding!!.firstPossesion.text = App.context.getString(R.string.possession, model.team1.ballPosition)
                    binding!!.secondPossesion.text = App.context.getString(R.string.possession, model.team2.ballPosition)

                    binding!!.progressBar.progress = model.team1.ballPosition!!
                    adapter.addActions(model.matchSummary.summaries.toMutableList())
                    firstHalfScores(binding!!.score, it)

                }
                ResultHandle.Companion.Status.ERROR -> {
                    requireActivity().showToast(it.error)
                }
            }
        })
    }

    private fun firstHalfScores(dashboard: TextView, matchInfo: ResultHandle<Match>) {
            val model = matchInfo.data!!.match
            var firstScores = 0
            var secondScores = 0

            // scored to opponents (first team)
            model.matchSummary.summaries.forEach { summaries ->
                if (summaries.actionTime.toInt() < 45) {
                    summaries.team1Action?.forEach { actions ->
                        if (actions.actionType == 1 && actions.action.goalType == 1) {
                            firstScores++
                        }
                    }
                }
            }

            // auto goals (by second team)
            model.matchSummary.summaries.forEach { summaries ->
                if (summaries.actionTime.toInt() < 45) {
                    summaries.team2Action?.forEach { actions ->
                        if (actions.actionType == 1 && actions.action.goalType != 1)
                            firstScores++
                    }
                }
            }

            // scored to opponents (second team)
            model.matchSummary.summaries.forEach { summaries ->
                if (summaries.actionTime.toInt() < 45) {
                    summaries.team2Action?.forEach { actions ->
                        if (actions.actionType == 1 && actions.action.goalType == 1) {
                            secondScores++
                        }
                    }
                }
            }

            // auto goals (by first team)
            model.matchSummary.summaries.forEach { summaries ->
                if (summaries.actionTime.toInt() < 45) {
                    summaries.team1Action?.forEach { actions ->
                        if (actions.actionType == 1 && actions.action.goalType != 1)
                            secondScores++
                    }
                }
            }
            dashboard.text = App.context.getString(R.string.first_half, firstScores, secondScores)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDataTime(date: Long): String {
        val form = SimpleDateFormat("dd MMMM yyyy")
        return form.format(Date(date))
    }

    private fun mainRecyclerInit() {
        adapter = MainRecyclerAdapter()
        binding!!.recylcer.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recylcer.adapter = adapter
    }

    private fun firstToFavourites() {
        binding!!.secondFavourite.setImageResource(R.drawable.ic_favourite)
        binding!!.firstFavourite.setImageResource(R.drawable.ic_favourites_selected)
    }

    private fun secondToFavourites() {
        binding!!.firstFavourite.setImageResource(R.drawable.ic_favourite)
        binding!!.secondFavourite.setImageResource(R.drawable.ic_favourites_selected)
    }

}