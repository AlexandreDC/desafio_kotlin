/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softfocus.server.api.model


import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


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


    @get: Size(max = 200, message = "Tamanho do nome não pode exceder 200 caracteres")
    @get: NotBlank(message = "Nome não pode ser vazio")
    var nome: String = "",

    @get: Size(max = 4000, message = "Tamanho da descrição não pode exceder 4000 caracteres")
    @get: NotBlank(message = "Descrição não pode ser vazia")
    var descricao: String = "",

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "categoria")
    @get: NotNull(message = "Categoria não informada")
    var categoria: Categoria? = null


)