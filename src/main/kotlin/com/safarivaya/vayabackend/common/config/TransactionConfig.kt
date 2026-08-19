package com.safarivaya.vayabackend.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement(order = 0)
class TransactionConfig