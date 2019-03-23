package com.example.demo.controllers

import com.example.demo.forms.TaskCreateForm
import com.example.demo.repository.TaskRepository
import com.example.demo.forms.TaskUpdateForm
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@Controller
//@RequestMapping("tasks")
class TaskController(private val taskRepository: TaskRepository) {

    @GetMapping("tasks")
//    @RequestMapping("tasks", method = [RequestMethod.GET])
    fun index(model: Model): String {
        println("INDEX")

        /*
        val tasks = listOf(
                Task(1, "ご飯を炊く", false),
                Task(2, "掃除する", true)
        )*/
        val tasks = taskRepository.findAll()
        model.addAttribute("tasks", tasks)

        return "tasks/index"
    }

    @GetMapping("new")
    fun new(form: TaskCreateForm): String {
        return "tasks/new"
    }

    @PostMapping("")
    fun create(@Validated form: TaskCreateForm,
               bindingResult: BindingResult): String {
        //validatedアノテーションによりバリデーションされて結果がbindingResultに入ってくる

        if (bindingResult.hasErrors())
            return "tasks/new"
        val content = requireNotNull(form.content)
        taskRepository.create(content)
        return "redirect:/tasks"
    }


    @GetMapping("{id}/edit")
    fun edit(@PathVariable("id") id: Long,
             form: TaskUpdateForm): String {
        val task = taskRepository.findById(id) ?: throw NotFoundException()
        form.content = task.content
        form.done = task.done
        return "tasks/edit"
    }

    @PatchMapping("{id}")
    fun update(@PathVariable("id") id: Long,
               @Validated form: TaskUpdateForm,
               bindingResult: BindingResult): String {
        if (bindingResult.hasErrors())
            return "tasks/edit"
        val task = taskRepository.findById(id) ?: throw NotFoundException()
        val newTask = task.copy(content = requireNotNull(form.content),
                done = form.done)
        taskRepository.update(newTask)
        return "redirect:/tasks"

    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(): String = "tasks/not_found"

    class NotFoundException : RuntimeException()
}