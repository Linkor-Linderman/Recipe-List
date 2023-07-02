package com.example.recipe_list.domain.utill


sealed class RecipeOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): RecipeOrder(orderType)
    class Date(orderType: OrderType): RecipeOrder(orderType)

    fun copy(orderType: OrderType): RecipeOrder{
        return when(this){
            is Name -> Name(orderType)
            is Date -> Date(orderType)
        }
    }
}