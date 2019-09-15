package co.yulu.assignment.di.component

import android.app.Application
import co.yulu.assignment.application.DemoApplication
import co.yulu.assignment.di.module.*

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    AppModule::class,
    DbModule::class,
    ApiServiceModule::class,
    FragmentModule::class,
    ViewModelModule::class])
interface AppComponent {

    fun inject(application: DemoApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}
