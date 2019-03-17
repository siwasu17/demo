package com.example.demo.greeter

import com.example.demo.greeter.Greeter
import org.springframework.stereotype.Component


@Component
class JapaneaseGreeter: Greeter {
    override fun hello(name: String): String = "こんにちは、$name !"
}