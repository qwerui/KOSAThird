import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import User from '@/stores/user-store'
import ProductBuying from '@/stores/productbuying-store'


Vue.use(Vuex);

 //나선주: productBuying vuex에 추가

export default new Vuex.Store({
    modules: { 
        user: User,
        productBuying: ProductBuying,
    },
    plugins:[
        createPersistedState({
            storage: window.sessionStorage
        })
    ]
});