package com.softfocus.server.api

import com.softfocus.server.api.repository.CategoriaRepository
import com.softfocus.server.model.Categoria
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/categorias")
class CategoriaAPI(private val categoriaRepository: CategoriaRepository) {


    @GetMapping
    fun findAll(): List<Categoria> = categoriaRepository.findAll()

    @PostMapping("/adicionar-categoria")
    fun createCategoria(@Valid @RequestBody categoria: Categoria): ResponseEntity<Categoria> {
        return ResponseEntity.ok(categoriaRepository.save(categoria))
    }

    @GetMapping("/{categoria_codigo}")
    fun findByCodigo(@PathVariable(value = "categoria_codigo") categoria_codigo: Int): ResponseEntity<Categoria> {

        return categoriaRepository.findById(categoria_codigo).map { categoria ->
            ResponseEntity.ok(categoria)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/{categoria_codigo}")
    fun updateCategoria(@PathVariable categoria_codigo: Int,
                        @Valid @RequestBody categoria: Categoria): ResponseEntity<*> {
        if (!categoriaRepository.findById(categoria_codigo).isPresent) {
            ResponseEntity.badRequest().build<Any>()
        }
        return ResponseEntity.ok(categoriaRepository.save(categoria))
    }

    @DeleteMapping("/{categoria_codigo}")
    fun deleteCategoria(@PathVariable categoria_codigo: Int): ResponseEntity<*> {
        if (categoriaRepository.findById(categoria_codigo).isPresent) {
            ResponseEntity.badRequest().build<Any>()
        }
        categoriaRepository.deleteById(categoria_codigo)
        return ResponseEntity.ok().build<Any>()
    }
}