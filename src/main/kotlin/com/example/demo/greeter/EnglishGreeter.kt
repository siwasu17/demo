package com.example.demo.greeter


class EnglishGreeter: Greeter {
    override fun hello(name: String): String = "Hello, $name !"
}