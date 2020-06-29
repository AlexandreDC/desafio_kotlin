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
    findAll(fn) {
        axios
                .get('/api/v1/categorias')
                .then(response => fn(response))
                .catch(error => console.log(error))
    }
}

var produtoRepository = {
    findAll(fn) {
        axios
                .get('/api/v1/produtos')
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    findByCodigo(produto_codigo, fn) {
        axios
                .get('/api/v1/produtos/' + produto_codigo)
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    create(produto, fn) {
        axios
                .post('/api/v1/produtos/adicionar-produto', produto)
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    update(produto_codigo, produto, fn) {
        axios
                .put('/api/v1/produtos/' + produto_codigo, produto)
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    deleteProduto(produto_codigo, fn) {
        axios
                .delete('/api/v1/produtos/' + produto_codigo)
                .then(response => fn(response))
                .catch(error => console.log(error))
    }
}

var List = Vue.extend({
    template: '#lista-produtos',
    data: function () {
        return {produtos: [], searchKey: ''};
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
        produtoRepository.findAll(r => {
            this.produtos = r.data;
            produtos = r.data
        })
    }
});



var VerProduto = Vue.extend({
    template: '#produto',
    data: function () {


        console.log('recuperando o produto: ' + this.$route.params.produto_codigo);

        if (produtos.length === 0) {
            produtoRepository.findByCodigo(this.$route.params.produto_codigo, r => {
                this.produto = r.data;
            });
            return({produto: this.produto});
        } else {
            return {produto: findProduto(this.$route.params.produto_codigo)};

        }

    }
});

var ProdutoEdit = Vue.extend({
    template: '#produto-edit',
    data: function () {
        console.log('recuperando o produto: ' + this.$route.params.produto_codigo);

        if (produtos.length === 0) {
            console.log('recuperando da API');
            produtoRepository.findByCodigo(this.$route.params.produto_codigo, r => {
                this.produto = r.data;
            });
            return({produto: this.produto, categorias: []});
        } else {
            return {produto: findProduto(this.$route.params.produto_codigo), categorias: []};

        }

    },
    methods: {
        updateProduto: function () {
            produtoRepository.update(this.produto.codigo, this.produto, r => router.push('/'))
        }
    },
    mounted() {
        categoriaRepository.findAll(r => {
                this.categorias = r.data;
                categorias = r.data;
            });
        console.log(this.categorias);
    }
});

var ProdutoDelete = Vue.extend({
    template: '#produto-delete',
    data: function () {
        console.log('recuperando o produto: ' + this.$route.params.produto_codigo);

        if (produtos.length === 0) {
            console.log('recuperando todos produtos');
            produtoRepository.findByCodigo(this.$route.params.produto_codigo, r => {
                this.produto = r.data;
                console.log("oooooo", this.produto);


            });
            return({produto: this.produto});
        } else {
            return {produto: findProduto(this.$route.params.produto_codigo)};

        }

    },
    methods: {
        deleteProduto: function () {
            produtoRepository.deleteProduto(this.produto.codigo, r => router.push('/'))
        }
    }
});

var AddProduto = Vue.extend({
    template: '#novo-produto',

    data: function () {
        return {produto: {nome: '', descricao: '', categoria: ""}, categorias: []}
    },
    methods: {
        createProduto() {
            produtoRepository.create(this.produto, r => router.push('/'));
        }
    },
    mounted() {
        categoriaRepository.findAll(r => {
                this.categorias = r.data;
                categorias = r.data;
            });
        console.log(this.categorias);
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