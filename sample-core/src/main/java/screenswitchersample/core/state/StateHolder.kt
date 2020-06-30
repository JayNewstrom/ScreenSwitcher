package screenswitchersample.core.state

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

class StateHolder<State> constructor(initialState: State) {
    private val relay: BehaviorRelay<State> = BehaviorRelay.createDefault(initialState)

    var state: State
        get() = relay.value!!
        set(viewFactories) = relay.accept(viewFactories)

    val stateObservable: Observable<State> = relay.distinctUntilChanged()
}
