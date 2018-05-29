package com.fangx.mimei.ui.base

import android.app.Application
import com.fangx.mimei.extensions.DelegatesExt
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
class App : Application() {

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initPicasso()

    }

    private fun initPicasso() {
        val picasso = Picasso.Builder(this)
                .downloader(OkHttp3Downloader(getOkHttpClient()))
                .build()
        Picasso.setSingletonInstance(picasso)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm())
        val keyStore: KeyStore? = null
        trustManagerFactory.init(keyStore)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers))
        }
        val trustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)
        val sslSocketFactory = sslContext.socketFactory


        return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)    //添加信任所有证书
                .hostnameVerifier { _, _ -> true }   //信任规则全部信任
                .build()

    }
}