package ru.haliksar.mviry.core

import kotlinx.coroutines.flow.MutableSharedFlow

typealias Handler<State, Action, Modification, SideEffect> =
        suspend MutableSharedFlow<Modification>.(
            state: State,
            action: Action,
            sideEffectsFlow: MutableSharedFlow<SideEffect>,
        ) -> Unit

typealias Reducer<State, Modification> =
        suspend MutableSharedFlow<State>.(
            state: State,
            modification: Modification
        ) -> Unit