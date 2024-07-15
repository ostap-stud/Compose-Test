package com.dev.cuckooxa.composetest

object ApiProvider{
    private var _testAPI: TestAPI? = null
    val testAPI
        get() = checkNotNull(_testAPI)

    fun setUpAPI(api: TestAPI){
        _testAPI = api
    }
}