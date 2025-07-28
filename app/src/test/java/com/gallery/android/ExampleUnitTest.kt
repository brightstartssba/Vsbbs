package com.gallery.android

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun mediaType_values_isCorrect() {
        val mediaTypes = com.gallery.android.model.MediaType.values()
        assertEquals(3, mediaTypes.size)
        assertTrue(mediaTypes.contains(com.gallery.android.model.MediaType.IMAGE))
        assertTrue(mediaTypes.contains(com.gallery.android.model.MediaType.VIDEO))
        assertTrue(mediaTypes.contains(com.gallery.android.model.MediaType.ALL))
    }
}