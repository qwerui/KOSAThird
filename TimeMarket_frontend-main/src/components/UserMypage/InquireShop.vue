<template>
  <div class="contact-container">
    <!-- 사이드바 -->
    <aside class="sidebar">
      <div class="user-info">
        <div class="username">{{ userInfo.currentname }} 님</div>
        <div class="points">포인트: {{ userInfo.wallet}} P</div>
        <button class="recharge-button" @click="navigateToTimeRecharge">충전하기</button>
      </div>
      <nav class="nav-menu">
        <!-- <ul>
          <li><router-link to="/recharge">충전하기</router-link></li>
          <li><router-link to="/recharge-history">충전내역</router-link></li>
          <li><router-link to="/usage-history">사용내역</router-link></li>
          <li><router-link to="/contact">문의하기</router-link></li>
        </ul> -->
      </nav>
    </aside>

    <!-- 메인 콘텐츠 -->
    <main class="content">
      <h2>문의하기</h2>
      <p>문의 사항을 입력해 주세요.</p>

      <form @submit.prevent="submitInquiry">
        <div class="form-group">
          <label for="inquiry-type">문의 유형</label>
          <select id="inquiry-type" v-model="inquiry.type" required>
            <option value="general">일반 문의</option>
            <option value="technical">기술 지원</option>
            <option value="billing">결제 문의</option>
          </select>
        </div>

        <div class="form-group">
          <label for="inquiry-message">문의 내용</label>
          <textarea
            id="inquiry-message"
            v-model="inquiry.message"
            placeholder="문의 내용을 입력하세요"
            required
          ></textarea>
        </div>

        <button type="submit" class="btn btn-primary">제출하기</button>
      </form>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userInfo: {},
      inquiry: {
        type: '',
        message: '',
      },
      csrfToken: '',
    };
  },
  mounted() {
    this.fetchUserInfo();
    this.csrfToken = this.getCsrfTokenFromCookie();
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
    submitInquiry() {
    // 이메일 전송을 위한 URL 생성
    const recipientEmail = 'TMCoperationCompany@gmail.com'; // 받는 사람 이메일 주소
    const subject = encodeURIComponent('문의 사항: ' + this.inquiry.type); // 이메일 제목
    const body = encodeURIComponent(`문의 유형: ${this.inquiry.type}\n문의 내용: ${this.inquiry.message}\n\n보낸 사람: ${this.userInfo.currentname}`); // 이메일 본문 내용

    const gmailUrl = `https://mail.google.com/mail/?view=cm&fs=1&to=${recipientEmail}&su=${subject}&body=${body}`;

    // Gmail로 이동
    window.open(gmailUrl, '_blank');
  }
  },
};
</script>

<style scoped>
.contact-container {
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
  margin-bottom: 20px;
  font-family: 'hanna';
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  font-family: 'hanna';
}

textarea{
  width: 100%;
  height: 150px;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-family: 'hanna';
}

select {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-family: 'hanna';
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #e60000;
  color: white;
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
</style>
