<template>
  <div class="recharge-container">
    <!-- 사이드바 -->
    <aside class="sidebar">
      <div class="user-info">
        <div class="username">{{ userInfo.nickname }} 님</div>
        <div class="points" v-if="userInfo.wallet !== undefined">
          포인트: {{ userInfo.wallet.toLocaleString() }} P
        </div>
        <button class="recharge-button" >충전하기</button>
      </div>
      <nav class="nav-menu">
        <ul>
          <li><router-link to="/recharge">충전하기</router-link></li>
          <li><router-link to="/recharge-history">충전내역</router-link></li>
          <li><router-link to="/usage-history">사용내역</router-link></li>
        </ul>
      </nav>
    </aside>

    <!-- 메인 콘텐츠 -->
    <main class="content">
      <h2>충전하기</h2>
      <p>충전 금액을 선택해 주세요</p>

      <div class="recharge-options">
        <div class="option" v-for="(option, index) in rechargeOptions" :key="index">
          <div class="option-header">
            <span class="bonus" v-if="option.bonus">{{ option.bonus }}</span>
          </div>
          <div class="option-body">
            <div class="amount">{{ option.amount.toLocaleString() }}원</div>
            <div class="price">{{ option.price.toLocaleString() }}</div>
          </div>
          <div class="option-footer">
            <input type="radio" :id="'recharge-' + index" name="recharge" v-model="selectedOption" :value="option">
            <label :for="'recharge-' + index"></label>
          </div>
        </div>
      </div>

      <p>결제 수단을 선택해 주세요.</p>
      <div class="payment-methods">
        <div class="method" @click="selectPaymentMethod('bank')">
          <img class="icon" src="@/assets/images/bank-icon.png"/>
          <div class="label">무통장 입금</div>
        </div>

        <div class="method" @click="reCharge()">
          <img class="icon" src="@/assets/images/card-icon.png"/>
          <div class="label">신용카드</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';
import EventBus from '@/util/eventBus'; 


export default {
  data() {
    return {
      userInfo: {}, // 유저 정보를 저장할 객체
      rechargeOptions: [
        { amount: 50000, price: 50000, bonus: null },
        { amount: 500000, price: 525000, bonus: "5% 추가적립" },
        { amount: 5000000, price: 5500000, bonus: "10% 추가적립" },
      ],
      selectedOption: '',
      paymentMethod: '',
      csrfToken: '', // CSRF 토큰을 저장할 변수 추가
    };
  },
  mounted() {
    this.fetchUserInfo(); // 유저 정보를 가져오는 메소드 호출
    this.csrfToken = this.getCsrfTokenFromCookie(); // CSRF 토큰을 쿠키에서 가져옴
  },
  methods: {
    fetchUserInfo() {
      axios.get('/api/user-info', {
        withCredentials: true  // 쿠키를 포함해서 요청하도록 설정
      })
      .then(response => {
        this.userInfo = response.data; // 서버로부터 받은 유저 정보를 저장
      })
      .catch(error => {
        console.error('Error fetching user info:', error);
      });
    },
    async reCharge(){
      // 팝업 창 띄우기
      const popup = window.open(
                'https://payapplite.com/l/SaR8yH', // 결제 URL
                'payapplite-popup',
                'width=800,height=600' // 팝업 창 크기 설정
            );

            // 팝업이 닫히는 것을 감지
            const popupInterval = setInterval(async () => {
                if (popup.closed) {
                    clearInterval(popupInterval); // interval 중지

                    // 팝업이 닫혔을 때 데이터베이스에 요청
                    let success = true;
                    const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
                    const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';

                    try {
                      const response = await axios.post(`/api/recharge-wallet/${this.userInfo.email}/${this.selectedOption.price}`, {
                      headers: {
                        'X-CSRF-TOKEN': csrfToken,
                        'Authorization': `Bearer ${localStorage.getItem("token")}`,
                        },
                        withCredentials: true
                       });
                       if(response.status === 200){
                        EventBus.$emit('update-wallet'); // 전역 이벤트 버스에 'update-wallet' 이벤트 발생
                        this.fetchUserInfo();
                       }
                    } catch (error) {
                      success = false;
                    } 
                                      // 요청이 성공적으로 완료되었는지 확인
                    if (success) {
                        alert('결제가 완료되었습니다.');
                        this.$router.push('/account-edit'); // 결제 후 이동
                    } else {
                        alert('결제중 오류가 발생하였습니다. 다시 시도해 주세요.');
                    }
                }
            }, 500); // 0.5초마다 팝업이 닫혔는지 확인

    },
    selectPaymentMethod(method) {
      this.paymentMethod = method;
      alert(method + " 선택됨");
    },
    navigateToTimeRecharge() {
      this.$router.push('/time-recharge');
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
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'hanna';
  src: url('@/assets/fonts/GodoM.ttf'); /* 폰트 경로를 실제 경로로 수정 */
}

.recharge-container {
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

.recharge-options {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
  font-family: 'hanna';
}

.option {
  width: 30%;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 20px;
  background-color: white;
  position: relative;
  font-family: 'hanna';
}

.option-header .bonus {
  position: absolute;
  top: -10px;
  left: -10px;
  background-color: #e60000;
  color: white;
  padding: 5px;
  border-radius: 5px;
  font-size: 12px;
  font-family: 'hanna';
}

.option-body .amount {
  font-size: 24px;
  font-weight: bold;
  font-family: 'hanna';
}

.option-body .price {
  margin-top: 10px;
  font-size: 18px;
  color: #666;
  font-family: 'hanna';
}

.option-footer {
  margin-top: 15px;
  font-family: 'hanna';
}

.payment-methods {
  display: flex;
  justify-content: space-around;
  font-family: 'hanna';
}

.method {
  width: 45%;
  text-align: center;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  cursor: pointer;
  background-color: white;
  font-family: 'hanna';
}

.method .label {
  font-size: 16px;
  font-weight: bold;
  font-family: 'hanna';
}

.icon {
  width: 150px; /* 너비를 50px로 설정 */
  height: 150px; /* 높이를 50px로 설정 */
  object-fit: contain; /* 이미지를 잘라내지 않고 맞추기 */
  font-family: 'hanna';
}
</style>
