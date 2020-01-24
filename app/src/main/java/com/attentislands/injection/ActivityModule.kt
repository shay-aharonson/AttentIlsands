package com.attentislands.injection

import com.attentislands.grid.GridActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Injection module for the application activities.
 */
@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindGridActivity(): GridActivity

}