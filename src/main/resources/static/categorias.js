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
    findAll(fn) {
        axios
                .get('/api/v1/categorias')
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    findByCodigo(categoria_codigo, fn) {
        axios
                .get('/api/v1/categorias/' + categoria_codigo)
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    create(categoria, fn) {
        axios
                .post('/api/v1/categorias/adicionar-categoria', categoria)
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    update(categoria_codigo, categoria, fn) {
        axios
                .put('/api/v1/categorias/' + categoria_codigo, categoria)
                .then(response => fn(response))
                .catch(error => console.log(error))
    },

    deleteCategoria(categoria_codigo, fn) {
        axios
                .delete('/api/v1/categorias/' + categoria_codigo)
                .then(response => fn(response))
                .catch(error => console.log(error))
    }
}

var List = Vue.extend({
    template: '#lista-categorias',
    data: function () {
        return {categorias: [], searchKey: ''};
    },
    computed: {
        filteredCategorias() {
            return this.categorias.filter((categoria) => {
                return categoria.nome.indexOf(this.searchKey) > -1
            }).sort((a,b) => (a.codigo > b.codigo) ? 1 : ((b.codigo > a.codigo) ? -1 : 0))
        }
    },
    mounted() {
        categoriaRepository.findAll(r => {
            this.categorias = r.data;
            categorias = r.data
        })
    }
});



var VerCategoria = Vue.extend({
    template: '#categoria',
    data: function () {


        console.log('recuperando a categoria: ' + this.$route.params.categoria_codigo);

        if (categorias.length === 0) {
            categoriaRepository.findByCodigo(this.$route.params.categoria_codigo, r => {
                this.categoria = r.data;
            });
            return({categoria: this.categoria});
        } else {
            return {categoria: findCategoria(this.$route.params.categoria_codigo)};

        }

    }
});

var CategoriaEdit = Vue.extend({
    template: '#categoria-edit',
    data: function () {


        console.log('recuperando a categoria: ' + this.$route.params.categoria_codigo);

        if (categorias.length === 0) {
            console.log('recuperando todos categorias');
            categoriaRepository.findByCodigo(this.$route.params.categoria_codigo, r => {
                this.categoria = r.data;
                console.log("oooooo", this.categoria);


            });
            return({categoria: this.categoria});
        } else {
            return {categoria: findCategoria(this.$route.params.categoria_codigo)};

        }

    },
    methods: {
        updateCategoria: function () {
            categoriaRepository.update(this.categoria.codigo, this.categoria, r => router.push('/'))
        }
    }
});

var CategoriaDelete = Vue.extend({
    template: '#categoria-delete',
    data: function () {
        console.log('recuperando o categoria: ' + this.$route.params.categoria_codigo);

        if (categorias.length === 0) {
            console.log('recuperando todos categorias');
            categoriaRepository.findByCodigo(this.$route.params.categoria_codigo, r => {
                this.categoria = r.data;
                console.log("oooooo", this.categoria);


            });
            return({categoria: this.categoria});
        } else {
            return {categoria: findCategoria(this.$route.params.categoria_codigo)};

        }

    },
    methods: {
        deleteCategoria: function () {
            categoriaRepository.deleteCategoria(this.categoria.codigo, r => router.push('/'))
        }
    }
});

var AddCategoria = Vue.extend({
    template: '#nova-categoria',
    data: function () {
        return {categoria: {nome: ''}}
    },
    methods: {
        createCategoria() {
            categoriaRepository.create(this.categoria, r => router.push('/'));
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