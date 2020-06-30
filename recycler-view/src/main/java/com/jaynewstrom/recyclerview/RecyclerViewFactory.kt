package com.jaynewstrom.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import screenswitchersample.core.state.StateHolder
import screenswitchersample.core.view.ViewPresenter

private typealias FactoryList = List<RecyclerItemViewFactory<*>>

typealias RecyclerStateHolder = StateHolder<FactoryList>

fun bootstrapRecyclerView(
    recyclerView: RecyclerView,
    initialViewFactories: FactoryList
): RecyclerViewData {
    val adapter = RecyclerViewAdapter(
        AdapterData.create(initialViewFactories, recyclerView.context),
        recyclerView.context
    )
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
    return AdapterUpdatingRecyclerViewData(adapter)
}

fun ViewPresenter.bootstrapRecyclerView(
    recyclerView: RecyclerView,
    initialViewFactories: FactoryList,
    viewFactoriesObservable: Observable<FactoryList>
): RecyclerViewData {
    val recyclerViewData = bootstrapRecyclerView(recyclerView, initialViewFactories)
    registerObservable(viewFactoriesObservable) { viewFactories ->
        recyclerViewData.update(viewFactories)
    }
    return recyclerViewData
}

fun ViewPresenter.bootstrapRecyclerView(
    recyclerView: RecyclerView,
    stateHolder: RecyclerStateHolder
): RecyclerViewData {
    return bootstrapRecyclerView(recyclerView, stateHolder.state, stateHolder.stateObservable)
}
