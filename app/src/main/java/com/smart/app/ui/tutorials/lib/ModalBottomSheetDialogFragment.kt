package com.smart.app.ui.tutorials.lib

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("unused")
class ModalBottomSheetDialogFragment : BottomSheetDialogFragment() {
}

// Unuse
//    companion object {
//        fun newInstance() = ModalBottomSheetDialogFragment()
//    }
//
//    lateinit var binding: DialogFragmentModalBottomSheetBinding
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = DialogFragmentModalBottomSheetBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initDialog()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun initDialog() {
//        requireDialog().window?.statusBarColor = requireContext().getColor(android.R.color.transparent)
//        requireDialog().findViewById<View>(R.id.select_image_click_container_left).setOnClickListener {
//            if (!SelectionAllToggleSelector.isAllSelectChecked) {
//                requireDialog().findViewById<ImageView>(R.id.take_image_icon).setImageResource(R.drawable.ic_checkmark1)
//                SelectionAllToggleSelector.isAllSelectChecked = !SelectionAllToggleSelector.isAllSelectChecked
//            } else {
//                requireDialog().findViewById<ImageView>(R.id.take_image_icon).setImageResource(R.drawable.ic_refresh)
//                SelectionAllToggleSelector.isAllSelectChecked = !SelectionAllToggleSelector.isAllSelectChecked
//            }
//        }
//        requireDialog().findViewById<View>(R.id.select_image_click_container).setOnClickListener {
//            Toast.makeText(requireContext(), "삭제", Toast.LENGTH_SHORT).show()
//            // do some later here
//        }
//        requireDialog().findViewById<View>(R.id.select_image_click_container_right).setOnClickListener {
//            requireDialog().cancel()
//        }
//    }
// Also
/*
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:paddingTop="18dp"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <!--        <ImageView-->
        <!--            android:id="@+id/take_image_icon"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            tools:layout_width="12dp"-->
        <!--            tools:layout_height="12dp"-->
        <!--            android:paddingTop="1dp"-->
        <!--            android:paddingBottom="1dp"-->
        <!--            android:layout_marginBottom="1dp"-->
        <!--            android:minWidth="12dp"-->
        <!--            android:minHeight="12dp"-->
        <!--            android:maxWidth="12dp"-->
        <!--            android:maxHeight="12dp"-->
        <!--            android:src="@drawable/ic_refresh"-->
        <!--            android:layout_marginEnd="10dp"-->
        <!--            app:layout_constraintEnd_toStartOf="@id/take_image_label"-->
        <!--            app:layout_constraintTop_toTopOf="parent"-->
        <!--            app:tint="@color/smart_black_01" />-->

        <!--        <TextView-->
        <!--            android:id="@+id/take_image_label"-->
        <!--            android:textSize="14sp"-->
        <!--            android:textColor="@color/smart_black_01"-->
        <!--            android:fontFamily="@font/t_font_light"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            android:text="모두 선택"-->
        <!--            android:layout_marginBottom="14dp"-->
        <!--            android:layout_marginEnd="6dp"-->
        <!--            app:layout_constraintBottom_toTopOf="@id/bottom_area"-->
        <!--            app:layout_constraintStart_toStartOf="parent"-->
        <!--            app:layout_constraintEnd_toEndOf="@id/select_image_label" />-->

        <!--        <View-->
        <!--            android:id="@+id/select_image_click_container_left"-->
        <!--            android:layout_width="100dp"-->
        <!--            android:layout_height="48dp"-->
        <!--            tools:layout_height="48dp"-->
        <!--            android:minHeight="48dp"-->
        <!--            android:background="?attr/selectableItemBackgroundBorderless"-->
        <!--            android:clickable="true"-->
        <!--            android:focusable="true"-->
        <!--            app:layout_constraintBottom_toTopOf="@id/select_image_barrier"-->
        <!--            app:layout_constraintEnd_toEndOf="@id/take_image_label" />-->


        <!--        <androidx.constraintlayout.widget.Barrier-->
        <!--            android:id="@+id/take_image_barrier"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            app:barrierDirection="bottom"-->
        <!--            app:constraint_referenced_ids="take_image_icon" />-->


        <!--        <ImageView-->
        <!--            android:id="@+id/select_image_icon"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            android:visibility="invisible"-->
        <!--            tools:visibility="invisible"-->
        <!--            android:layout_margin="0dp"-->
        <!--            app:layout_constraintStart_toStartOf="parent"-->
        <!--            app:layout_constraintTop_toTopOf="@+id/take_image_barrier" />-->

        <!--        <TextView-->
        <!--            android:id="@+id/select_image_label"-->
        <!--            android:textSize="14sp"-->
        <!--            android:textColor="@color/smart_black_01"-->
        <!--            android:fontFamily="@font/t_font_light"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            android:text="삭제"-->
        <!--            android:layout_marginBottom="14dp"-->
        <!--            app:layout_constraintBottom_toTopOf="@id/bottom_area"-->
        <!--            app:layout_constraintEnd_toEndOf="parent"-->
        <!--            app:layout_constraintStart_toStartOf="parent" />-->


        <!--        <androidx.constraintlayout.widget.Barrier-->
        <!--            android:id="@+id/select_image_barrier"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            app:barrierDirection="bottom"-->
        <!--            app:barrierMargin="16dp"-->
        <!--            app:constraint_referenced_ids="select_image_icon" />-->

        <!--        <View-->
        <!--            android:id="@+id/select_image_click_container"-->
        <!--            android:layout_width="100dp"-->
        <!--            android:layout_height="48dp"-->
        <!--            tools:layout_height="48dp"-->
        <!--            android:minHeight="48dp"-->
        <!--            android:paddingTop="20dp"-->
        <!--            android:background="?attr/selectableItemBackgroundBorderless"-->
        <!--            android:clickable="true"-->
        <!--            android:focusable="true"-->
        <!--            app:layout_constraintBottom_toTopOf="@id/select_image_barrier"-->
        <!--            app:layout_constraintEnd_toEndOf="parent"-->
        <!--            app:layout_constraintStart_toStartOf="parent" />-->

        <!--        <TextView-->
        <!--            android:id="@+id/select_image_label2"-->
        <!--            android:textSize="14sp"-->
        <!--            android:textColor="@color/smart_black_01"-->
        <!--            android:fontFamily="@font/t_font_light"-->
        <!--            android:layout_width="wrap_content"-->
        <!--            android:layout_height="wrap_content"-->
        <!--            android:text="취소"-->
        <!--            android:layout_marginBottom="14dp"-->
        <!--            app:layout_constraintBottom_toTopOf="@id/bottom_area"-->
        <!--            app:layout_constraintStart_toEndOf="@+id/select_image_label"-->
        <!--            app:layout_constraintEnd_toEndOf="parent" />-->

        <!--        <View-->
        <!--            android:id="@+id/select_image_click_container_right"-->
        <!--            android:layout_width="80dp"-->
        <!--            android:layout_height="48dp"-->
        <!--            android:layout_marginStart="6dp"-->
        <!--            android:minHeight="48dp"-->
        <!--            android:background="?attr/selectableItemBackgroundBorderless"-->
        <!--            android:clickable="true"-->
        <!--            android:focusable="true"-->
        <!--            app:layout_constraintBottom_toTopOf="@id/select_image_barrier"-->
        <!--            app:layout_constraintEnd_toEndOf="parent"-->
        <!--            app:layout_constraintStart_toEndOf="@id/select_image_label" />-->

        <!--        <View-->
        <!--            android:id="@+id/bottom_area"-->
        <!--            android:layout_width="0dp"-->
        <!--            android:layout_height="8dp"-->
        <!--            app:layout_constraintEnd_toEndOf="parent"-->
        <!--            app:layout_constraintStart_toStartOf="parent"-->
        <!--            app:layout_constraintTop_toBottomOf="@id/select_image_barrier" />-->

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
 */