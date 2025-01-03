import Vue from 'vue';
import VueRouter from 'vue-router';
import pageroutes from './productForm';
import Route from '@/routes/route'

Vue.use(VueRouter);

export default new VueRouter({
    mode: 'history',
    routes: [...pageroutes, ...Route,]
});