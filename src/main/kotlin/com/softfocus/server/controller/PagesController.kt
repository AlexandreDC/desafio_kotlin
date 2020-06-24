package com.softfocus.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class PagesController {
    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/requisitos")
    fun requisitos(): String {
        return "requisitos"
    }

    @GetMapping("/produtos")
    fun paginaprodutos(): String {
        return "produtos"
    }

    @GetMapping("/categorias")
    fun paginacategorias(): String {
        return "categorias"
    }
}
