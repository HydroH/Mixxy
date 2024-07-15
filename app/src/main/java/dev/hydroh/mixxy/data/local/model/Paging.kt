package dev.hydroh.mixxy.data.local.model

data class Paging<T>(
    val edge: T?,
    val direction: Direction,
    val limit: Int,
)

enum class Direction {
    PREV,
    NEXT,
}