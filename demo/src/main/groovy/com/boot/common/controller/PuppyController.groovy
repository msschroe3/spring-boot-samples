package com.boot.common.controller

import com.boot.common.dto.PuppyResponse
import com.boot.common.dto.PuppySearch
import com.boot.common.service.PuppyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = '/api/v1/puppies')
class PuppyController {
    @Autowired
    PuppyService puppyService

    @GetMapping
    Page<PuppyResponse> findPuppies(PuppySearch search,
                                    @PageableDefault Pageable pageable) {
        puppyService.findPuppies(search, pageable)
    }
}
