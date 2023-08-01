package com.example.thecodingstory

import com.example.thecodingstory.api.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..120) {
            val story = ListStoryItem(
                "https://i.pinimg.com/originals/7e/03/97/7e03971f15d23233289ca4a5b5d935e7.jpg",
                "dummy create at",
                "dummy creator $i",
                "dummy description",
                109.35006589013962,
                "$i",
                -0.02807932546533127
            )
            items.add(story)
        }
        return items
    }
}


