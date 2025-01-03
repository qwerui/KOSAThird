<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-container">
      <header class="modal-header">
        <h2>로그인</h2>
        <button class="close-button" @click="closeModal">X</button>
      </header>
      <div class="modal-body">
        <form @submit.prevent="login">
          <div class="form-group">
            <label for="id">ID</label>
            <input v-model="userId" type="text" id="id" placeholder="Value" required>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input v-model="password" type="password" id="password" placeholder="Value" required>
          </div>
          <button type="submit" class="btn btn-dark">Sign In</button>
        </form>
        <div class="social-login">
          <a class="btn btn-naver" @click="socialLogin('http://localhost:8083/oauth2/authorization/naver')">네이버</a>
          <a class="btn btn-kakao" @click="socialLogin('http://localhost:8083/oauth2/authorization/kakao')">카카오</a>
          <a class="btn btn-google" @click="socialLogin('http://localhost:8083/oauth2/authorization/google')">구글</a>
        </div>
        <router-link to="/signup">회원가입</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['isVisible'],
  data() {
    return {
      userId: '',
      password: '',
    };
  },
  methods: {
    login() {
      // 로그인 로직 처리
      // 실제 로그인 로직이 성공하면 아래 메서드를 호출하세요.
      this.loginSuccess();
      this.closeModal();
    },
    closeModal() {
      this.$emit('close');
    },
    loginSuccess() {
      // this.$emit('login-success');  // 로그인 성공 이벤트 발생
      this.$emit('mypage');
    },
    socialLogin(url) {
      window.location.href = url;
      this.closeModal(); // 소셜 로그인 시 모달을 닫음
    }
  }
}
</script>

<style scoped>
@font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf');
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'hanna';
}

.modal-container {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  font-family: 'hanna';
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-family: 'hanna';
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  font-family: 'hanna';
}

.form-group {
  margin-bottom: 15px;
  font-family: 'hanna';
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-family: 'hanna';
}

.form-group input {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  font-family: 'hanna';
}

.social-login {
  margin-top: 15px;
  display: flex;
  justify-content: space-between;
  font-family: 'hanna';
}

.social-login .btn {
  width: 48%;
  font-family: 'hanna';
}

.btn-naver {
  background-color: #1ec800;
  color: white;
  font-family: 'hanna';
}

.btn-kakao {
  background-color: #f7e600;
  color: black;
  font-family: 'hanna';
}
</style>
