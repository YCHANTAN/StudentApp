package com.example.studentapp.data.remote

import com.example.studentapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private val BASE_URL = BuildConfig.API_BASE_URL

    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        authToken = token
    }

    fun getAuthToken(): String? = authToken

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val financeApi: FinanceApi = retrofit.create(FinanceApi::class.java)
    val academicApi: AcademicApi = retrofit.create(AcademicApi::class.java)
    val documentRequestApi: DocumentRequestApi = retrofit.create(DocumentRequestApi::class.java)
    val complaintApi: ComplaintApi = retrofit.create(ComplaintApi::class.java)
    val enrollmentApi: EnrollmentApi = retrofit.create(EnrollmentApi::class.java)
    val libraryApi: LibraryApi = retrofit.create(LibraryApi::class.java)
}
