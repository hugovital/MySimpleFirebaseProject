package com.hugo.mysimplefirebaseproject.domain.repository

import com.hugo.mysimplefirebaseproject.data.api.CatFactApi
import com.hugo.mysimplefirebaseproject.data.model.CatFact

class CatFactRepository(private val api: CatFactApi) {
    suspend fun getCatFact(): Result<CatFact> = api.getCatFact()
} 