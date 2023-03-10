package com.lai.sticker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lai.sticker.databinding.ActivityMainBinding
import com.lai.sticker.view.bean.StickerTrickItem


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Example of a call to a native method
        viewBinding.controller.allowInterceptCallback = {
            viewBinding.hFrame.allowIntercept = it
        }
        viewBinding.hFrame.scrollList.add(viewBinding.timeRuler.iIScrollLayout)
        viewBinding.hFrame.scrollList.add(viewBinding.controller.iIScrollLayout)
        viewBinding.button.setOnClickListener {
//            val intent = Intent(this, EditActivity::class.java)
//            startActivity(intent)
            val stickerItemView = StickerTrickItem(this)
            stickerItemView.startMillisecond = 0
            stickerItemView.endMillisecond = 4000
            viewBinding.controller.addSticker(0, stickerItemView)
        }

    }


}