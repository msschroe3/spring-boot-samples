package com.boot.common.service

import com.boot.common.domain.Puppy
import com.boot.common.dto.PuppyResponse
import com.boot.common.dto.PuppySearch
import com.boot.common.repository.PuppyRepository
import com.boot.common.search.SpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service

@Service
class PuppyServiceImpl implements PuppyService {
    @Autowired
    PuppyRepository repository

    @Autowired
    SpecificationFactory specificationFactory

    @Override
    Page<PuppyResponse> findPuppies(PuppySearch search, Pageable pageable) {
        Specifications specs = specificationFactory.buildSpecifications(search)

        Page<Puppy> puppies = repository.findAll(specs, pageable)

        puppies.map { it.toPuppyResponse() }
    }
}
