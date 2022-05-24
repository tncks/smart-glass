package com.smart.app.repository.category

import com.smart.app.common.DELIM
import com.smart.app.model.Category

class CategoryRepository(
    private val remoteDataSource: CategoryRemoteDataSource
) {

    suspend fun getCategories(uid: String): List<Category>? {
        val tmp: List<Category>? = remoteDataSource.getCategories(uid)


        /*------------------------------------------------------*/
        /*------------------------------------------------------*/
        Supglobal.mSup = ""
        Supglobal.mLabel = ""
        Supglobal.mLocation = ""
        Supglobal.mPeriod = ""
        Supglobal.mMemo = ""
        /*----------------------*/
        if (tmp != null) {
            if (tmp.isNotEmpty()) {
                for (item in tmp) {
                    Supglobal.mSup += item.thumbnailImageUrl
                    Supglobal.mSup += DELIM

                    Supglobal.mLabel += item.label
                    Supglobal.mLabel += DELIM

                    Supglobal.mLocation += item.location
                    Supglobal.mLocation += DELIM

                    Supglobal.mPeriod += item.period
                    Supglobal.mPeriod += DELIM

                    Supglobal.mMemo += item.memo
                    Supglobal.mMemo += DELIM
                }
                /*----------------------*/
                Supglobal.mSup = Supglobal.mSup.dropLast(1)
                Supglobal.mLabel = Supglobal.mLabel.dropLast(1)
                Supglobal.mLocation = Supglobal.mLocation.dropLast(1)
                Supglobal.mPeriod = Supglobal.mPeriod.dropLast(1)
                Supglobal.mMemo = Supglobal.mMemo.dropLast(1)
            }
        }
        /*------------------------------------------------------*/
        /*------------------------------------------------------*/


        return tmp
    }
}