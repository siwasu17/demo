package com.example.demo

import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@WebMvcTest(TaskController::class)
class TaskControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var taskRepository: TaskRepository

    @MockBean
    private lateinit var commandLineRunner: CommandLineRunner

    @Test
    fun index_保存されているタスクを全件表示すること() {
        val tasks = listOf(
                Task(id = 123, content = "hoge", done = false),
                Task(id = 234, content = "fuga", done = true)
        )
        Mockito.`when`(taskRepository.findAll())
                .thenReturn(tasks)

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(view().name("tasks/index"))
                .andExpect(model().attribute("tasks", tasks))
                .andExpect(content().string(Matchers.containsString("<span>hoge</span>")))
                .andExpect(content().string(Matchers.containsString("<s>fuga</s>")))
    }

    @Test
    fun create_ポストされた内容でタスクを新規作成すること() {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .param("content", "piyo"))
                .andExpect(redirectedUrl("/tasks"))

        Mockito.verify(taskRepository).create("piyo")
    }

    @Test
    fun edit_編集対象の情報が表示されていること(){
        val task = Task(id = 123, content = "hoge", done = false)
        Mockito.`when`(taskRepository.findById(task.id))
                .thenReturn(task)

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/${task.id}/edit"))
                .andExpect(view().name("tasks/edit"))
                .andExpect(xpath("""//*[@id="content"]/@value""").string("${task.content}"))
    }

}