package com.example.pill

/**
 * Created by Дом on 14.01.2018.
 */
open class NotificationInfo (
        var id: Int,
        var name: String,
        var dose: String,
        var min: Int,
        var hour: Int,
        var isRepeat: Int,
        var isStored: Int,
        var beforeEat: Int,
        var afterEat: Int
        )