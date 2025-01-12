package com.example.homework3.di

import android.content.Context
import android.content.SharedPreferences
import com.example.homework3.data.remote.common.EmailSignInResponseHandler
import com.example.homework3.data.remote.common.HandleResponse
import com.example.homework3.data.repository.FirebaseAuthRepositoryImpl
import com.example.homework3.data.repository.SharedPrefsRepositoryImpl
import com.example.homework3.domain.repository.FirebaseAuthRepository
import com.example.homework3.domain.repository.SharedPrefsRepository
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideHandleResponse(): HandleResponse = HandleResponse()

    @Provides
    @Singleton
    fun provideEmailSignInResponseHandler(ioDispatcher: CoroutineDispatcher): EmailSignInResponseHandler {
        return EmailSignInResponseHandler(ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(
        auth: FirebaseAuth,
        emailSignInResponseHandler: EmailSignInResponseHandler,
        sharedPrefsRepository: SharedPrefsRepository
    ): FirebaseAuthRepository =
        FirebaseAuthRepositoryImpl(auth, emailSignInResponseHandler, sharedPrefsRepository)

    @Provides
    @Singleton
    fun provideSharedPrefsRepository(
        sharedPreferences: SharedPreferences
    ): SharedPrefsRepository = SharedPrefsRepositoryImpl(sharedPreferences)

    @Provides
    @Singleton
    fun provideIODispatcher() = Dispatchers.IO
}