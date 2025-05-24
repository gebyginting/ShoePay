import com.geby.shoepay.BuildConfig
import com.geby.shoepay.data.retrofit.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        private val authInterceptor = Interceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", BuildConfig.API_KEY)
                .build()
            chain.proceed(request)
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val tripayApi: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

    }
}