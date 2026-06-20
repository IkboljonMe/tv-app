package com.karuhun.core.common.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val launcherDispacther: LauncherDispatcher)

enum class LauncherDispatcher {
    Default,
    IO
}