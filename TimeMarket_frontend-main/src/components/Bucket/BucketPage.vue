<template>
  <div class="maindiv">
    <hr />
    <h3>장바구니</h3>
    <hr />
    <div class="productContainerBox">
      <p v-if="loading">Loading...</p>
      <p v-else-if="error">{{ error }}</p>
      <p v-else-if="productList.length === 0">No products found.</p>
      <div v-else>
        <div
          v-for="(product, index) in productList"
          :key="index"
          style="display: flex; align-items: center; cursor: pointer;"
          class="boxShadow"
        >
          <!-- 체크박스 -->
          <input
            type="checkbox"
            class="custom-checkbox"
            :value="product"
            v-model="selectedProducts"
            @click.stop
            style="margin-right: 10px;"
          />
          <div style="display: flex; padding-left: 20px;" @click="openModal(product)">
            <div>
              <!-- 이미지 -->
              <div style="width: 100px; height: 100px; background-color: gray;">
                <img
                  :src="'https://storage.googleapis.com/ellie_bucket98/' + product.uniqueImg"
                  style="width: inherit; height: inherit;"
                />
              </div>
            </div>
            <div style="padding-left: 30px;">
              <div>판매종료시간: {{ product.closeTime }}</div>
              <div>{{ product.title }}</div>
              <div>{{ product.price }}원</div>
              <div>판매자 : {{ product.member.email }}</div>
            </div>
          </div>
          <!-- 삭제 아이콘 -->
          <div @click.stop="deleteProduct(product)" style="margin-left: auto; cursor: pointer;">
            <img class="icon" src="@/assets/images/delete-icon.png" alt="Delete" />
          </div>
        </div>
        <!-- 상세 정보 -->
        <transition name="slide-fade">
          <div v-if="showDetails" class="details">
            <!-- 상세 정보 내용을 여기에 추가 -->
            <p>상세 정보 내용</p>
          </div>
        </transition>
      </div>
    </div>

    <div class="checkoutShortDesc">
      <p class="assumedPrice">총 결제 예정 금액: {{ totalCost }}원</p>
      <div v-if="selectedProducts.length > 0">
        <router-link 
          :to="{ 
            name: 'ProductBuy', 
            params: { 
              totalCost: totalCost, 
              selectedProducts: selectedProducts 
            } 
          }">
          <button class="checkoutBtn">결제하러 가기</button>
        </router-link>
      </div>
    </div>

    <!-- 모달 컴포넌트 -->
    <ProductDetail
      :isVisible="isModalVisible"
      :canBuy="canBuy"
      :selectedItem="selectedProduct"
      @close="closeModal"
    />
  </div>
</template>

  
  <script>
  import axios from "axios";
  import ProductDetail from "@/components/productDetail/ProductDetail"; // 모달 컴포넌트 임포트
import ImageView from '../productDetail/ImageView.vue';
  
  export default {
    data() {
      return {
        memberEmail: this.$store.getters.getEmail,
        productList: [],
        selectedProducts: [], // 선택된 제품들
        totalCost: 0, // 총 결제 금액
        loading: true,
        error: null,
        showDetails: false, // 상세 정보 표시 여부
        isModalVisible: false, // 모달 표시 여부
        selectedProduct: {}, // 선택된 상품의 데이터
        canBuy: true, // 구매 가능 여부 (상황에 따라 설정)
      };
    },
    components: {
      ProductDetail, // 모달 컴포넌트 등록
    },
    mounted() {
    this.fetchBucketData(); // mounted 훅 안에서 메서드를 호출합니다.
    },
    watch: {
      // selectedProducts 배열이 변경될 때마다 총 가격을 재계산
      selectedProducts: {
        handler(newSelectedProducts) {

          this.totalCost = newSelectedProducts.reduce((total, product) => {
            return total + product.price;
          }, 0);
        },
        deep: true, // 배열 내부의 객체 변화도 감지
      },
    },
    methods: {
      async fetchBucketData() {
        try {
          const memberEmail = this.memberEmail;
          const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
          const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute("content") : "";
          const response = await axios.get(`/api/bucket/${memberEmail}`, {
            headers: {
              "X-CSRF-TOKEN": csrfToken,
              Authorization: `Bearer ${localStorage.getItem("token")}`, // OAuth 토큰
            },
            withCredentials: true,
          });
  
          if (response.status === 200) {
            this.productList = response.data; // 성공적으로 데이터를 가져온 경우
          } else {
            this.error = "Failed to load product details.";
          }
        } catch (error) {
          if (error.response && error.response.status === 404) {
            this.error = "장바구니가 비었습니다.";
          } else {
            this.error = "An unexpected error occurred.";
          }
        } finally {
          this.loading = false;
        }
      },
      ImageViewed() {
      // 원하는 기능 추가
      },
      toggleDetails() {
        this.showDetails = !this.showDetails;
      },
      openModal(product) {
        this.selectedProduct = product; // 선택된 상품 데이터 저장
        this.isModalVisible = true; // 모달 표시
      },
      closeModal() {
        this.isModalVisible = false; // 모달 숨김
      },
      deleteProduct(product) {
          const productId = product.id;

          axios.post(`/api/bucket/${productId}`, null, {
          headers: {
              "X-CSRF-TOKEN": this.csrfToken,
              Authorization: `Bearer ${localStorage.getItem("token")}`, // OAuth 토큰
          },
          withCredentials: true,
        })
      .then(response => {
          // 성공적으로 삭제 후 UI 업데이트 (예: 로컬 리스트에서 제거)
          const productIndex = this.productList.indexOf(product);
          if (productIndex > -1) {
              this.productList.splice(productIndex, 1);
          }
      })
      .catch(error => {
          console.error('Error deleting product:', error);
      });
}
    }
  };
  </script>
  
  <style scoped>
  @font-face {
  font-family: 'hanna';
  src: url('@/assets/fonts/GodoM.ttf'); /* 폰트 경로를 실제 경로로 수정 */
  }

  .maindiv {
    width: 60%;
    margin-left: 20%;
    font-family: 'hanna';

  }
  .boxShadow {
    border-radius: 10px;
    border: 1px solid lightgray;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.25);
    padding: 20px;
    margin-bottom: 20px;
    font-family: 'hanna';
  }
  .details {
    padding: 10px;
    border-top: 1px solid lightgray;
    font-family: 'hanna';
  }
  
  .slide-fade-enter-active,
  .slide-fade-leave-active {
    transition: max-height 0.5s ease;
    font-family: 'hanna';
  }
  
  .slide-fade-enter,
  .slide-fade-leave-to {
    max-height: 0;
    opacity: 0;
    font-family: 'hanna';
  }
  .custom-checkbox {
    width: 20px; /* 체크박스 너비 조절 */
    height: 20px; /* 체크박스 높이 조절 */
    transform: scale(1.5); /* 체크박스 크기 확대 */
    margin-right: 10px;
    cursor: pointer;
    font-family: 'hanna';
  }
  .assumedPrice {
    font-size: 25px;
    font-family: 'hanna';
    text-align: center;
    font-family: 'hanna';
  }

  .checkoutBtn {
    border-style: double;
    border-radius: 5px;
    color: white;
    background-color: wheat;
    border-color: aliceblue;
    size: 50px;
    padding: 10px 20px;
    height: 50px;
    position: unset;
    min-inline-size: -webkit-fill-available;
    font-size: 20px;
    font-family: 'hanna';
  }

  .icon {
    width: 20px;
    height: 20px;
    font-family: 'hanna';
  }
  </style>
  