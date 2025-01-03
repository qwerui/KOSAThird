<template>
  <div class="mypage-container">
    <!-- 사이드바 -->
    <aside class="sidebar">
      <div class="user-info">
        <div class="username">{{ userInfo.currentname }} 님</div>
        <div class="points" v-if="userInfo.wallet !== undefined">
          포인트: {{ userInfo.wallet.toLocaleString() }} P
        </div>
        <button class="recharge-button" @click="navigateToTimeRecharge">충전하기</button>
      </div>
      <nav class="nav-menu">
        <ul>
          <li><router-link to="/bussiness-member">기업 신청</router-link></li>
          <li><router-link to="/bucket">장바구니</router-link></li>
        </ul>
      </nav>
    </aside>

    <!-- 메인 콘텐츠 -->
    <main class="content">
      <h2>마이페이지</h2>
      <div class="user-details">
        <div class="user-field">
          <label for="username">유저 이름:</label>
          <input 
            id="username"
            name="username"
            type="text"
            autocomplete="name"
            v-model="editableName"
            placeholder="이름을 입력하세요"
            style="width: 30%;" 
          />
          <button class="btn btn-primary" @click="updateName">수정</button>
        </div>
        <div class="user-field">
          <label>유저 이메일:</label>
          <span>{{ userInfo.email }}</span>
        </div>
        <div class="user-field">
          <label>유저 타임:</label>
          <span>{{ userInfo.wallet }}</span>
        </div>
        <div class="user-field">
          <label>유저 온도:</label>
          <span>{{ userInfo.temp }}</span>
        </div>
        <div class="user-field">
          <label>유저 역할:</label>
          <span>{{ userInfo.role }}</span>
        </div>
        <div class="user-field">
          <label for="userIntroduce">유저 소개:</label>
          <textarea
            id="userIntroduce"
            v-model="editableIntroduce"
            placeholder="소개를 입력하세요"
          ></textarea>
          <button class="btn btn-primary" @click="updateIntroduce">
            소개 수정
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AccountSettings',
  data() {
    return {
      userInfo: {},
      editableName: '',
      editableIntroduce: '',
      csrfToken: '',
      currentname: '',
    };
  },
  mounted() {
    this.fetchUserInfo();
    this.csrfToken = this.getCsrfTokenFromCookie();
  },
  methods: {
    fetchUserInfo() {
      axios.get('/api/user-info', {
        withCredentials: true,
      })
      .then(response => {
        this.userInfo = response.data;
        this.editableName = this.userInfo.currentname;
        this.editableIntroduce = this.userInfo.introduce;
        this.currentname = this.userInfo.currentname;
      })
      .catch(error => {
        console.error('Error fetching user info:', error);
      });
    },
    updateName() {
      const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
      const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';

      this.currentname = this.editableName;

      axios.post('/api/update-name', {
        name: this.editableName,
        email: this.userInfo.email,
      }, {
        headers: {
          'X-CSRF-TOKEN': csrfToken,
          Authorization: `Bearer ${localStorage.getItem("token")}`
        },
        withCredentials: true
      })
      .then(response => {
        alert('이름이 성공적으로 업데이트되었습니다.');
        this.fetchUserInfo(); // 사용자 정보를 다시 가져옵니다.

      })
      .catch(error => {
        console.error('이름 업데이트 중 오류 발생:', error);
        alert('이름 업데이트에 실패했습니다.');
      });
    },
    updateIntroduce() {
      axios.post('/api/update-introduce', {
        introduce: this.editableIntroduce,
        email: this.userInfo.email,
      }, {
        headers: {
          'X-CSRF-TOKEN': this.csrfToken,
          Authorization: `Bearer ${localStorage.getItem("token")}`
        },
        withCredentials: true
      })
      .then(response => {
        alert('소개가 성공적으로 업데이트되었습니다.');
        this.fetchUserInfo(); // 사용자 정보를 다시 가져옵니다.

      })
      .catch(error => {
        console.error('소개 업데이트 중 오류 발생:', error);
        alert('소개 업데이트에 실패했습니다.');
      });
    },
    getCsrfTokenFromCookie() {
      const name = 'XSRF-TOKEN=';
      const decodedCookie = decodeURIComponent(document.cookie);
      const ca = decodedCookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return '';
    },
    navigateToTimeRecharge() {
      // '/time-recharge'로 라우터 이동
      this.$router.push('/time-recharge');
    },
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'hanna';
  src: url('@/assets/fonts/GodoM.ttf');
}

.mypage-container {
  display: flex;
  justify-content: center; /*수평 가운데 정렬*/
  min-height: 100vh; /* 최소 높이를 설정하여 화면의 100% 차지 */
  background-color: #eef2f5; /* 배경색 */
  padding: 20px;
  font-family: 'hanna';
}

.sidebar {
  width: 200px;
  padding: 20px;
  background-color: #f8f9fa;
  border-right: 1px solid #ddd;
  font-family: 'hanna';
}

.user-info {
  text-align: center;
  margin-bottom: 30px;
  font-family: 'hanna';
}

.username {
  font-size: 18px;
  font-weight: bold;
  font-family: 'hanna';
}

.points {
  color: #e60000;
  margin-top: 10px;
  font-family: 'hanna';
}

.recharge-button {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #e60000;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-family: 'hanna';
}

.nav-menu ul {
  list-style: none;
  padding: 0;
  font-family: 'hanna';
}

.nav-menu li {
  margin-bottom: 15px;
  font-family: 'hanna';
}

.nav-menu a {
  color: #333;
  text-decoration: none;
  font-family: 'hanna';
}

.nav-menu a.active {
  font-weight: bold;
  color: #e60000;
  font-family: 'hanna';
}

.content {
  background-color: #ffffff; /* 배경색을 흰색으로 설정 */
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
  width: 100%;
  max-width: 600px; /* 최대 너비 설정 */
  text-align: left; /* 텍스트 왼쪽 정렬 */
  margin: 0 auto; /* 수평 가운데 정렬 */
  font-family: 'hanna';
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  font-family: 'hanna';
}

.user-details {
  margin-top: 20px;
  font-family: 'hanna';
}

.user-field {
  margin-bottom: 20px;
  font-family: 'hanna';
}

.user-field label {
  display: block;
  font-weight: bold;
  color: #393e46;
  font-size: 14px;
  margin-bottom: 6px;
  font-family: 'hanna';
}

.user-field input,
.user-field textarea {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #dddddd;
  font-size: 14px;
  color: #222831;
  background-color: #f7f7f7;
  font-family: 'hanna';
}

.user-field textarea {
  resize: none;
  height: 100px;
  font-family: 'hanna';
}

.user-field span {
  display: block;
  font-size: 14px;
  color: #393e46;
  margin-top: 6px;
  font-family: 'hanna';
}

.input-group {
  display: flex;
  gap: 10px;
  align-items: center;
  font-family: 'hanna';
}

.btn {
  padding: 10px 15px;
  border-radius: 8px;
  border: 2px solid transparent;
  cursor: pointer;
  transition: background-color 0.3s, border-color 0.3s;
  font-family: 'hanna';
}

.btn-outline {
  background-color: transparent;
  border-color: #00adb5;
  color: #00adb5;
  font-family: 'hanna';
}

.btn-outline:hover {
  background-color: #00adb5;
  color: #ffffff;
  font-family: 'hanna';
}

.btn-outline:disabled {
  background-color: #cccccc;
  color: #666666;
  cursor: not-allowed;
  font-family: 'hanna';
}
</style>
