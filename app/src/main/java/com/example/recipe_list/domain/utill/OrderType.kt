package com.example.recipe_list.domain.utill

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}