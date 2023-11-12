package com.wein_business.ui.fragment.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wein_business.R
import com.wein_business.ui.activity.RegisterActivity
import com.wein_business.ui.fragment.generic.GenericFragment
import com.wein_business.utils.enums.UserType
import kotlinx.android.synthetic.main.fragment_user_type.*

class UserTypeFragment : GenericFragment(),
    View.OnClickListener {

    private lateinit var activity: RegisterActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as RegisterActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        listeners()
    }

    private fun bindUI() {

    }

    private fun listeners() {
        btnIndividual_userType.setOnClickListener(this)
        btnCompany_userType.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnIndividual_userType -> activity.setUserTypeValue(UserType.PROVIDER_USER)
            btnCompany_userType -> activity.setUserTypeValue(UserType.PROVIDER_COMPANY)
        }
    }
}