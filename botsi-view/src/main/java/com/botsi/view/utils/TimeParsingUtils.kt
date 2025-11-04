package com.botsi.view.utils

import androidx.annotation.RestrictTo

/**
 * Utility object for parsing various time formats and converting them to milliseconds.
 * Supports flexible time formats including:
 * - HH:MM:SS (hours:minutes:seconds)
 * - MM:SS (minutes:seconds)
 * - SS (seconds only)
 * - Various separators (colon, dash, space, etc.)
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal object TimeParsingUtils {

    /**
     * Converts a time string to milliseconds.
     * Supports various formats:
     * - "10:00" -> 600000ms (10 minutes)
     * - "1:30:45" -> 5445000ms (1 hour 30 minutes 45 seconds)
     * - "45" -> 45000ms (45 seconds)
     * - "05:30" -> 330000ms (5 minutes 30 seconds)
     * 
     * @param timeString The time string to parse
     * @param separator The separator used in the time string (default: ":")
     * @return Time in milliseconds, or null if parsing fails
     */
    fun parseTimeToMilliseconds(timeString: String?, separator: String = ":"): Long? {
        if (timeString.isNullOrBlank()) return null
        
        return try {
            // Clean the input string - remove extra whitespace and handle different separators
            val cleanedTime = timeString.trim()
            
            // Handle different separator types
            val normalizedTime = when {
                separator == " " -> cleanedTime
                separator == ":" -> cleanedTime.replace(Regex("[:\\-\\s]+"), ":")
                separator == "-" -> cleanedTime.replace(Regex("[:\\-\\s]+"), "-")
                else -> cleanedTime.replace(Regex("[:\\-\\s]+"), separator)
            }
            
            // Split by the separator
            val parts = normalizedTime.split(separator).map { it.trim() }
            
            when (parts.size) {
                1 -> {
                    // Only seconds: "45" -> 45 seconds
                    val seconds = parts[0].toIntOrNull() ?: return null
                    if (seconds < 0) return null
                    seconds * 1000L
                }
                2 -> {
                    // Minutes and seconds: "10:30" -> 10 minutes 30 seconds
                    val minutes = parts[0].toIntOrNull() ?: return null
                    val seconds = parts[1].toIntOrNull() ?: return null
                    if (minutes < 0 || seconds < 0 || seconds >= 60) return null
                    (minutes * 60 + seconds) * 1000L
                }
                3 -> {
                    // Hours, minutes, and seconds: "1:30:45" -> 1 hour 30 minutes 45 seconds
                    val hours = parts[0].toIntOrNull() ?: return null
                    val minutes = parts[1].toIntOrNull() ?: return null
                    val seconds = parts[2].toIntOrNull() ?: return null
                    if (hours < 0 || minutes < 0 || seconds < 0 || minutes >= 60 || seconds >= 60) return null
                    (hours * 3600 + minutes * 60 + seconds) * 1000L
                }
                4 -> {
                    // Days, hours, minutes, and seconds: "1:12:30:45" -> 1 day 12 hours 30 minutes 45 seconds
                    val days = parts[0].toIntOrNull() ?: return null
                    val hours = parts[1].toIntOrNull() ?: return null
                    val minutes = parts[2].toIntOrNull() ?: return null
                    val seconds = parts[3].toIntOrNull() ?: return null
                    if (days < 0 || hours < 0 || minutes < 0 || seconds < 0 || 
                        hours >= 24 || minutes >= 60 || seconds >= 60) return null
                    (days * 86400 + hours * 3600 + minutes * 60 + seconds) * 1000L
                }
                else -> null // Unsupported format
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parses time string with automatic separator detection.
     * Tries to detect the separator automatically from common patterns.
     * 
     * @param timeString The time string to parse
     * @return Time in milliseconds, or null if parsing fails
     */
    fun parseTimeToMillisecondsAuto(timeString: String?): Long? {
        if (timeString.isNullOrBlank()) return null
        
        val cleanedTime = timeString.trim()
        
        // Try to detect separator
        val separator = when {
            cleanedTime.contains(":") -> ":"
            cleanedTime.contains("-") -> "-"
            cleanedTime.contains(" ") -> " "
            else -> {
                // If no separator found, assume it's just seconds
                return cleanedTime.toIntOrNull()?.let { 
                    if (it >= 0) it * 1000L else null 
                }
            }
        }
        
        return parseTimeToMilliseconds(cleanedTime, separator)
    }

    /**
     * Parses time string handling letter separators like "1H 30M 45S".
     * 
     * @param timeString The time string with letter separators
     * @return Time in milliseconds, or null if parsing fails
     */
    fun parseTimeWithLetterSeparators(timeString: String?): Long? {
        if (timeString.isNullOrBlank()) return null
        
        return try {
            val cleanedTime = timeString.trim().uppercase()
            var totalMilliseconds = 0L
            
            // Extract days
            val dayPattern = Regex("""(\d+)D""")
            dayPattern.find(cleanedTime)?.let { match ->
                val days = match.groupValues[1].toInt()
                totalMilliseconds += days * 86400 * 1000L
            }
            
            // Extract hours
            val hourPattern = Regex("""(\d+)H""")
            hourPattern.find(cleanedTime)?.let { match ->
                val hours = match.groupValues[1].toInt()
                totalMilliseconds += hours * 3600 * 1000L
            }
            
            // Extract minutes
            val minutePattern = Regex("""(\d+)M""")
            minutePattern.find(cleanedTime)?.let { match ->
                val minutes = match.groupValues[1].toInt()
                totalMilliseconds += minutes * 60 * 1000L
            }
            
            // Extract seconds
            val secondPattern = Regex("""(\d+)S""")
            secondPattern.find(cleanedTime)?.let { match ->
                val seconds = match.groupValues[1].toInt()
                totalMilliseconds += seconds * 1000L
            }
            
            if (totalMilliseconds > 0) totalMilliseconds else null
        } catch (e: Exception) {
            null
        }
    }
}