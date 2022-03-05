package me.visutrb.resumegen.db.converter

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class DateConverterTest {

    private lateinit var converter: DateConverter

    @Before
    fun setup() {
        converter = DateConverter()
    }

    @Test
    fun testConvertFromTimestamp() {
        val date1 = converter.fromTimestamp(0)
        Assert.assertNotNull(date1)
        Assert.assertEquals(0L, date1?.time)

        val date2 = converter.fromTimestamp(null)
        Assert.assertNull(date2)
    }

    @Test
    fun testConvertToTimestamp() {
        val timestamp1 = converter.toTimestamp(Date(0))
        Assert.assertNotNull(timestamp1)
        Assert.assertEquals(0L, timestamp1)

        val timestamp2 = converter.toTimestamp(null)
        Assert.assertNull(timestamp2)
    }
}