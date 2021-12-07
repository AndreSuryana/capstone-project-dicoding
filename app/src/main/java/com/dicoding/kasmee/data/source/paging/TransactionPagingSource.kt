package com.dicoding.kasmee.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.data.source.remote.api.ApiService
import com.dicoding.kasmee.util.Constants
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TransactionPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Transaction>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        val position = params.key ?: Constants.TRANSACTION_STARTING_PAGE_INDEX

        return try {
            val response = apiService.getAllTransaction(position)
            val responseBody = response.body()
            val transaction = responseBody?.data?.listTransaction!!

            val prevPage =
                if (position == Constants.TRANSACTION_STARTING_PAGE_INDEX) null else position - 1
            val nextPage =
                if (transaction.isEmpty()) null else position + 1

            LoadResult.Page(
                data = transaction,
                prevKey = prevPage,
                nextKey = nextPage
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition
    }
}