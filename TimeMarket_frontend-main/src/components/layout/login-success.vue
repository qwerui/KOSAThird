<template>
  <div class="container">
    <div class="user-info-card">
      <h1>유저 정보</h1>
  
      <ul v-if="userInfo" class="user-info-list">
        <li><strong>이름:</strong> {{ userInfo.nickname }}</li>
        <li><strong>이메일:</strong> {{ userInfo.email }}</li>
      </ul>
      <div v-else class="loading">
        유저 정보를 불러오는 중입니다...
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      userInfo: null,
    };
  },
  mounted() {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    if (token) {
      localStorage.setItem('token', token);  // 토큰을 localStorage에 저장
      this.fetchUserInfo();  // 사용자 정보 가져오기
    } else {
      console.error('No token found in URL');
    }
  },
  methods: {
    fetchUserInfo() {
      const token = localStorage.getItem('token');
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
    ...mapActions(['login'])
  },
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f4f9;
}

.user-info-card {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 400px;
  width: 100%;
  text-align: center;
}

h1 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.user-info-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
  text-align: left;
}

.user-info-list li {
  font-size: 18px;
  margin-bottom: 10px;
}

.loading {
  font-size: 18px;
  color: #888;
}

.loading::after {
  content: "⏳";
  display: inline-block;
  margin-left: 10px;
}
</style>
