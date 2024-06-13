package com.foundie.id.ui.community.community_create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.foundie.id.databinding.FragmentCommunityCreateBinding

class CommunityCreateFragment : Fragment() {

    private var _binding: FragmentCommunityCreateBinding? = null
    private val binding get() = _binding!!

    private lateinit var image : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityCreateBinding.inflate(inflater, container, false)

        binding.imgBackgroundCommunity.setOnClickListener {
            image = binding.imgBackgroundCommunity
            uploadImage(image)
        }

        binding.ivBackgroundCommunity.setOnClickListener {
            image = binding.ivBackgroundCommunity
            uploadImage(image)
        }

        return binding.root
    }

    private fun uploadImage(image: ImageView) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            image.setImageURI(data?.data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        _binding = null
    }

}
