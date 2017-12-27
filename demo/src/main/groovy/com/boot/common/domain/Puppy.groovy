package com.boot.common.domain

import com.boot.common.dto.PuppyResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotNull
import java.sql.Date
import java.time.ZonedDateTime

import static com.boot.common.converter.Converter.copyProperties

@Entity
class Puppy {
    @Id
    @GeneratedValue
    Long id

    @NotNull
    String name

    @NotNull
    Double cost

    ZonedDateTime birthday

    // auditing fields (not copied to PuppyResponse)
    @CreatedDate
    Date createdDate
    @LastModifiedDate
    Date modifiedDate

    PuppyResponse toPuppyResponse() {
        copyProperties(new PuppyResponse(), this) as PuppyResponse
    }
}
