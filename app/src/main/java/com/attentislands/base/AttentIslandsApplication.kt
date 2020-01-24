package com.attentislands.base

import com.attentislands.injection.ContextModule
import com.attentislands.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class AttentIslandsApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .application(this)
            .build()
    }
}