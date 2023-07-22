package dev.hydroh.misskey.client.client

import dev.hydroh.misskey.client.entity.request.NotesReq
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class MisskeyClientTest {
    private lateinit var client: MisskeyClient

    @BeforeTest
    fun setUp() {
        client = MisskeyClient("cafe.otter.homes")
    }

    @Test
    fun globalTimelineTest() {
        runBlocking {
            val timeline = client.notes.globalTimeline(NotesReq.Timeline(limit = 10))
            print(timeline)
        }
    }
}