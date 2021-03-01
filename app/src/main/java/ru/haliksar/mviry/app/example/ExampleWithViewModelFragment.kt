package ru.haliksar.mviry.app.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.haliksar.mviry.core.MviryRenderView


class ExampleWithViewModelFragment : MviryRenderView<State, Action, SideEffect> {

    private val lifecycleScope = CoroutineScope(Job())

    private val viewModel = ExampleViewModel()

    private fun onCreate() {
        lifecycleScope.launch {
            viewModel.store.bindView(view = this@ExampleWithViewModelFragment, scope = this)
        }
        lifecycleScope.launch {
            viewModel.store.bindSideEffects(view = this@ExampleWithViewModelFragment, scope = this)
        }
    }

    override fun render(state: State) {
        when (state) {
            State.Default -> {
                // render view
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun actionsFlow(): Flow<Action> = callbackFlow {
        //generate actions click and other
        awaitClose()
    }

    override fun sideEffects(sideEffect: SideEffect) { // single events
        when (sideEffect) {
            is SideEffect.ShowToast -> {
                // Toast.show
            }
        }
    }
}