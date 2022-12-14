package com.weather.checker.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.weather.checker.data.local.db.AppDatabase
import com.weather.checker.data.remote.network.ApiClient
import com.weather.checker.data.remote.network.ApiService
import com.weather.checker.utils.AppConstants
import com.weather.checker.utils.NetworkUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    single { get<Retrofit>().create(ApiService::class.java) }
    single { provideRetrofit(get()) }
    single { provideOkHttpClient(get(), get()) }
    single { provideInterceptor(get()) }
    single { provideCache(get()) }

    single { provideAppDatabase(provideAppContext(get()), provideDatabaseName()) }
    single { provideSharedPreferences(get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(ApiClient.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun provideCache(context: Context): Cache {
    val cacheSize: Long = 10 * 1024 * 1024
    return Cache(context.cacheDir, cacheSize)
}

private fun provideOkHttpClient(interceptor: Interceptor, cache: Cache): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(interceptor)
        .build()
}

private fun provideInterceptor(context: Context): Interceptor {
    return Interceptor { chain ->
        var request: Request = chain.request()
        request = if (!NetworkUtils.isNetworkAvailable(context)) {
            val maxStale = 60 * 60 * 24 * 28 // 4-weeks-stale
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale$maxStale")
                .build()
        } else {
            val maxAge = 5 // fresh data
            request.newBuilder()
                .header("Cache-Control", "public, max-age = $maxAge")
                .build()
        }
        chain.proceed(request)
    }
}

private fun provideAppContext(application: Application) = application

private fun provideDatabaseName() = AppConstants.DATABASE_NAME

private fun provideAppDatabase(context: Context, databaseName: String) =
    Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
        .allowMainThreadQueries().build()

private fun provideSharedPreferences(context: Context) =
    context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
