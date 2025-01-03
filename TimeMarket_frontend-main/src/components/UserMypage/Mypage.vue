<template>
  <div class="mypage-container">
    <!-- <div class="content">
      <h3 >유저 정보</h3>
      <div v-if="userInfo">
        <p><strong>이름:</strong> {{ userInfo.nickname }}</p>
        <p><strong>이메일:</strong> {{ userInfo.email }}</p>
      </div>
      <div v-else>
        유저 정보를 불러오는 중입니다...
      </div>
      <router-view></router-view>
    </div> -->
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MyPage',
  data() {
    return {
      userInfo: null,
    };
  },
  mounted() {
    this.fetchUserInfo();
  },
  methods: {
    fetchUserInfo() {
      const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
      const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';
      axios.get('/api/user-info', {
        headers: {
            'X-CSRF-TOKEN': csrfToken,
            Authorization: `Bearer ${localStorage.getItem("token")}` // OAuth 토큰
        },
        withCredentials: true  // 쿠키를 포함해서 요청하도록 설정
      })
      .then(response => {
        this.userInfo = response.data;
      })
      .catch(error => {
        console.error('Error fetching user info:', error);
      });
    }
  }
};
</script>

<style scoped>
.mypage-container {
  display: flex;
  height: 100vh;
  background-color: #f8f9fa;
}

.sidebar {
  width: 250px;
  background-color: #343a40;
  padding: 20px;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo {
  width: 100%;
  margin-bottom: 20px;
}

h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.menu {
  list-style: none;
  padding: 0;
  width: 100%;
}

.menu li {
  margin-bottom: 10px;
  text-align: center;
}

.menu li a {
  text-decoration: none;
  color: #ffffff;
  font-size: 18px;
  display: block;
  padding: 10px;
  background-color: #495057;
  border-radius: 5px;
}

.menu li a:hover {
  background-color: #adb5bd;
  color: #343a40;
}

.content {
  flex: 1;
  padding: 20px;
  background-color: #ffffff;
  overflow-y: auto;
}
</style>
