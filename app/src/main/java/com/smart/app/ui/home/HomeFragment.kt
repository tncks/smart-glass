package com.smart.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smart.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // private val viewModel: HomeViewModel by viewModels { ViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner


    }


}


/*
home.json file in asset folder remove soon and original structure is below showing
  "title": {
    "text": "LEO님, 이런 상품\n어때요?",
    "icon_url": "https://user-images.githubusercontent.com/26240553/163317354-b665c18c-8c3e-46d9-a6c9-50f336d70e2f.png"
  },
  "top_banners": [
    {
      "background_image_url": "https://user-images.githubusercontent.com/26240553/163317372-f182496a-1267-4e69-adae-4a34535d3f05.png",
      "badge": {
        "label": "기획전",
        "background_color": "#52514d"
      },
      "label": "따스한 겨울\n준비하기",
      "product_detail": {
        "brand_name": "twg. official",
        "label": "캐시미어 100% 터틀넥 스웨터",
        "discount_rate": 9,
        "price": 102000,
        "thumbnail_image_url": "https://user-images.githubusercontent.com/26240553/163317391-525129c7-5373-4ff1-b9fb-6ae2cc293331.png",
        "product_id": "FW-twg-sweater-1"
      }
    },
    {
      "background_image_url": "https://user-images.githubusercontent.com/26240553/163317383-584d7aa6-3649-4029-819b-8544627ff712.png",
      "badge": {
        "label": "기획전",
        "background_color": "#967a6d"
      },
      "label": "나만의\n홈 오피스",
      "product_detail": {
        "brand_name": "Desk",
        "label": "슬림 데스크 800",
        "discount_rate": 16,
        "price": 160000,
        "thumbnail_image_url": "https://user-images.githubusercontent.com/26240553/163317390-8f41c626-ec18-4107-aa0e-acc77f908e2f.png",
        "product_id": "desk-1"
      }
    }
  ]
 */