<template>
  <div class="business-application-container">
    <!-- 사이드바 -->
    <aside class="sidebar">
      <div class="user-info">
        <div class="username">{{ userInfo.nickname }} 님</div>
        <div class="points" v-if="userInfo.wallet !== undefined">
          포인트: {{ userInfo.wallet.toLocaleString() }} P
        </div>
        <button class="recharge-button" @click="navigateToTimeRecharge">충전하기</button>
      </div>
      <nav class="nav-menu">
      
      </nav>
    </aside>

    <!-- 메인 콘텐츠 -->
    <main class="content">
      <h2>기업 신청</h2>
      <p>아래 정보를 입력하여 기업 회원으로 신청하세요.</p>

      <form @submit.prevent="submitBusinessApplication">
        <div class="form-group">
          <label for="businessName">사업자명</label>
          <input
            id="businessName"
            v-model="businessInfo.name"
            placeholder="사업자명을 입력하세요"
            required
          />
        </div>

        <div class="form-group">
          <label for="businessNumber">사업자번호</label>
          <input
            id="businessNumber"
            v-model="businessInfo.number"
            placeholder="사업자번호를 입력하세요"
            required
          />
        </div>

        <div class="form-group">
          <label for="businessAddress">사업지 주소</label>
          <input
            id="businessAddress"
            v-model="businessInfo.address"
            placeholder="사업지 주소를 입력하세요"
            required
          />
        </div>

        <div class="form-group">
          <label for="businessPhone">사업지 전화번호</label>
          <input
            id="businessPhone"
            v-model="businessInfo.phone"
            placeholder="사업지 전화번호를 입력하세요"
            required
          />
        </div>

        <button type="submit" class="btn btn-primary">신청하기</button>
      </form>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userInfo: {
        wallet: 0, // 초기화 값
      },
      businessInfo: {
        name: '',
        number: '',
        address: '',
        phone: '',
      },
      csrfToken: '',
    };
  },
  mounted() {
    this.fetchUserInfo();
    this.csrfToken = this.getCsrfTokenFromCookie(); // CSRF 토큰을 쿠키에서 가져옴
  },
  methods: {
    fetchUserInfo() {
      axios.get('/api/user-info', {
        withCredentials: true
      })
      .then(response => {
        this.userInfo = response.data;
      })
      .catch(error => {
        console.error('Error fetching user info:', error);
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
      this.$router.push('/time-recharge');
    },
    submitBusinessApplication() {
        // 사용자 정보와 사업자 정보를 바탕으로 메일 작성 URL 생성
        const recipientEmail = encodeURIComponent('TMCoperationCompany@gmail.com'); // 받는 사람 이메일
        const subject = encodeURIComponent('기업 회원 신청'); // 이메일 제목
        const body = encodeURIComponent(`안녕하세요,

        ${this.userInfo.nickname}님이 기업 회원으로 신청합니다.

        사업자명: ${this.businessInfo.name}
        사업자번호: ${this.businessInfo.number}
        사업지 주소: ${this.businessInfo.address}
        사업지 전화번호: ${this.businessInfo.phone}

        감사합니다.`); // 이메일 본문 내용

        // Gmail URL
        const gmailUrl = `https://mail.google.com/mail/?view=cm&fs=1&to=${recipientEmail}&su=${subject}&body=${body}`;

        // Outlook URL
        // const outlookUrl = `https://outlook.live.com/owa/?path=/mail/action/compose&to=&subject=${subject}&body=${body}`;

        // 사용자의 소셜 계정에 따라 해당 메일 서비스로 이동
        window.open(gmailUrl, '_blank'); // Gmail로 이동하는 경우
        // window.open(outlookUrl, '_blank'); // Outlook으로 이동하는 경우
    }
  }
};
</script>

<style scoped>
@font-face {
  font-family: 'hanna';
  src: url('@/assets/fonts/GodoM.ttf');
}

.business-application-container {
  display: flex;
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
  flex: 1;
  padding: 20px;
  font-family: 'hanna';
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  font-family: 'hanna';
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  font-family: 'hanna';
}

.form-group input {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 14px;
  font-family: 'hanna';
}

.btn-primary {
  padding: 10px 20px;
  background-color: #00adb5;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-family: 'hanna';
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: #007b9e;
}
</style>
