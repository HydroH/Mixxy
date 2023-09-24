package dev.hydroh.mixxy.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.arrow.EitherCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.hydroh.mixxy.data.remote.AccountService
import dev.hydroh.mixxy.data.remote.InstanceService
import dev.hydroh.mixxy.data.remote.NotesService
import dev.hydroh.mixxy.data.remote.adapter.ContextualTokenSerializer
import dev.hydroh.mixxy.data.remote.adapter.HostSelectionInterceptor
import dev.hydroh.mixxy.data.remote.model.request.Token
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(
        hostSelectionInterceptor: HostSelectionInterceptor,
        contextualTokenSerializer: ContextualTokenSerializer,
    ): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(hostSelectionInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
        val json = Json {
            encodeDefaults = true
            explicitNulls = false
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                contextual(Token::class, contextualTokenSerializer)
            }
        }
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://localhost")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(EitherCallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideAccountService(retrofit: Retrofit): AccountService =
        retrofit.create(AccountService::class.java)

    @Provides
    fun provideInstanceService(retrofit: Retrofit): InstanceService =
        retrofit.create(InstanceService::class.java)

    @Provides
    fun provideNotesService(retrofit: Retrofit): NotesService =
        retrofit.create(NotesService::class.java)
}
