package dev.hydroh.mixxy.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostSelectionInterceptor @Inject constructor() : Interceptor {
    var host: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        host?.let {
            val url = request.url.newBuilder().host(it).build()
            request = request.newBuilder().url(url).build()
        }
        return chain.proceed(request)
    }
}
