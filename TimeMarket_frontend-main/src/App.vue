<template>
  <div id="app">
    <top-nav></top-nav>
    <div id="top-margin-div"></div>
    <div id="content-wrapper">
      <router-view></router-view>
    </div>
    
    <footer-comp></footer-comp>
  </div>
</template>

<script>
import TopNav from './components/layout/TopNav.vue'
import FooterComp from './components/layout/FooterComp.vue';
import axios from 'axios';
import { mapActions } from 'vuex';
import Cookies from 'js-cookie';

export default {
  name: 'App',
  components: {
    TopNav,
    FooterComp
  },
  data() {
    return {
      userInfo: null,
    };
  },
  mounted() {
    const token = Cookies.get('jwt_token');   // 쿠키에서 토큰 읽어옴
    if (token) {
      this.fetchUserInfo(token);  // 사용자 정보 가져오기
    } else {
      console.error('No token found in URL');
    }

    this.fetchCsrf();
  },
  methods: {
    fetchUserInfo(token) {
      if (token) {
        axios.get('/api/user-info', {
          headers: {
            Authorization: `Bearer ${token}`
          },
          withCredentials: true
        })
        .then(response => {
          this.userInfo = response.data;
          this.userInfo.token = token;
          this.login(this.userInfo);
        })
        .catch(error => {
          console.error('Error fetching user info:', error);
        });
      } else {
        console.error('No token found');
      }
    },
    async fetchCsrf(){
      try{
        const response = await axios('/api/csrf-token');
        Cookies.set('XSRF-TOKEN', response.data.csrfToken);
      } catch(error){
        //error
      }
      
    },
    ...mapActions(['login'])
  },
}
</script>

<style>

body {
  margin: 0px;
  padding: 0px;
  background-color: white;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height : 100vh;
}

#top-margin-div{
  height: 100px;
  background-color: white;

}

.vue-link {
  text-decoration-line: none;
  color: #2c3e50;
}

#content-wrapper {
  height: auto;
  min-height: 100%;
  padding-bottom: 300px;
}
</style>