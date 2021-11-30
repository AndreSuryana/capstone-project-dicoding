package com.dicoding.kasmee.util

open class Event<out T>(private val data: T) {

    private var handled = false

    fun getDataIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            data
        }
    }
}