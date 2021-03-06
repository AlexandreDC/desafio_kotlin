/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softfocus.server.api.repository

import com.softfocus.server.api.model.Categoria
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoriaRepository : CrudRepository<Categoria, Int> {
    fun findByNome(nome: String): Iterable<Categoria>
    fun findCategoriasTeste(): Iterable<Categoria>
}