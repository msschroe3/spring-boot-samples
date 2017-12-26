package com.boot.common.service

import com.boot.common.dto.PuppyResponse
import com.boot.common.dto.PuppySearch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PuppyService {
    Page<PuppyResponse> findPuppies(PuppySearch search, Pageable pageable)
}
