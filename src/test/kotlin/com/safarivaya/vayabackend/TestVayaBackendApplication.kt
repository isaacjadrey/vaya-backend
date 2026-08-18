package com.safarivaya.vayabackend

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<VayaBackendApplication>().with(TestcontainersConfiguration::class).run(*args)
}
