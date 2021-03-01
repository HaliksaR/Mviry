package ru.haliksar.mviry.app.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.haliksar.mviry.core.Handler
import ru.haliksar.mviry.core.Mviry
import ru.haliksar.mviry.core.MviryStore
import ru.haliksar.mviry.core.Reducer

class ExampleViewModel : MviryStore<State, Action, Modification, SideEffect> {

    private val viewModelScope = CoroutineScope(Job())

    // handle actions, generate side effects (single events) and send changes (may be viewModel ane change)
    private val handler: Handler<State, Action, Modification, SideEffect> =
        { _: State, action: Action, sideEffectsFlow: MutableSharedFlow<SideEffect> ->
            when (action) {
				Action.Click -> {
					sideEffectsFlow.emit(SideEffect.ShowToast(""))
					delay(10)
					emit(Modification.ChangeText(""))
				}
            }
        }

    // handle modifications and current state and generate new state for view
    private val reducer: Reducer<State, Modification> = { _: State, modification: Modification ->
        when (modification) {
			is Modification.ChangeText -> {
				State.Default
			}
        }
    }

    override val store: Mviry<State, Action, Modification, SideEffect> = Mviry(
		initialState = State.Default,
		reducer = reducer,
		handler = handler,
		scope = viewModelScope
	)  // create "store"
}