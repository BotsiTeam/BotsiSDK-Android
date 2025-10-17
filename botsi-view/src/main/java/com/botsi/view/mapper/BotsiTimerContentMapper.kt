package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiTimerContent
import com.botsi.view.model.content.BotsiTimerFormat
import com.botsi.view.model.content.BotsiTimerMode
import com.botsi.view.model.content.BotsiTimerSeparator
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


internal class BotsiTimerContentMapper(
    private val textMapper: BotsiTextMapper,
) {
    suspend fun map(json: JsonElement): BotsiTimerContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                val format = runCatching {
                    BotsiTimerFormat.findByFormat(get("format").asString)
                }.getOrNull()

                val separator = runCatching {
                    BotsiTimerSeparator.valueOf(get("separator").toCapitalizedString())
                }.getOrNull()

                val isSeparatorLetter = separator == BotsiTimerSeparator.Letter

                val startText = runCatching { get("start_text").asString }.getOrNull().orEmpty()
                    .run {
                        if (isSeparatorLetter) {
                            filter { it.isDigit() || it.isWhitespace() }
                        } else {
                            this
                        }
                    }
                val noJavaFormatSupportSeparatorValues = if (isSeparatorLetter) {
                    val startTextItemsCount = startText.split(" ").count()
                    val splitSeparator = separator.symbol.split(" ")
                    val separatorSliceStart = splitSeparator.count() - startTextItemsCount
                    splitSeparator.slice(separatorSliceStart..splitSeparator.lastIndex)
                } else {
                    null
                }

                val correctFormat = format?.format?.replace("h", "H")
                val pattern = if (isSeparatorLetter) {
                    correctFormat.orEmpty()
                } else {
                    correctFormat.orEmpty().replace(" ", separator?.symbol.orEmpty())
                }
                val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val date: Date? = runCatching { dateFormat.parse(startText) }.getOrNull()

                BotsiTimerContent(
                    dateFormat = dateFormat,
                    startTime = date?.time,
                    noJavaFormatSupportSeparatorValues = noJavaFormatSupportSeparatorValues,
                    beforeText = runCatching { get("before_text").asString }.getOrNull(),
                    afterText = runCatching { get("after_text").asString }.getOrNull(),
                    beforeTextFallback = runCatching { get("before_text_fallback").asString }.getOrNull(),
                    afterTextFallback = runCatching { get("after_text_fallback").asString }.getOrNull(),
                    style = runCatching { textMapper.map(get("style")) }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    timerMode = runCatching { BotsiTimerMode.findByKey(get("timer_mode").asString) }.getOrNull(),
                    triggerCustomAction = runCatching { get("trigger_custom_action").asBoolean }.getOrNull(),
                    customActionId = runCatching { get("custom_action_id").asString }.getOrNull(),
                    timerId = runCatching { get("timer_id").asString }.getOrNull(),
                )
            }
        }
    }
}