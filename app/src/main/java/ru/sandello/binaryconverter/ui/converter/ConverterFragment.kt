package ru.sandello.binaryconverter.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.FragmentConverterBinding

class ConverterFragment : Fragment() {
    private lateinit var binding: FragmentConverterBinding
    private val model: ConverterViewModel by navGraphViewModels(R.id.nav_graph)

    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetInternal: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentConverterBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetDialog = BottomSheetDialog(view.rootView.context)
        bottomSheetDialog!!.setContentView(R.layout.fragment_explanation)
        bottomSheetInternal = bottomSheetDialog!!.findViewById(R.id.bottomSheetMain)
        bottomSheetDialog!!.behavior.skipCollapsed = true
        bottomSheetDialog!!.behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_HIDDEN) {
                    bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                    bottomSheetDialog!!.dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        /*activityBinding.explanationFab.setOnClickListener {
            if (binding.editText10.text.toString().isNotEmpty()) {
                try {
                    val job = GlobalScope.launch(Dispatchers.Main) {
                        async {
                            bottomSheetDialog!!.show()
                            delay(10)
                            bottomSheetDialog!!.behavior.peekHeight =
                                bottomSheetInternal!!.rootView.bottomSheetCardView.height
                            delay(10)
                            bottomSheetDialog!!.behavior.state = STATE_COLLAPSED
                            bottomSheetInternal!!.rootView.progressBar.visibility = View.VISIBLE
                            delay(350)
                        }.join()
                        async {
                            bottomSheetInternal!!.rootView.bottomSheetScrollView.setPadding(
                                0,
                                bottomSheetInternal!!.rootView.bottomSheetAppBar.height - 8,
                                0,
                                0
                            )
                            bottomSheetInternal!!.rootView.bottomSheetContent.setPadding(
                                0,
                                16,
                                0,
                                0
                            )
                            parseRepeat()
                        }.join()
                        async {
                            delay(1)
                            if (bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString()
                                    .toInt() != bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString()
                                    .toInt()
                            ) {
                                bottomSheetDialog!!.behavior.state = STATE_EXPANDED
                            }
                            bottomSheetInternal!!.rootView.progressBar.visibility = View.INVISIBLE


                        }
                    }
                } catch (e: Exception) {
                }
            }
        }*/
    }

}
