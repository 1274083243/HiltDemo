package com.ike.hiltdemo

import javax.inject.Inject

class User @Inject constructor() {
    fun getUserName():String{
        return "IKe"
    }
}