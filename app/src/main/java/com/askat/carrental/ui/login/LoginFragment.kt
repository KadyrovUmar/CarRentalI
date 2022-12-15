package com.askat.carrental.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.askat.carrental.R


class LoginFragment : Fragment() {

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)

 }

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {

  return inflater.inflate(R.layout.fragment_login, container, false)
 }
}