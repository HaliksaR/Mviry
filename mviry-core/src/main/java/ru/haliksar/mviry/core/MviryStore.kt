package ru.haliksar.mviry.core

interface MviryStore<State, Action, Modification, SideEffect> {

    val store: Mviry<State, Action, Modification, SideEffect>
}