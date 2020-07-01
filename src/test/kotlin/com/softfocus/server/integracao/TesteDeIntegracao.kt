/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softfocus.server.integracao

import com.fasterxml.jackson.databind.ObjectMapper
import com.softfocus.server.api.model.Categoria
import com.softfocus.server.api.model.Produto
import com.softfocus.server.api.repository.CategoriaRepository
import com.softfocus.server.api.repository.ProdutoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * @author alexa
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation::class)
internal class TesteDeIntegracao {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@Autowired
	lateinit var produtoRepository: ProdutoRepository

	@Autowired
	lateinit var categoriaRepository: CategoriaRepository

	@Test
	@Order(1)
	@Throws(Exception::class)
	fun testeDeInsercaoCategoriaViaAPI() {
		val categoriaTestada = Categoria()
		val nomeAleatorio = ("T- Teste de insercao"
				+ SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS").format(Date()).substring(0, 20)
				+ Random().ints(97, 123)
				.limit(10)
				.collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
				.toString())


		assertThat(nomeAleatorio.length).isEqualTo(50)
		categoriaTestada.nome  = nomeAleatorio


		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categorias/adicionar-categoria")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(categoriaTestada)))
				.andExpect(MockMvcResultMatchers.status().isOk)

		val categoriasSalvas = categoriaRepository.findByNome(categoriaTestada.nome)

		assertThat(!categoriasSalvas.any())
		assertThat(categoriasSalvas.count()).isEqualTo(1)
		assertThat(categoriasSalvas.first().nome).isEqualTo(categoriaTestada.nome)
	}

	@Test
	@Order(2)
	@Throws(Exception::class)
	fun testeDeConsultaCategoriaViaAPI() {
		val listaCategorias = categoriaRepository.findCategoriasTeste()
		val categoriaTestada: Categoria

		assertThat(!listaCategorias.any())
		categoriaTestada = listaCategorias.last()

		mockMvc.perform(get("/api/v1/categorias/" + categoriaTestada.codigo.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(categoriaTestada.nome))
	}

	@Test
	@Order(3)
	@Throws(Exception::class)
	fun testeDeAlteracaoCategoriaViaAPI() {
		val listaCategorias = categoriaRepository.findCategoriasTeste()

		val categoriaTestada: Categoria

		val categoriaRecuperada: Optional<Categoria>

		val nomeAleatorio = ("T- Teste de insercao"
				+ SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS").format(Date()).substring(0, 20)
				+ Random().ints(97, 123)
				.limit(10)
				.collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
				.toString())
		assertThat(nomeAleatorio.length).isEqualTo(50)
		assertThat(!listaCategorias.any())
		categoriaTestada = listaCategorias.last()

		categoriaTestada.nome = nomeAleatorio


		mockMvc.perform(put("/api/v1/categorias/" + categoriaTestada.codigo.toString())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(categoriaTestada)))
				.andExpect(MockMvcResultMatchers.status().isOk)
		categoriaRecuperada = categoriaRepository.findById(categoriaTestada.codigo)
		assertThat(!categoriaRecuperada.isEmpty)
		assertThat(categoriaRecuperada.get().nome).isEqualTo(categoriaTestada.nome)
	}

	@Test
	@Order(4)
	@Throws(Exception::class)
	fun testeDeInsercaoProdutoViaAPI() {

		val produtoTestado = Produto()
		val listaCategorias = categoriaRepository.findCategoriasTeste()
		assertThat(!listaCategorias.any())
		val descricaoAleatoria = (SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS").format(Date()).substring(0, 20)
				+ Random().ints(97, 123)
				.limit(4000 - 20.toLong()) //retira 20 da string inicial
				.collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
				.toString())
		val nomeAleatorio = ("T- Teste de insercao"
				+ Random().ints(97, 123)
				.limit(200 - 20.toLong()) //retira 20 da string inicial
				.collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
				.toString())
		assertThat(nomeAleatorio.length).isEqualTo(200)
		assertThat(descricaoAleatoria.length).isEqualTo(4000)
		produtoTestado.nome = nomeAleatorio
		produtoTestado.descricao = descricaoAleatoria
		produtoTestado.categoria = listaCategorias.last()
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/produtos/adicionar-produto")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(produtoTestado)))
				.andExpect(MockMvcResultMatchers.status().isOk)

		val produtosSalvos = produtoRepository.findByDescricao(descricaoAleatoria)


		assertThat(!produtosSalvos.any())
		val produtoSalvo = produtosSalvos.first()

		assertThat(produtoSalvo.nome).isEqualTo(produtoTestado.nome)
		assertThat(produtoSalvo.descricao).isEqualTo(produtoTestado.descricao)
		assertThat(produtoSalvo.categoria!!.codigo).isEqualTo(produtoTestado.categoria!!.codigo)
	}

	@Test
	@Order(5)
	@Throws(Exception::class)
	fun testeDeConsultaProdutoViaAPI() {
		val listaProdutos = produtoRepository.findProdutosTeste()
		val produtoTestado: Produto

		assertThat(!listaProdutos.any())
		produtoTestado = listaProdutos.last()
		mockMvc.perform(get("/api/v1/produtos/" + produtoTestado.codigo.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk)
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(produtoTestado.nome))
				.andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value(produtoTestado.descricao))
				.andExpect(MockMvcResultMatchers.jsonPath("$.categoria.nome").value(produtoTestado.categoria!!.nome))
	}

	@Test
	@Order(6)
	@Throws(Exception::class)
	fun testeDeAlteracaoProdutoViaAPI() {

		val produtoTestado: Produto
		val produtoRecuperado: Optional<Produto>
		val descricaoAleatoria = (SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS").format(Date()).substring(0, 20)
				+ Random().ints(97, 123)
				.limit(4000 - 20.toLong()) //retira 20 da string inicial
				.collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
				.toString())
		val nomeAleatorio = ("T- Teste de alteraca"
				+ Random().ints(97, 123)
				.limit(200 - 20.toLong()) //retira 20 da string inicial
				.collect({ StringBuilder() }, { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
				.toString())
		assertThat(nomeAleatorio.length).isEqualTo(200)
		assertThat(descricaoAleatoria.length).isEqualTo(4000)
		val listaProdutos = produtoRepository.findProdutosTeste()
		assertThat(!listaProdutos.any())
		produtoTestado = listaProdutos.last()
		produtoTestado.nome = nomeAleatorio
		produtoTestado.descricao = descricaoAleatoria
		produtoTestado.categoria = categoriaRepository.findAll().first()
		mockMvc.perform(put("/api/v1/produtos/" + produtoTestado.codigo.toString())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(produtoTestado)))
				.andExpect(MockMvcResultMatchers.status().isOk)
		produtoRecuperado = produtoRepository.findById(produtoTestado.codigo)
		assertThat(!produtoRecuperado.isEmpty)
		assertThat(produtoRecuperado.get().nome).isEqualTo(produtoTestado.nome)
		assertThat(produtoRecuperado.get().descricao).isEqualTo(produtoTestado.descricao)
		assertThat(produtoRecuperado.get().categoria!!.codigo).isEqualTo(produtoTestado.categoria!!.codigo)
	}


	@Test
	@Order(7)
	@Throws(Exception::class)
	fun testeDeExclusaoProdutoViaAPI() {
		val listaProdutos = produtoRepository.findProdutosTeste()
		var produtoRecuperado: Optional<Produto?>

		assertThat(!listaProdutos.any())
		for (produto in listaProdutos) {
			mockMvc.perform(delete("/api/v1/produtos/" + produto.codigo.toString())
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(produto)))
					.andExpect(MockMvcResultMatchers.status().isOk)
			produtoRecuperado = produtoRepository.findById(produto.codigo)
			assertThat(produtoRecuperado.isEmpty)
		}
	}

	@Test
	@Order(8)
	@Throws(Exception::class)
	fun testeDeExclusaoCategoriaViaAPI() {
		val listaCategoria = categoriaRepository.findCategoriasTeste()
		var categoriaRecuperada: Optional<Categoria?>
		assertThat(!listaCategoria.any())
		for (categoria in listaCategoria) {
			mockMvc.perform(delete("/api/v1/categorias/" + categoria.codigo.toString())
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(categoria)))
					.andExpect(MockMvcResultMatchers.status().isOk)
			categoriaRecuperada = categoriaRepository.findById(categoria.codigo)
			assertThat(categoriaRecuperada.isEmpty)
		}
	}
}