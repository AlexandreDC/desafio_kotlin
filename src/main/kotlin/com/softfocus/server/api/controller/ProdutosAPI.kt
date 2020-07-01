package com.softfocus.server.api.controller

import com.softfocus.server.api.model.Produto
import com.softfocus.server.api.repository.ProdutoRepository
import com.softfocus.server.api.service.ProdutoService
import com.softfocus.server.api.util.APIErrorResponse
import com.softfocus.server.api.util.APIException
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
@RequestMapping("/api/v1/produtos")
class ProdutosAPI(private val produtoService: ProdutoService) {


    @GetMapping
    fun findAll(): Iterable<Produto> = produtoService.findAll()

    @PostMapping("/adicionar-produto")
    fun createProduto(@Valid @RequestBody produto: Produto): ResponseEntity<Produto> {
        return ResponseEntity.ok(produtoService.create(produto))
    }

    @GetMapping("/{produto_codigo}")
    fun findByCodigo(@PathVariable(value = "produto_codigo") produto_codigo: Int): ResponseEntity<Produto> {

        return produtoService.findById(produto_codigo).map { produto ->
            ResponseEntity.ok(produto)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/{produto_codigo}")
    fun updateProduto(@PathVariable produto_codigo: Int,
                        @Valid @RequestBody produto: Produto): ResponseEntity<*> {
        return ResponseEntity.ok(produtoService.edit(produto_codigo, produto))
    }

    @DeleteMapping("/{produto_codigo}")
    fun deleteProduto(@PathVariable produto_codigo: Int): ResponseEntity<*> {
        produtoService.deleteById(produto_codigo)
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