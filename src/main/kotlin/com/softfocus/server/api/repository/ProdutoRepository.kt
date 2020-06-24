/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softfocus.server.api.repository

import com.softfocus.server.model.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository : JpaRepository<Produto, Int> {
    fun findByNome(nome: String): List<Produto>
    fun findByDescricao(descricao: String): List<Produto>
    fun findProdutosTeste(): List<Produto>
}