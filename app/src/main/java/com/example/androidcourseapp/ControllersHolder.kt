package com.example.androidcourseapp

object ControllersHolder {
    private var controllers: List<Controller> = emptyList()

    fun isInitialized(): Boolean {
        return controllers.isNotEmpty()
    }

    fun setControllers(controllers: List<Controller>) {
        this.controllers = controllers
    }

    fun getControllers(): List<Controller> {
        return controllers
    }
}