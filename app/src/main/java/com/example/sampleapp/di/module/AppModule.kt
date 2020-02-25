package com.example.sampleapp.di.module

import android.app.Application
import androidx.room.Room
import com.example.sampleapp.data.database.AppDatabase
import com.example.sampleapp.data.network.ApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    internal fun buildDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java, "sample.db"
        ).build()
    }

    private val API_BASE_URL = "https://api.imgur.com/3/gallery/search/"

    private val CLIENT_ID = "18913a1bb966645"
    @Provides
    @Singleton
    internal fun providePostApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getAuthorizationHeaderInterceptor())
            .build()
    }

//    @Provides
//    @Singleton
//    fun getAuthorizationHeaderInterceptor(): OkHttpClient {
//        return OkHttpClient().newBuilder().addInterceptor(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//                val originalRequest = chain.request()
//
//                val builder = originalRequest.newBuilder().header(
//                    "Authorization",
//                    "Client-ID " + CLIENT_ID
//                )
//
//                val newRequest = builder.build()
//                return chain.proceed(newRequest)
//            }
//        }).build()
//    }

    @Provides
    @Singleton
    fun getAuthorizationHeaderInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Client-ID " + CLIENT_ID)
                    .build()
                chain.proceed(newRequest)
            }
        return builder.build()
    }
}