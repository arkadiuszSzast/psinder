package com.psinder.mail

fun MailDto.toDomain() = Mail(id.cast(), subject, from, to, templateId, variables)

fun Mail.toDto() = MailDto(id.cast(), subject, from, to, templateId, variables)
