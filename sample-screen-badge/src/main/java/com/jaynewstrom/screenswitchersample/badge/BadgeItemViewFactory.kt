package com.jaynewstrom.screenswitchersample.badge

import android.content.Context
import android.view.View
import android.widget.TextView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jaynewstrom.recyclerview.RecyclerItemViewFactory
import com.jaynewstrom.screenswitchersample.core.ViewPresenter
import com.jaynewstrom.screenswitchersample.core.setClickListener
import javax.inject.Inject
import kotlin.math.abs

internal data class BadgeItemViewFactory(private val amount: Int) : RecyclerItemViewFactory<BadgeItemPresenter>() {
    override fun layoutResource(context: Context): Int = R.layout.badge_item_view
    override fun initialize(itemView: View): BadgeItemPresenter = BadgeItemPresenter(itemView)

    override fun bind(presenter: BadgeItemPresenter) {
        presenter.setAmount(amount)
    }
}

internal class BadgeItemPresenter(view: View) : ViewPresenter(view) {
    @Inject @BadgeCount lateinit var badgeCountRelay: BehaviorRelay<Int>

    private var amount: Int = 0

    private val button = bindView<TextView>(R.id.button)

    init {
        component<BadgeComponent>().inject(this)

        button.setClickListener {
            badgeCountRelay.accept(badgeCountRelay.value!! + amount)
        }
    }

    fun setAmount(amount: Int) {
        this.amount = amount
        button.text = if (amount > 0) {
            "Increment $amount"
        } else {
            "Decrement ${abs(amount)}"
        }
    }
}
