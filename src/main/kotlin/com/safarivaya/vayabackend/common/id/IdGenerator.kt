package com.safarivaya.vayabackend.common.id

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdGenerator {
    fun next(): String = UUID.randomUUID().toString()
}