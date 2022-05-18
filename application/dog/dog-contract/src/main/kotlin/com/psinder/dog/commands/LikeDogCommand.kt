package com.psinder.dog.commands

import com.psinder.dog.DogContext
import com.psinder.dog.DogProfileDto
import com.trendyol.kediatr.Command
import com.trendyol.kediatr.CommandMetadata
import org.litote.kmongo.Id

data class LikeDogCommand(
    val dogContext: DogContext, val targetDogId: Id<DogProfileDto>,
    override val metadata: CommandMetadata? = null
) : Command
