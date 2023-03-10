package com.lai.sticker.pager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lai.sticker.R
import com.lai.sticker.databinding.ActivityEditBinding
//import com.lai.sticker.pager.adapter.LayerAdapter


class EditActivity : AppCompatActivity(R.layout.activity_edit), View.OnClickListener {

    private val viewBinding by viewBinding(ActivityEditBinding::bind)
//    private val contentLayers = ArrayList<ContentLayer>()
/*
    private val layerAdapter by lazy {
        LayerAdapter()
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.addLayer.setOnClickListener(this)
        viewBinding.rvLayer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        /*layerAdapter.setNewInstance(contentLayers)
        viewBinding.rvLayer.adapter = layerAdapter
        viewBinding.rvLayer.animation = null
        layerAdapter.setOnItemClickListener { _, _, position ->
            layerAdapter.select(position)
        }*/

    }

    override fun onClick(v: View?) {
        /*if (v?.id == R.id.add_layer) {
            val generateViewId = View.generateViewId()
            val contentLayer = ContentLayer(generateViewId, false, ArrayList())
            layerAdapter.addData(contentLayer)
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}