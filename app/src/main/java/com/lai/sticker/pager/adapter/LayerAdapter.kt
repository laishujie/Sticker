package com.lai.sticker.pager.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lai.core.bean.ContentLayer
import com.lai.sticker.R

class LayerAdapter : BaseQuickAdapter<ContentLayer, BaseViewHolder>(R.layout.item_layer),
    View.OnClickListener {

    var lastId: Int = -1

    override fun convert(holder: BaseViewHolder, item: ContentLayer) {
        val view = holder.getView<TextView>(R.id.textView2)
        view.text = "图层：${item.id}"
        if (item.isSelect) {
            view.setBackgroundResource(R.drawable.bg_red_round_corner)
        } else {
            view.setBackgroundResource(R.drawable.bg_white_corner)
        }
    }

    fun select(selectPosition: Int) {
        if (lastId != -1) {
            data[lastId].isSelect = false
            notifyItemChanged(lastId)
        }
        data[selectPosition].isSelect = true
        lastId = selectPosition
        notifyItemChanged(selectPosition)
    }

    override fun onClick(v: View?) {

    }
}