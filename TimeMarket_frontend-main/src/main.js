import Vue from 'vue'
import App from './App.vue'
import routes from '@/routes/index'
import 'bootstrap/dist/css/bootstrap.min.css';
import { register } from 'swiper/element/bundle';
import axios from 'axios';
import store from '@/stores/index'

register();

Vue.config.productionTip = false
Vue.prototype.$axios = axios;

new Vue({
  render: h => h(App),
  router: routes,
  store: store
}).$mount('#app')
