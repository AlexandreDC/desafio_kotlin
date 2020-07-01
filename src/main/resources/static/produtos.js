var produtos = [];
var categorias = [];

function findProduto(codigoProduto) {
    return produtos[findCodigoProduto(codigoProduto)];
}

function findCodigoProduto(codigoProduto) {
    for (var cod = 0; cod < produtos.length; cod++) {
        if (produtos[cod].codigo == codigoProduto) {
            return cod;
        }
    }
}


var categoriaRepository = {
    findAll(fn, err) {

        axios
            .get('/api/v1/categorias')
            .then(response => fn(response))
            .catch(error => err(error))

    }
}

var produtoRepository = {
    findAll(fn, err) {
        axios
            .get('/api/v1/produtos')
            .then(response => fn(response))
            .catch(error => err(error))
    },

    findByCodigo(produto_codigo, fn, err) {
        axios
            .get('/api/v1/produtos/' + produto_codigo)
            .then(response => fn(response))
            .catch(error => err(error))
    },

    create(produto, fn, err) {
        axios
            .post('/api/v1/produtos/adicionar-produto', produto)
            .then(response => fn(response))
            .catch(error => err(error))
    },

    update(produto_codigo, produto, fn, err) {
        axios
            .put('/api/v1/produtos/' + produto_codigo, produto)
            .then(response => fn(response))
            .catch(error => err(error))
    },

    deleteProduto(produto_codigo, fn, err) {
        axios
            .delete('/api/v1/produtos/' + produto_codigo)
            .then(response => fn(response))
            .catch(error => err(error))
    }
}

var List = Vue.extend({
    template: '#lista-produtos',
    data: function () {
        return {
            produtos: [],
            searchKey: '',
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        };
    },
    computed: {
        filteredProdutos() {
            return this.produtos.filter((produto) => {
                return produto.nome.indexOf(this.searchKey) > -1
                    || produto.descricao.indexOf(this.searchKey) > -1
                    || produto.categoria.toString().indexOf(this.searchKey) > -1
            }).sort((a, b) => (a.codigo > b.codigo) ? 1 : ((b.codigo > a.codigo) ? -1 : 0))
        }


    },
    mounted() {
        produtoRepository.findAll(
            r => {
                this.produtos = r.data;
                produtos = r.data
            },
            r => {
                this.validacao.status = r.response.data.status
                this.validacao.message = r.response.data.message
                this.validacao.timeStamp = r.response.data.timeStamp

                setTimeout(() => {  this.validacao.message = [] }, 3000);
            })
    }
});


var VerProduto = Vue.extend({
    template: '#produto',
    data: function () {
        let dados = {
            produto: {
                codigo: 'não encontrado',
                nome: '',
                descricao: '',
                categoria: ""

            },
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }


        if (produtos.length === 0) {
            produtoRepository.findByCodigo(this.$route.params.produto_codigo,
                r => {
                    this.produto = r.data;
                    dados.produto = r.data;
                },
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });


        } else {
            dados.produto = findProduto(this.$route.params.produto_codigo);

        }
        return (dados);

    }
});

var ProdutoEdit = Vue.extend({
    template: '#produto-edit',
    data: function () {
        let dados = {
            produto: {
                codigo: 'não encontrado',
                nome: '',
                descricao: '',
                categoria: ""},
            categorias: [],
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }
        console.log('recuperando o produto: ' + this.$route.params.produto_codigo);

        if (produtos.length === 0) {

            produtoRepository.findByCodigo(this.$route.params.produto_codigo,
                r => {
                    this.produto = r.data;
                    dados.produto = r.data;
                },
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });
        } else {
            dados.produto = findProduto(this.$route.params.produto_codigo);

        }
        return (dados);

    },
    methods: {
        updateProduto: function () {
            produtoRepository.update(this.produto.codigo, this.produto,
                r => router.push('/'),
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                })
        }
    },
    mounted() {
        categoriaRepository.findAll(
            r => {
                this.categorias = r.data;
                categorias = r.data;
            },
            r => {
                this.validacao.status = r.response.data.status
                this.validacao.message = r.response.data.message
                this.validacao.timeStamp = r.response.data.timeStamp

                setTimeout(() => {  this.validacao.message = [] }, 3000);
            });
        console.log(this.categorias);
    }

});

var ProdutoDelete = Vue.extend({
    template: '#produto-delete',
    data: function () {
        let dados = {
            produto: {
                codigo: 'não encontrado',
                nome: '',
                descricao: '',
                categoria: ""},
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }

        if (produtos.length === 0) {
            console.log('recuperando todos produtos');
            produtoRepository.findByCodigo(this.$route.params.produto_codigo,
                r => {
                    this.produto = r.data;
                    dados.produto = r.data;


                },
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });

        } else {
            dados.produto = findProduto(this.$route.params.produto_codigo);

        }
        return (dados);

    },
    methods: {
        deleteProduto: function () {
            produtoRepository.deleteProduto(this.produto.codigo,
                r => router.push('/'),
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                })
        }
    }
});

var AddProduto = Vue.extend({
    template: '#novo-produto',

    data: function () {
        return {
            produto: {
                codigo: 0,
                nome: '',
                descricao: '',
                categoria: ''
            },
            categorias: [],
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }
    },
    methods: {
        createProduto() {
            produtoRepository.create(this.produto,
                r => router.push('/'),
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });
        }
    },
    mounted() {
        categoriaRepository.findAll(
            r => {
                this.categorias = r.data;
                categorias = r.data;
            },
            r => {
                this.validacao.status = r.response.data.status
                this.validacao.message = r.response.data.message
                this.validacao.timeStamp = r.response.data.timeStamp

                setTimeout(() => {  this.validacao.message = [] }, 3000);
            });
    }
});

var router = new VueRouter({
    routes: [
        {path: '/', component: List},
        {path: '/:produto_codigo', component: VerProduto, name: 'produto'},
        {path: '/produto/novo', component: AddProduto, name: 'novo-produto'},
        {path: '/:produto_codigo/edit', component: ProdutoEdit, name: 'produto-edit'},
        {path: '/:produto_codigo/delete', component: ProdutoDelete, name: 'produto-delete'}
    ]
});

new Vue({
    router
}).$mount('#app')