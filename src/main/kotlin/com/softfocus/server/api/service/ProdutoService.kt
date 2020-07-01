package com.softfocus.server.api.service

import com.softfocus.server.api.util.APIException
import com.softfocus.server.api.model.Produto
import com.softfocus.server.api.repository.ProdutoRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProdutoService(private val produtoRepository: ProdutoRepository) {

    fun findAll(): Iterable<Produto> {
        return produtoRepository.findAll()
    }

    fun findById(id: Int): Optional<Produto> {
        return produtoRepository.findById(id)
    }

    fun create(produto: Produto): Produto {

        if (produto.codigo != 0) {
            throw APIException("Produto inserido não pode ter código. Informado: " + produto.codigo);
        }
        return produtoRepository.save(produto)
    }

    fun edit(produtoID: Int, produto: Produto): Produto {
        if (!produtoRepository.existsById(produtoID)) {
            throw APIException("Produto não existe : " + produtoID);
        }
        if (produtoRepository.findById(produtoID).get().codigo != produto.codigo) {
            throw APIException("Produto editado: " + produto.codigo + " é diferente da requisitada: " + produtoID);
        }


        return produtoRepository.save(produto)
    }

    fun deleteById(produtoID: Int) {
        if (produtoID == 0) {
            throw APIException("Informe um código de produto");
        }
        if (!produtoRepository.existsById(produtoID)) {
            throw APIException("Produto não existe : " + produtoID);
        }
        produtoRepository.deleteById(produtoID)
    }
}