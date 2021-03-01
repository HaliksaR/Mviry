package ru.haliksar.mviry.core

import kotlinx.coroutines.flow.Flow

interface MviryRenderView<State, Action, SideEffect> {

    fun render(state: State)

    fun actionsFlow(): Flow<Action>

    fun sideEffects(sideEffect: SideEffect)
}