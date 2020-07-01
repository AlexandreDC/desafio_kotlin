package com.softfocus.server.api.service

import com.softfocus.server.api.model.Categoria
import com.softfocus.server.api.repository.CategoriaRepository
import com.softfocus.server.api.util.APIException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoriaService(private val categoriaRepository: CategoriaRepository) {

    fun findAll(): Iterable<Categoria> {
        return categoriaRepository.findAll()
    }

    fun findById(id: Int): Optional<Categoria> {
        return categoriaRepository.findById(id)
    }

    fun create(categoria: Categoria): Categoria {

        if (categoria.codigo != 0) {
            throw APIException("Categoria inserida não pode ter código. Informado: " + categoria.codigo);
        }
        return categoriaRepository.save(categoria)
    }

    fun edit(categoriaID: Int, categoria: Categoria): Categoria {
        if (!categoriaRepository.existsById(categoriaID)) {
            throw APIException("Categoria não existe : " + categoriaID);
        }
        if (categoriaRepository.findById(categoriaID).get().codigo != categoria.codigo) {
            throw APIException("Categoria editada: " + categoria.codigo + " é diferente da requisitada: " + categoriaID);
        }


        return categoriaRepository.save(categoria)
    }

    fun deleteById(categoriaID: Int) {
        if (categoriaID == 0) {
            throw APIException("Informe um código de categoria");
        }
        if (!categoriaRepository.existsById(categoriaID)) {
            throw APIException("Categoria não existe : " + categoriaID);
        }

        if (categoriaRepository.findById(categoriaID).get().produtos!!.isNotEmpty()) {
            throw APIException("Existem produtos vinculados a esta categoria, não será permitido excluir");
        }

        categoriaRepository.deleteById(categoriaID)
    }
}