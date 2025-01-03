<template>
  <div class="modal-overlay" v-if="isVisible">
    <div class="modal-content">
      <div v-if="showPopup" class="popup">
                      {{ popupMessage }}
                    </div>
      <span class="close-btn" @click="closeModal" >&times;</span>
      
      <div class="modal-header">
        
          <div id="modal-header-box">

              <div id="product-image">
                <!-- Use the ImageView component here -->
                  <image-view :productId="selectedItem.id"/>
                  <!-- <p>{{selectedItem.id}}</p> -->
                  <!-- <img :src="'http://localhost:8083/images/'+selectedItem.uniqueImg" alt="Product Image" class="product-img"/> -->
              </div>
              <div class="product-info">
                  <p class="selected-title">{{ selectedItem.title }}</p>
                  <div class="price-container">
                    <p class="selected-price">{{ selectedItem.price }}원</p>
                    <p>    </p>
                    <p class="selected-sellingtype" :style="selectedItem.sellingType === '선착순' ? normalDealStyle : selectedItem.sellingType === '무료나눔' ? freeDealStyle : standardStyle">{{ selectedItem.sellingType }}</p>
                    <p class="selected-sellingtype" v-if="selectedItem.isDirect ==='T'"> 거래방법 : 개인거래 (직거래/연락)</p>
                    <p class="selected-sellingtype" v-if="selectedItem.isDirect ==='F'"> 거래방법 : 택배 (비용 : {{ selectedItem.shippingCost }}원)</p>
                    
                  </div>
                  <div class="seller-info" v-if="selectedItem.member">
                      <p class="selected-member">판매자: {{ selectedItem.member.name }}</p>
  
                      <!-- Role with conditional styling and content -->
                      <p 
                      class="selected-role" 
                      :style="selectedItem.member.role === '기업' ? companyStyle : individualStyle"
                      >
                      {{ selectedItem.member.role }}
                      </p>
  
                      <!-- Conditionally render 신뢰도 based on the role -->
                      <p 
                      v-if="selectedItem.member.role === '개인'" 
                      class="selected-temp" 
                      :style="individualStyle"
                      >
                      신뢰도: {{ selectedItem.member.memberTemp }}
                      </p>
  
                  </div>
                  

                  <div class="dates-info">
                    <p class="selected-opentime">판매 시작 시간: {{ formatDate(selectedItem.openTime) }}</p>
                    <p class="selected-closetime">마감 시간: {{ formatDate(selectedItem.closeTime) }}</p>
                  </div>
                    <!-- 박스안 요소 : 버튼마다 다른 내용 나옴 -->
                  <div v-if="whatToShow" class="detail-description">
                    <p class="description-title">제품 상세</p>
                    <p class="selected-description">{{ selectedItem.description }}</p>
                  </div>

                  <div v-if="!whatToShow" class="startAuction">
                    <auction-detail v-if="auction" :productId="selectedItem.id" :startPrice="selectedItem.price" :bidUnit="selectedItem.bidUnit" :joinMember="userEmail" :closingTime="selectedItem.closeTime"> <!--:bidunit="selectedItem.bidunit"-->
                      
                    </auction-detail>

                    <order-detail v-if="buy" :productId="selectedItem.id"/>
                  </div>
                  
              </div>
              

          </div>
          <div class="modal-buttons">
            <div class="modal-actions">
                <!-- 버튼 -->
                    <button v-if="canBuy && selectedItem.sellingType != '경매'" class="buy-btn" @click="handleBuyClick">장바구니 추가</button>
                    <button v-if="canBuy && selectedItem.sellingType === '경매'" class="red-btn"  @click="handleAuctionClick">경매참여</button>
                    <button v-if="!canBuy" class="notify-btn" @click="handleNotifyClick">알림받기</button>
                    
              </div>
              <button v-if="canBuy && selectedItem.sellingType == '경매'" class="desc-btn" @click="toggleDetails">제품상세</button>
              </div>          
      </div>
    </div>
    
  </div>
  
</template>

<script>
import ImageView from './ImageView' ; // Import the ImageView component
import AuctionDetail from './AuctionDetail';
import OrderDetail from './OrderDetail';
import axios from 'axios';
export default {
name: "ProductDetail",
components: {
    ImageView, // Register the ImageView component
    AuctionDetail,
    OrderDetail,
  },
props: {
  isVisible: {
    type: Boolean,
    default: false,
  },
  canBuy: {
    type: Boolean,
    default: true,
  },
  selectedItem: {
    type: Object,
    required: true,
  },
},

computed: {
  individualStyle() {
    return {
      backgroundColor: '#ddeeff', // Background color for '개인'
    };
  },
  companyStyle() {
    return {
      backgroundColor: '#ddddff', // Background color for '기업'
    };
  },
  standardStyle() {
      return {
          backgroundColor: '#ffdddd',
      }
  },
  normalDealStyle(){
      return{
          backgroundColor: '#ddffe5',
      }
  },
  freeDealStyle(){
      return{
          backgroundColor: '#ffffdd',
      }
  }
},
data() {
  return {
      whatToShow: true, // 제품 상세 보기 여부
      cash: 10000,
      auction: false,
      buy: false,
      showPopup: false, // 팝업 표시 여부
      userEmail: this.$store.getters.getEmail,
    };
},
methods: {
  
  closeModal() {
    this.whatToShow = true;
    this.auction = false;
    this.buy = false;
    this.$emit("close"); // Close event
  },
  formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: "numeric", minute: "numeric" };
    return new Date(dateString).toLocaleDateString(undefined, options);
  },
  toggleDetails() {
      this.whatToShow = true; // 제품 상세 보기 활성화
      this.buy = false;
      this.auction = false;
    },
    async handleBuyClick() {
      this.buy = true;

      if (this.userEmail == '' || this.userEmail == null) {
        alert('로그인을 먼저 진행해 주세요.');
      } else {
        try {
          const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
          const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';
          const response2 = await axios.post(`/api/want/${this.selectedItem.id}/${this.userEmail}`, {
            headers: {
              'X-CSRF-TOKEN': csrfToken,
              Authorization: `Bearer ${localStorage.getItem("token")}`
            },
            withCredentials: true
          });

          if (response2.status == 200) {
            this.popupMessage = response2.data;
            this.showPopup = true;
            setTimeout(() => {
              this.showPopup = false; // 5초 후 팝업 닫기
            }, 5000);
          }
        } catch (error) {
          if (error.response && error.response.status === 400) {
            // 이미 등록된 알람인 경우
            this.popupMessage = '이미 장바구니에 담겨 있는 상품 입니다.';
            this.showPopup = true;
            setTimeout(() => {
              this.showPopup = false; // 5초 후 팝업 닫기
            }, 5000);
          } else {
            console.error("장바구니 추가 중 오류가 발생했습니다:", error);
          }
        }
      }
    },
    handleAuctionClick() {
      this.whatToShow = false; // 경매참여 버튼 클릭 시 상세보기 비활성화
      this.auction = true;
    },
    async handleNotifyClick() {
      try {
        const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
        const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';

        const response = await axios.post(
          `/api/reg-open-alarm/${this.selectedItem.id}/${this.userEmail}`,
          {}, // request body는 비어있으므로 빈 객체 전달
          {
            headers: {
              'X-CSRF-TOKEN': csrfToken,
              Authorization: `Bearer ${localStorage.getItem("token")}`, // OAuth 토큰
            },
            withCredentials: true,
          }
        );

        if (response.status === 200) {
          // 알람이 성공적으로 등록된 경우
          this.popupMessage = response.data; // 컨트롤러에서 반환된 메시지 사용
          this.showPopup = true;
          setTimeout(() => {
            this.showPopup = false; // 5초 후 팝업 닫기
          }, 5000);
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          // 이미 등록된 알람인 경우
          this.popupMessage = '이미 등록된 알람입니다.';
          this.showPopup = true;
          setTimeout(() => {
            this.showPopup = false; // 5초 후 팝업 닫기
          }, 5000);
        } else {
          console.error("알림 등록 중 오류가 발생했습니다:", error);
        }
      }
    },
  },
};
</script>
<style scoped>
@font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf');
}
.seller-info {
  display: flex;
align-items: center;
gap: 5px; /* Set gap between flex items */
font-family: 'hanna';
}
.selected-role, .selected-temp {
display: inline;
padding: 5px 10px;
border-radius: 10px;
font-size: 13px;
color: black; /* Text color for both roles */
border: 1px solid transparent;
font-family: 'hanna';
}

.selected-temp {
display: inline;
padding: 5px 10px;
border-radius: 10px;
font-size: 13px;
color: black; /* Text color for both roles */
border: 1px solid transparent;
font-family: 'hanna';
}

/* Styles for role '개인' */
.individual-role {
background-color: #ddeeff;
margin-right: 10px; 
font-family: 'hanna';
}

/* Styles for role '기업' */
.company-role {
background-color: orange; /* Orange background for 기업 */
font-family: 'hanna';
}


.selected-sellingtype{
  display: inline;
  padding: 5px 10px;
  background-color: white;
  border-radius: 10px;
  font-size: 13px;
  color: #333;
  border: 1px solid white;
  margin-left: 10px;
  font-family: 'hanna';
  }

.standardStyle {
  background-color: pink;
  border: 1px solid pink;
  font-family: 'hanna';
}
.selected-title {
  font-size: 35px;
  color: black;
  font-weight: bold;
  font-family: 'hanna';
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
  z-index: 1000;
  font-family: 'hanna';
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 900px;
  max-width: 90%;
  position: relative;
  font-family: 'hanna';
  height: 680px;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  cursor: pointer;
  font-family: 'hanna';
}

.modal-header {
  display: flex;
    align-items: center;
    height: 600px;
    width: 100%;
    font-family: 'hanna';
}

#modal-header-box {
  display: flex;
  align-items: center;
  height: 100%;
  width: 100%;
  font-family: 'hanna';
  /* background-color: cyan; */
}

#product-image {
  width: 50%;
  height: 100%;
  box-sizing: border-box;
  margin-right: 20px;
  background-color: #ffffff;
  align-content: space-evenly;
  font-family: 'hanna';
}

#product-image img {
  width: 100%;
  height: auto;
  font-family: 'hanna';
}
/* .product-img {
  width: 300px;
  height: auto;
  margin-right: 20px;
} */

.product-info {
  flex-grow: 1;
  width: 50%;
  height: 90%;
  font-family: 'hanna';
}

.selected-title {
  font-size: 24px;
  margin-bottom: 10px;
  font-family: 'hanna';
}

.selected-price {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  font-family: 'hanna';
}

.selected-sellingtype {
  font-size: 14px;
  margin-bottom: 10px;
  font-family: 'hanna';
}

.seller-info {
  font-size: 14px;
  color: #555;
  font-family: 'hanna';
}

.modal-body {
  margin-top: 20px;
  font-family: 'hanna';
}

.modal-actions {
  display: flex;
    align-items: center;
    
    width: 100%;
    font-family: 'hanna';
}

.buy-btn,
.notify-btn,
.red-btn 
{
  padding: 10px 19px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    position: fixed;
    font-family: 'hanna';
    width: 97px;
}

.buy-btn {
  background-color: #28a745;
  color: white;
  font-family: 'hanna';
}

.notify-btn {
  background-color: #ffc107;
  color: white;
  font-family: 'hanna';
}

.red-btn {
  background-color: pink;
  color: white;
  font-family: 'hanna';
}

.desc-btn{
  padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    position: fixed;
    margin-top: 30px;
    width: 97px;
    font-size: 15px;
    color: white;
    background-color: darksalmon;
    font-family: 'hanna';
}

.price-container {
  display: flex;
  align-items: center; /* Align items vertically center if needed */
  font-family: 'hanna';
}

.dates-info{
  font-size: 12px;
  font-family: 'hanna';
}

.detail-description {
  max-height: 200px; /* 원하는 최대 높이 설정 */
  overflow-y: auto;  /* 수직 스크롤 추가 */
  padding: 10px;
  border: 1px solid #ccc; /* 선택적인 테두리 스타일 */
  border-radius: 4px; /* 선택적인 모서리 반경 */
  background-color: #fff; /* 배경 색상 설정 */
  margin-right: 50px;
  font-family: 'hanna';
}

.selected-description {
  margin: 0; /* 여백 제거 */
  font-size: 13px;
  font-family: 'hanna';
}
.description-title{
  font-weight: bold;
  font-size: 15px;
  font-family: 'hanna';
}

.popup {
  position: center;
  bottom: 20px;
  right: 20px;
  background-color: #4CAF50;
  color: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  text-align: center;
}

</style>
