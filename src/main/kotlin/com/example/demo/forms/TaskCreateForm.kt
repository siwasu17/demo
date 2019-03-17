package com.example.demo.forms

import org.hibernate.validator.constraints.NotBlank
import javax.validation.constraints.Size

class TaskCreateForm{
    @NotBlank
    @Size(max = 20)
    var content: String? = null
}