package com.example.weatherforecast.di

import android.app.Application
import android.content.Context
import com.example.weatherforecast.api.APIService
import com.example.weatherforecast.api.APIService.Companion.BASE_URL
import com.example.weatherforecast.db.MyDatabase
import com.example.weatherforecast.db.TempDao
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getAppDB(context: Application):MyDatabase{
        return MyDatabase.getMyDB(context)
    }

    @Singleton
    @Provides
    fun getDao(appDB:MyDatabase):TempDao{
        return appDB.getDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okhttpClientBuilder.addInterceptor(logging)

        return okhttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit):APIService{
        return retrofit.create(APIService::class.java)
    }

}