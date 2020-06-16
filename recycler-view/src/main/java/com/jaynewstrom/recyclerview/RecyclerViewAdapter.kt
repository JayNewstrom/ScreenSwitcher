package com.jaynewstrom.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

internal class RecyclerViewAdapter(
    var data: AdapterData,
    val context: Context
) : RecyclerView.Adapter<StateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val viewFactory = data.itemForViewType(viewType)
        val view = LayoutInflater.from(parent.context).inflate(viewFactory.layoutResource(context), parent, false)
        val initializedState = viewFactory.initialize(view)
        return StateViewHolder(view, initializedState as Any)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val viewFactory = data.itemAtPosition(position)
        viewFactory.bindAny(holder.initializedState)
    }

    override fun getItemCount(): Int = data.itemCount()

    override fun getItemViewType(position: Int): Int = data.itemAtPosition(position).layoutResource(context)

    fun setData(data: AdapterData, diffResult: DiffUtil.DiffResult) {
        this.data = data
        diffResult.dispatchUpdatesTo(this)
    }
}
