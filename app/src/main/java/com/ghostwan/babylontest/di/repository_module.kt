package com.ghostwan.babylontest.di

import com.ghostwan.babylontest.data.source.PostsRepository
import com.ghostwan.babylontest.data.source.remote.PostsRemoteRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { PostsRepository(PostsRemoteRepository("http://jsonplaceholder.typicode.com")) }
}