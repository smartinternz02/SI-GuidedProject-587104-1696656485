package com.aarush.core.abstraction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding

abstract class BaseFragmentBinding<T: ViewBinding> : Fragment() {

    private var _binding: T? = null

    var isNeedToRefresh = false
    protected var onRefresh : (() -> Unit)? = null

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    protected val binding: T
        get() {
            if (_binding == null) {
                throw IllegalArgumentException("View binding is not initialized yet")
            }
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callOnceWhenCreated { setupView() }

    }

    protected abstract fun setupView()

    protected fun callOnceWhenCreated(callable: () -> Unit) {
        lifecycleScope.launchWhenCreated { callable() }
    }

    protected fun callOnceWhenDisplayed(callable: () -> Unit) {
        lifecycleScope.launchWhenResumed { callable() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        currentFocusView?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }
}