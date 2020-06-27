/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softfocus.server.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty


/**
 *
 * @author alexa
 */
@Entity(name = "produtos")
@NamedQueries(NamedQuery(name = "Produto.findAll", query = "SELECT p FROM produtos p"),
        NamedQuery(name = "Produto.findByCodigo", query = "SELECT p FROM produtos p WHERE p.codigo = :codigo"),
        NamedQuery(name = "Produto.findByCategoria", query = "SELECT p FROM produtos p WHERE p.categoria = :categoria"),
        NamedQuery(name = "Produto.findByNome", query = "SELECT p FROM produtos p WHERE p.nome = :nome"),
        NamedQuery(name = "Produto.findProdutosTeste", query = "SELECT p FROM produtos p WHERE (p.nome like 'T- Teste de insercao%' or p.nome like 'T- Teste de alteraca%')"),
        NamedQuery(name = "Produto.findByDescricao", query = "SELECT p FROM produtos p WHERE p.descricao = :descricao"))
data class Produto (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    var codigo: Int = 0,


    @Column(length = 200)
    @get: NotBlank
    var nome: String = "",

    @Column(length = 4000)
    @get: NotBlank
    var descricao: String = "",


    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "categoria")
    @get: NotEmpty
    var categoria: Categoria? = null


)