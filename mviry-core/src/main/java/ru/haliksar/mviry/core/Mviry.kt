package ru.haliksar.mviry.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface Mviry<State, Action, Modification, SideEffect> {

    fun bindView(
        view: MviryRenderView<State, Action, SideEffect>,
        scope: CoroutineScope,
        actions: List<Action> = listOf()
    )

    fun bindSideEffects(
        view: MviryRenderView<State, Action, SideEffect>,
        scope: CoroutineScope
    )
}

private class MviryImpl<State, Action, Modification, SideEffect>(
    initialState: State,
    private val reducer: Reducer<State, Modification>,
    private val handler: Handler<State, Action, Modification, SideEffect>,
    scope: CoroutineScope,
) : Mviry<State, Action, Modification, SideEffect>,
    CoroutineScope by scope,
    Mutex by Mutex() {

    private val state = MutableStateFlow(initialState)
    private val sideEffect = MutableSharedFlow<SideEffect>()

    override fun bindSideEffects(
        view: MviryRenderView<State, Action, SideEffect>,
        scope: CoroutineScope
    ): Unit = with(scope) {
        launch {
            sideEffect.collect { view.sideEffects(it) }
        }
    }

    override fun bindView(
        view: MviryRenderView<State, Action, SideEffect>,
        scope: CoroutineScope,
        actions: List<Action>
    ): Unit = with(scope) {
        emitStateForRender(view)
        handleActions(view, actions)
    }

    private fun CoroutineScope.emitStateForRender(
        view: MviryRenderView<State, Action, SideEffect>
    ) = launch {
        state.collect {
            view.render(it)
        }
    }

    private fun CoroutineScope.handleActions(
        view: MviryRenderView<State, Action, SideEffect>,
        actions: List<Action>
    ) = launch {
        view.actionsFlow()
            .onStart { emitAll(actions.asFlow()) }
            .proceed()
    }

    private suspend fun Flow<Action>.proceed() =
        collect { action ->
            handler.invoke(
                MutableSharedFlow<Modification>().subscribeState(),
                state.value, action, sideEffect
            )
        }

    private fun MutableSharedFlow<Modification>.subscribeState(): MutableSharedFlow<Modification> =
        also {
            onEach { modification ->
                withLock {
                    reducer.invoke(state, state.value, modification)
                }
            }.launchIn(this@MviryImpl)
        }
}

fun <State, Action, SideEffect, Modification> Mviry(
    initialState: State,
    reducer: Reducer<State, Modification>,
    handler: Handler<State, Action, Modification, SideEffect>,
    scope: CoroutineScope
): Mviry<State, Action, Modification, SideEffect> = MviryImpl(
    initialState = initialState,
    reducer = reducer,
    handler = handler,
    scope = scope
)