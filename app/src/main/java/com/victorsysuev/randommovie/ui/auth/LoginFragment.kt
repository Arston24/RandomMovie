package com.victorsysuev.randommovie.ui.auth

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.HttpAuthHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.victorsysuev.randommovie.repository.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.arston.randommovie.R
import ru.arston.randommovie.databinding.FragmentLoginBinding
import timber.log.Timber


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


        binding.singupText.setOnClickListener {
            binding.webLabel.visibility = View.VISIBLE
            binding.webView.webViewClient = Client(binding)
            binding.webView.loadUrl("https://www.themoviedb.org/signup")

        }


        binding.enter.setOnClickListener {
            GlobalScope.launch {
               val res =  UserRepository().authenticateUser(binding.userName.text.toString(), binding.password.text.toString())
                Timber.e("res $res")
                findNavController().popBackStack()
            }
        }


        return binding.root
    }
}

 class Client(val binding: FragmentLoginBinding) : WebViewClient() {

     override fun onReceivedHttpAuthRequest(
         view: WebView?,
         handler: HttpAuthHandler?,
         host: String?,
         realm: String?
     ) {
         Timber.e("onReceivedHttpAuthRequest view ${view} host $host realm $realm")
         super.onReceivedHttpAuthRequest(view, handler, host, realm)
     }

     override fun onPageFinished(view: WebView?, url: String?) {
         Timber.e("onPageFinished view ${view} url $url")
         super.onPageFinished(view, url)
     }

     override fun onLoadResource(view: WebView?, url: String?) {
         Timber.e("onLoadResource view ${view} url $url")
         super.onLoadResource(view, url)
     }

     override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
         Timber.e("onPageStarted view ${view} url $url")
         if(url == "https://www.themoviedb.org/login"){
             binding.webLabel.visibility = View.GONE
         }
         super.onPageStarted(view, url, favicon)
     }
 }