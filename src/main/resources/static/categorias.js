var categorias = [];

function findCategoria(codigoCategoria) {
    return categorias[findCodigoCategoria(codigoCategoria)];
}

function findCodigoCategoria(codigoCategoria) {
    for (var cod = 0; cod < categorias.length; cod++) {
        if (categorias[cod].codigo == codigoCategoria) {
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
    },

    findByCodigo(categoria_codigo, fn, err) {
        axios
            .get('/api/v1/categorias/' + categoria_codigo)
            .then(response => fn(response))
            .catch(error => err(error))
    },

    create(categoria, fn, err) {
        axios
            .post('/api/v1/categorias/adicionar-categoria', categoria)
            .then(response => fn(response))
            .catch(error => err(error))
    },

    update(categoria_codigo, categoria, fn, err) {
        axios
            .put('/api/v1/categorias/' + categoria_codigo, categoria)
            .then(response => fn(response))
            .catch(error => err(error))
    },

    deleteCategoria(categoria_codigo, fn, err) {
        axios
            .delete('/api/v1/categorias/' + categoria_codigo)
            .then(response => fn(response))
            .catch(error => err(error))
    }
}

var List = Vue.extend({
    template: '#lista-categorias',
    data: function () {
        return {
            categorias: [],
            searchKey: '',
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        };
    },
    computed: {
        filteredCategorias() {
            return this.categorias.filter((categoria) => {
                return categoria.nome.indexOf(this.searchKey) > -1
            }).sort((a, b) => (a.codigo > b.codigo) ? 1 : ((b.codigo > a.codigo) ? -1 : 0))
        }


    },
    mounted() {
        categoriaRepository.findAll(
            r => {
                this.categorias = r.data;
                categorias = r.data
            },
            r => {
                this.validacao.status = r.response.data.status
                this.validacao.message = r.response.data.message
                this.validacao.timeStamp = r.response.data.timeStamp

                setTimeout(() => {  this.validacao.message = [] }, 3000);
            })
    }
});


var VerCategoria = Vue.extend({
    template: '#categoria',
    data: function () {
        let dados = {
            categoria: {
                codigo: 'não encontrada',
                nome: ''

            },
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }


        if (categorias.length === 0) {
            categoriaRepository.findByCodigo(this.$route.params.categoria_codigo,
                r => {
                    this.categoria = r.data;
                    dados.categoria = r.data;
                },
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });


        } else {
            dados.categoria = findCategoria(this.$route.params.categoria_codigo);

        }
        return (dados);

    }
});

var CategoriaEdit = Vue.extend({
    template: '#categoria-edit',
    data: function () {
        let dados = {
            categoria: {
                codigo: 'não encontrada',
                nome: ''
            },
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }


        if (categorias.length === 0) {

            categoriaRepository.findByCodigo(this.$route.params.categoria_codigo,
                r => {
                    this.categoria = r.data;
                    dados.categoria = r.data;
                },
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });
        } else {
            dados.categoria = findCategoria(this.$route.params.categoria_codigo);

        }
        return (dados);

    },
    methods: {
        updateCategoria: function () {
            categoriaRepository.update(this.categoria.codigo, this.categoria,
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

var CategoriaDelete = Vue.extend({
    template: '#categoria-delete',
    data: function () {
        let dados = {
            categoria: {
                codigo: 'não encontrada',
                nome: ''
            },
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }

        if (categorias.length === 0) {
            console.log('recuperando todas categorias');
            categoriaRepository.findByCodigo(this.$route.params.categoria_codigo,
                r => {
                    this.categoria = r.data;
                    dados.categoria = r.data;


                },
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });

        } else {
            dados.categoria = findCategoria(this.$route.params.categoria_codigo);

        }
        return (dados);

    },
    methods: {
        deleteCategoria: function () {
            categoriaRepository.deleteCategoria(this.categoria.codigo,
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

var AddCategoria = Vue.extend({
    template: '#nova-categoria',

    data: function () {
        return {
            categoria: {
                codigo: 0,
                nome: ''
            },
            validacao: {
                status: 0,
                message: [],
                timeStamp: 0
            }
        }
    },
    methods: {
        createCategoria() {
            categoriaRepository.create(this.categoria,
                r => router.push('/'),
                r => {
                    this.validacao.status = r.response.data.status
                    this.validacao.message = r.response.data.message
                    this.validacao.timeStamp = r.response.data.timeStamp

                    setTimeout(() => {  this.validacao.message = [] }, 3000);
                });
        }
    }
});

var router = new VueRouter({
    routes: [
        {path: '/', component: List},
        {path: '/:categoria_codigo', component: VerCategoria, name: 'categoria'},
        {path: '/categoria/nova', component: AddCategoria, name: 'nova-categoria'},
        {path: '/:categoria_codigo/edit', component: CategoriaEdit, name: 'categoria-edit'},
        {path: '/:categoria_codigo/delete', component: CategoriaDelete, name: 'categoria-delete'}
    ]
});

new Vue({
    router
}).$mount('#app')