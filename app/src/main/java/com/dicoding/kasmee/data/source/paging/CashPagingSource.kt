package com.dicoding.kasmee.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.source.remote.api.ApiService
import com.dicoding.kasmee.util.Constants
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CashPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Cash>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cash> {
        val position = params.key ?: Constants.CASH_STARTING_PAGE_INDEX

        return try {
            val response = apiService.getAllCash(position)
            val responseBody = response.body()
            val cash: List<Cash> = responseBody?.data?.listCash!!

            val prevPage = if (position == Constants.CASH_STARTING_PAGE_INDEX) null else position - 1
            val nextPage = if (cash.isEmpty()) null else position + 1

            LoadResult.Page(
                data = cash,
                prevKey = prevPage,
                nextKey = nextPage
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Cash>): Int? {
        return state.anchorPosition
    }
}