package com.github.pedramrn.slick.parent.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.pedramrn.slick.parent.R
import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentContainerBoxOffice.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentContainerBoxOffice.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentContainerBoxOffice : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val FRAGMENT_TYPE = "FRAGMENT_TYPE"
    // TODO: Rename and change types of parameters
    private var bundle: Bundle? = null
    private var fragmentType: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it.getBundle(ARG_PARAM1)
            fragmentType = it.getString(FRAGMENT_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    private val TAG = "FRAGMENT_BOX_OFFICE"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (childFragmentManager?.findFragmentByTag(TAG) == null)
            childFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, ControllerBoxOffice(), TAG)
                    ?.commit()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param bundle Parameter 1.
         * @return A new instance of fragment FragmentContainer.
         */
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
                FragmentContainerBoxOffice().apply {
                    arguments = Bundle().apply {
                        putBundle(ARG_PARAM1, bundle)
                    }
                }
    }
}