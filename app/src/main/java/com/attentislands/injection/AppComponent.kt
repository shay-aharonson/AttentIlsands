package com.attentislands.injection

import android.app.Application
import com.attentislands.base.AttentIslandsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ContextModule::class, ActivityModule::class])
interface AppComponent : AndroidInjector<AttentIslandsApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun contextModule(contextModule: ContextModule): Builder
        fun build(): AppComponent
    }
}