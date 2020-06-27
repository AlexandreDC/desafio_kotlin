package com.softfocus.server.api

import com.softfocus.server.api.repository.ProdutoRepository
import com.softfocus.server.model.Produto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/produtos")
class ProdutosAPI(private val produtoRepository: ProdutoRepository) {


    @GetMapping
    fun findAll(): List<Produto> = produtoRepository.findAll()

    @PostMapping("/adicionar-produto")
    fun createProduto(@Valid @RequestBody produto: Produto): ResponseEntity<Produto> {
        return ResponseEntity.ok(produtoRepository.save(produto))
    }

    @GetMapping("/{produto_codigo}")
    fun findByCodigo(@PathVariable(value = "produto_codigo") produto_codigo: Int): ResponseEntity<Produto> {

        return produtoRepository.findById(produto_codigo).map { produto ->
            ResponseEntity.ok(produto)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/{produto_codigo}")
    fun updateProduto(@PathVariable produto_codigo: Int,
                        @Valid @RequestBody produto: Produto): ResponseEntity<*> {
        if (!produtoRepository.findById(produto_codigo).isPresent) {
            ResponseEntity.badRequest().build<Any>()
        }
        return ResponseEntity.ok(produtoRepository.save(produto))
    }

    @DeleteMapping("/{produto_codigo}")
    fun deleteProduto(@PathVariable produto_codigo: Int): ResponseEntity<*> {
        if (produtoRepository.findById(produto_codigo).isPresent) {
            ResponseEntity.badRequest().build<Any>()
        }
        produtoRepository.deleteById(produto_codigo)
        return ResponseEntity.ok().build<Any>()
    }
}