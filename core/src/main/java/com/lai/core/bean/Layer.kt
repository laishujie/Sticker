package com.lai.core.bean

data class Sticker(
    val id: Int,
    var filePath: String
)

data class ContentLayer(
    val id: Int,
    var isSelect: Boolean,
    val arrayList: ArrayList<Sticker>
)












