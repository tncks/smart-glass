package com.smart.app.ui.mypage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.smart.app.R
import com.smart.app.ui.DevByteActivity
import com.smart.app.ui.common.MoveToGalleryCameraPickOneWithNextActivity
import com.smart.app.ui.feed.MemoWithoutPictureActivity
import com.smart.app.ui.feed.PictureWithMemoActivity
import com.smart.app.ui.feed.PictureWithoutMemoActivity
import com.smart.app.ui.schedule.ScheduleWriteEditActivity
import com.smart.app.ui.schedule.TheLocationActivity
import com.smart.app.ui.signstep.*

class MypageFragment : Fragment() {

    private var viewProfile: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewProfile = inflater.inflate(R.layout.fragment_mypage, container, false)

        return viewProfile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setTestRestEventsAtOnce(view)
    }

    /*-------------------------------------------------------------------*/

    private fun setTestRestEventsAtOnce(view: View) {

        view.findViewById<Button>(R.id.btn_move_test_1).setOnClickListener {
            val intent = Intent(activity, PictureWithMemoActivity::class.java)
//            val intent = Intent(activity, MoveToGalleryCameraPickOneActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_2).setOnClickListener {
            val intent = Intent(activity, PictureWithMemoActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_3).setOnClickListener {
            val intent = Intent(activity, PictureWithoutMemoActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_4).setOnClickListener {
            val intent = Intent(activity, JoinNormalNewActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_5).setOnClickListener {
            val intent = Intent(activity, ForgotPwNeedEmailAddressActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_6).setOnClickListener {
            val intent = Intent(activity, ForgotPwConfirmEmailCheckValidActivity::class.java)
            startActivity(intent)
        }




        view.findViewById<Button>(R.id.btn_move_test_7).setOnClickListener {
            val intent = Intent(activity, JoinEntireViewOfTermsAgreeActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_8).setOnClickListener {
            val intent = Intent(activity, ScheduleWriteEditActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_9).setOnClickListener {
            val intent = Intent(activity, ForgotResetPasswordRenewActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_10).setOnClickListener {
            val intent = Intent(activity, JoinCompleteSuccessActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_11).setOnClickListener {
            val intent = Intent(activity, JoinIncompleteInvalidorfailActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_12).setOnClickListener {
            val intent = Intent(activity, TheLocationActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_13).setOnClickListener {
            val intent = Intent(activity, MoveToGalleryCameraPickOneWithNextActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_14).setOnClickListener {
            val intent = Intent(activity, MemoWithoutPictureActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_15).setOnClickListener {
            val intent = Intent(activity, DevByteActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_move_test_16).setOnClickListener {
            val intent = Intent(activity, SignBeforeStartActivity::class.java)
            startActivity(intent)
        }



        view.findViewById<Button>(R.id.btn_test_myinfo_and_logout).setOnClickListener {
            val intent = Intent(activity, UserPrivacyAndSignOutActivity::class.java)
            startActivity(intent)
        }


    }


}