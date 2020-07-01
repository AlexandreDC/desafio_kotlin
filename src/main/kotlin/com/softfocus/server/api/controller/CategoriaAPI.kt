package com.softfocus.server.api.controller

import com.softfocus.server.api.model.Categoria
import com.softfocus.server.api.service.CategoriaService
import com.softfocus.server.api.util.APIErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.function.Consumer
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/categorias")
class CategoriaAPI(private val categoriaService: CategoriaService) {


    @GetMapping
    fun findAll(): Iterable<Categoria> = categoriaService.findAll()

    @PostMapping("/adicionar-categoria")
    fun createCategoria(@Valid @RequestBody categoria: Categoria): ResponseEntity<Categoria> {
        return ResponseEntity.ok(categoriaService.create(categoria))
    }

    @GetMapping("/{categoria_codigo}")
    fun findByCodigo(@PathVariable(value = "categoria_codigo") categoria_codigo: Int): ResponseEntity<Categoria> {

        return categoriaService.findById(categoria_codigo).map { categoria ->
            ResponseEntity.ok(categoria)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/{categoria_codigo}")
    fun editCategoria(@PathVariable categoria_codigo: Int,
                        @Valid @RequestBody categoria: Categoria): ResponseEntity<*> {
        return ResponseEntity.ok(categoriaService.edit(categoria_codigo, categoria))
    }

    @DeleteMapping("/{categoria_codigo}")
    fun deleteCategoria(@PathVariable categoria_codigo: Int): ResponseEntity<*> {
        if (categoriaService.findById(categoria_codigo).isPresent) {
            ResponseEntity.badRequest().build<Any>()
        }
        categoriaService.deleteById(categoria_codigo)
        return ResponseEntity.ok().build<Any>()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
            ex: MethodArgumentNotValidException): APIErrorResponse {

        val aPIErrorResponse: APIErrorResponse = APIErrorResponse()


        aPIErrorResponse.status = HttpStatus.BAD_REQUEST.value()

        ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            aPIErrorResponse.message.add("$errorMessage")
        })

        return aPIErrorResponse


    }
}