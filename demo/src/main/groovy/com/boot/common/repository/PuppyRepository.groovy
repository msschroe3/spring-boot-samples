package com.boot.common.repository

import com.boot.common.domain.Puppy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PuppyRepository extends JpaRepository<Puppy, Long>, JpaSpecificationExecutor<Puppy> {}