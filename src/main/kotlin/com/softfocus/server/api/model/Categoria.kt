/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softfocus.server.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 *
 * @author alexa
 */
@Entity(name = "categorias")
@NamedQueries(NamedQuery(name = "Categoria.findAll", query = "SELECT p FROM categorias p"),
        NamedQuery(name = "Categoria.findByCodigo", query = "SELECT p FROM categorias p WHERE p.codigo = :codigo"),
        NamedQuery(name = "Categoria.findCategoriasTeste", query = "SELECT p FROM categorias p WHERE (p.nome like 'T- Teste de insercao%' or p.nome like 'T- Teste de alteraca%')"),
        NamedQuery(name = "Categoria.findByNome", query = "SELECT p FROM categorias p WHERE p.nome = :nome"))
data class Categoria (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    val  codigo: Int = 0,

    @get: Size(max = 50, message = "Tamanho do nome não pode exceder 50 caracteres")
    @get: NotBlank(message = "Nome não pode ser vazio")
    var nome: String = "",

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", fetch = FetchType.EAGER)
    var produtos: List<Produto>? = null


)