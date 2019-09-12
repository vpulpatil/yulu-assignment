package co.yulu.assignment.di.module

import co.yulu.assignment.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

}
