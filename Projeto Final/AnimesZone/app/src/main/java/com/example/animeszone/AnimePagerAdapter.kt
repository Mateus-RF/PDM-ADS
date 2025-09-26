package com.example.animeszone

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AnimePagerAdapter(
    activity: AppCompatActivity,
    private val titles: List<String>
) : FragmentStateAdapter(activity) {

    private val fragments = MutableList(titles.size) { AnimeListFragment(mutableListOf()) }

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setData(position: Int, list: MutableList<SimpleAnime>) {
        fragments[position].updateData(list)
    }
}



