<template>
    <div style="margin-left:10%; margin-right:10%; font-family: 'hanna';">
      <hr />
      <h3>구매내역</h3>
      <hr />
      <div v-if="loading">Loading...</div>
      <div v-else-if="error">{{ error }}</div>
      <div v-else-if="!Object.keys(productBuyDetails).length">
        구매내역이 없습니다.
      </div>
      <div v-else>
        <div v-for="(products, date) in productBuyDetails" :key="date" class="boxShadow">
          <h3>{{ formatDate1(date) }}</h3>
          <div v-for="product in products" :key="product.product.id" class="boxShadow">
            <div style="display:flex; justify-content: space-between; align-items: center;     margin-bottom: 15px;">
              <div style="font-size:25px;">
                <b>주문 시간: {{ formatDate2(product.productBuying.purchaseTime) }}</b>
              </div>
            </div>
            <div style="display:flex; cursor: pointer; justify-content: space-evenly;" @click="openModal(product.product)">
              <!-- 이미지 -->
              <div style="width:100px; height:100px; background-color: gray;">
                <img :src="'https://storage.googleapis.com/ellie_bucket98/'+product.product.uniqueImg" style="width:inherit; height:inherit;" />
              </div>
              <div style="padding-left:30px;">
                <div>주문번호 : {{ product.productBuying.id }}</div>
                <div style="font-size:20px;">{{product.product.title}}</div>
                <div><b>{{product.product.price}}원</b></div>
                <div>상태 : {{namingStatus(product.productBuying)}} </div>
              </div>
              <div style="padding-left:30px;">
                <div>내 배송 메모</div>
                <div>{{ product.productBuying.buyingMemo }}</div>
              </div>
              <div style="padding-left:30px;">
                <div>입력한 배송지</div>
                <div>{{ product.productBuying.address }}</div>
              </div>
            </div>
            <div v-if="product.productBuying.state != 0">
              <hr />
              <div style="font-size:25px; text-align:center"><b>현재 배송 상세</b></div>
              <div style="display:flex; justify-content: space-evenly;">
                <!-- 배송 상태 표시 -->
                <div class="status-step completed">
                  <div class="status-icon">💰</div>
                  <div class="status-label">결제완료</div>
                </div>
                <div style="padding-top:7px;">→</div>
                <div class="status-step">
                  <div class="status-icon" :style="product.productBuying.state >= 2 ? 'background-color: #34c759;' : ''">🚀</div>
                  <div class="status-label">배송중</div>
                  
                </div>
                <div style="padding-top:7px;">→</div>
                <div class="status-step">
                  <div class="status-icon" :style="product.productBuying.state >= 3 ? 'background-color: #34c759;' : ''">📍</div>
                  <div class="status-label">배송완료</div>

                  <button v-if="product.productBuying.state <= 3" @click="goToHell(product.productBuying.id)" style="border-style: revert-layer; border-color: pink; 
                    background-color: pink;
                    margin-top: 10px;
                    border-radius: 20px;
                    font-size: 18px;
                    color: black;">신고하기</button>
                  
                </div>

                <div style="padding-top:7px;">→</div>
                <div class="status-step">
                  <div class="status-icon" :style="product.productBuying.state >= 4 ? 'background-color: #34c759;' : ''">✓</div>
                  <div class="status-label">구매확정</div>
                  <button v-if="product.productBuying.state < 4" @click="confirmOrder(product.productBuying.id)" style="border-style: revert-layer; border-color: wheat;
                    background-color: wheat;
                    margin-top: 10px;
                    border-radius: 20px;
                    font-size: 18px;
                    color: black;">구매 확정하기</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 모달 컴포넌트 -->
      <ProductDetail :isVisible="isModalVisible" :canBuy="canBuy" :selectedItem="selectedProduct" @close="closeModal" />
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import ProductDetail from "@/components/productDetail/ProductDetail"; // 모달 컴포넌트 임포트
  
  export default {
    data() {
      return {
        productBuyDetails: {},
        memberEmail: this.$store.getters.getEmail,
        productId: 0,
        state: 0,
        loading: true,
        error: null,
        isModalVisible: false, // 모달 표시 여부
        selectedProduct: {}, // 선택된 상품의 데이터
        canBuy: true, // 구매 가능 여부 (상황에 따라 설정)
      };
    },
    components: {
      ProductDetail, // 모달 컴포넌트 등록
    },
    mounted() {
      this.fetchAllProductBuyingData();
    },
    methods: {
      async fetchAllProductBuyingData() {
        this.loading = true;
        this.error = null;
        try {
          const response = await axios.get(`/api/productBuyDetailsbyBuyer/${this.memberEmail}`);
          if (response.status === 200) {
            this.productBuyDetails = response.data;
          } else {
            this.error = "Failed to load data";
          }
        } catch (error) {
          this.error = "Error fetching product buying data";
          console.error(error);
        } finally {
          this.loading = false;
        }
      },
      formatDate1(dateString) {
        const options = { year: "numeric", month: "long", day: "numeric" };
        return new Date(dateString).toLocaleDateString(undefined, options);
      },
      formatDate2(dateString) {
        const options = { year: "numeric", month: "long", day: "numeric", hour: "numeric", minute: "numeric" };
        return new Date(dateString).toLocaleDateString(undefined, options);
      },
      openModal(product) {
        this.selectedProduct = product; // 선택된 상품 데이터 저장
        this.isModalVisible = true; // 모달 표시
      },
      closeModal() {
        this.isModalVisible = false; // 모달 숨김
      },
      namingStatus(productBuying) {
        if (productBuying.state == 1) {
          return "결제완료";
        } else if (productBuying.state == 2) {
          return "배송중";
        } else if (productBuying.state == 3) {
          return "배송완료";
        } else if (productBuying.state == 4) {
          return "구매확정";
        } else {
          return "구매자가 없습니다";
        }
      },
      confirmOrder(productBuyingId) {
        // 확인 팝업 메시지
        if (confirm("정말로 해당 상품 구매를 확정하시겠습니까? 확정하신 뒤에는 신고를 하실 수 없습니다.")) {
            // 사용자가 '확정하기'를 누른 경우
            axios.post(`/api/productBuying/confirm/${productBuyingId}`)
                .then(response => {
                    if (response.status === 200) {
                        alert("구매가 확정되었습니다.");
                        this.fetchAllProductBuyingData(); // 데이터 재로드 (상태 갱신을 위해)
                    } else {
                        alert("구매 확정에 실패하였습니다. 다시 시도해 주세요.");
                    }
                })
                .catch(error => {
                    console.error("Error confirming order:", error);
                    alert("오류가 발생했습니다. 다시 시도해 주세요.");
                });
            } else {
            // 사용자가 '취소'를 누른 경우
                alert("구매 확정을 취소하였습니다.");
            }
        }
    },
  };
  </script>
  
  <style scoped>
  @font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf');
    }


  .boxShadow {
    border-radius: 10px;
    border: 1px solid lightgray;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.25);
    padding: 20px;
    margin-bottom: 20px;
  }
  
  .status-step {
    display: flex;
    flex-direction: column;
    align-items: center;
    flex: 1;
  }
  
  .status-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: #d1d5db;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 20px;
    margin-bottom: 5px;
  }
  
  .completed .status-icon,
  .completed .status-line {
    background-color: #34c759;
  }
  </style>
  