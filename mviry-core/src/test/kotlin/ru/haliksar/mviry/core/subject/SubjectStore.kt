package ru.haliksar.mviry.core.subject

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.haliksar.mviry.core.Handler
import ru.haliksar.mviry.core.Mviry
import ru.haliksar.mviry.core.MviryStore
import ru.haliksar.mviry.core.Reducer

class SubjectStore<TD>(
    scope: CoroutineScope,
    defaultState: State,
    private val testData: TD
) : MviryStore<State, Action, Modification, SideEffect> {

    val modifications = mutableListOf<Modification>()

    private val handler: Handler<State, Action, Modification, SideEffect> =
        { state: State, action: Action, sideEffectsFlow: MutableSharedFlow<SideEffect> ->
            when (action) {
                Action.Click -> {
                    emit(Modification.ChangeDescription(testData))
                    sideEffectsFlow.emit(SideEffect.ShowToast(testData))
                }
                Action.Tap -> {
                    emit(Modification.ChangeTitle(testData))
                    sideEffectsFlow.emit(SideEffect.ShowToast(testData))
                }
            }
        }

    private val reducer: Reducer<State, Modification> =
        { state: State, modification: Modification ->
            modifications.add(modification)
            when (modification) {
                is Modification.ChangeDescription<*> -> {
                    emit(State.SetDescription(modification.text))
                    emit(State.Default)
                }
                is Modification.ChangeTitle<*> -> {
                    emit(State.SetTitle(modification.text))
                    emit(State.Default)
                }
            }
        }

    override val store: Mviry<State, Action, Modification, SideEffect> =
        Mviry(
            scope = scope,
            initialState = defaultState,
            handler = handler,
            reducer = reducer
        )
}