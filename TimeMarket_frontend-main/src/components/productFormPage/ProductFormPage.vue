<template>
    <div
      id="content"
      style="display: flex; justify-content: flex-start; align-items: flex-start; width: 80%; 
      position: relative; right: -100px; margin: auto;"
    >
      <!-- 상품 정보 영역 -->
      <div id="product-info" style="width: 70%;">
        <h2 style="margin-top:40px;"><b>상품 정보</b></h2>
        <hr class="hr_menu" />
        <form @submit.prevent="saveProduct" enctype="multipart/form-data">
          <div style="display:flex; align-items: center; margin-bottom: 20px;">
            <div style="text-align: center;width:100px;">
              상품이미지<br /><span>({{images.length}}/5)</span>
            </div>
            <a href="#" @click.prevent="triggerFileInput" style="color:black; text-decoration: none;">
              <div
                style="width:200px;height:200px; margin-left:30px; background-color: gray; display:inline-flex; align-items:center; justify-content:center; flex-direction: column;"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="100"
                  height="100"
                  fill="currentColor"
                  class="bi bi-camera-fill"
                  viewBox="0 0 16 16"
                >
                  <path d="M10.5 8.5a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0" />
                  <path
                    d="M2 4a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2h-1.172a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 9.172 2H6.828a2 2 0 0 0-1.414.586l-.828.828A2 2 0 0 1 3.172 4zm.5 2a.5.5 0 1 1 0-1 .5.5 0 0 1 0 1m9 2.5a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0"
                  />
                </svg>
                <span>이미지등록</span>
              </div>
            </a>
            <input
              type="file"
              ref="fileInput"
              accept="image/*"
              multiple
              style="display: none;"
              @change="previewImage"
            />
          </div>
          <!-- 이미지 미리보기 -->
          <div
            v-if="images.length"
            style="background-color: gray; display:flex; flex-wrap:wrap; justify-content: space-evenly; margin-top:10px;"
          >
            <div
              v-for="(image, index) in images"
              :key="index"
              style="width:200px;height:200px;position:relative"
            >
              <div
                v-if="index==0"
                style="position:absolute; border-radius: 10px; background-color: lightpink; opacity:0.8; top:5px;left:5px;"
              >
                대표이미지
              </div>
              <img :src="image" alt="이미지 미리보기" style="width:inherit; height:inherit" />
              <button @click="removeImage(index)" style="position:absolute; right:0">x</button>
            </div>
          </div>
          <hr />
          <div style="display:flex;align-items: center;">
            <div style="text-align: center;width:80px;">상품명</div>
            <input
              id="title"
              v-model="product.title"
              @input="updateTextLength"
              style="margin-left:30px;width:50%"
              type="text"
              maxlength="40"
              required
            />
            <span style="margin-left:10px;">{{ textLength }}/40</span>
          </div>
          <hr />
          <div style="display: flex; align-items: center;">
            <!-- 상품상태 텍스트 부분 -->
            <div style="text-align: center; width: 95px; margin-right: 30px;">상품상태</div>
  
            <!-- 라디오 버튼들 -->
            <div style="display: flex; align-items: center; width: 100%;">
              <label style="margin-right: 80px;"
                ><input v-model="product.isUsed" type="radio" name="isUsed" value="F" />새상품</label
              >
              <label style="margin-right: 80px;"
                ><input v-model="product.isUsed" type="radio" name="isUsed" value="T" />중고</label
              >
            </div>
          </div>
          <hr />
          <div style="display:flex;align-items: center;">
            <div style="text-align: center;width:80px;">설명</div>
            <textarea
              name=""
              id="productDescription"
              v-model="product.description"
              @input="updateDescriptionTextLength"
              cols="30"
              rows="10"
              style="margin-left:40px;width:100%; resize:none"
              placeholder=" 상품설명 칸입니다."
            ></textarea>
          </div>
          <div style="text-align: right;">{{descriptionTextLength}}/2000</div>
  
          <h2 style="margin-top:40px;"><b>거래</b></h2>
          <hr class="hr_menu" />
          <div style="display:flex;align-items: center;">
            <div style="text-align: center;width:80px;">거래방식</div>
            <label style="margin-right: 20px;margin-left:30px;">
              <input v-model="product.isDirect" type="radio" name="isDirect" value="T" checked />개인거래
            </label>
            <label style="margin-right: 20px;">
              <input v-model="product.isDirect" type="radio" name="isDirect" value="F" />택배거래
            </label>
          </div>
          <hr />
          <div v-if="product.isDirect==='F'" style="display:flex;align-items: center;">
            <div style="text-align: center;width:80px;">배송비</div>
            <label style="margin-right: 20px;margin-left:30px;">
              <input type="radio" name="isShippingCost" value="included" v-model="shippingOption" />배송비 포함
            </label>
            <label style="margin-right: 20px;">
              <input type="radio" name="isShippingCost" value="excluded" v-model="shippingOption" />배송비 별도
            </label>
          </div>
          <div v-if="shippingOption==='excluded'" style="margin-left:110px;margin-top:10px;">
            <input type="text" placeholder="배송비 입력" v-model="product.shippingCost" />
            <span>&nbsp;원</span>
          </div>
  
          <h2 style="margin-top:60px;"><b>기한</b></h2>
          <hr class="hr_menu" />
          <div style="display:flex;align-items: center;">
            <div style="text-align: center;width:80px;">형식</div>
            <select v-model="product.sellingType" name="sellingType" style="width:30%; margin-left:30px;height:30px;">
              <option value="선착순" selected>선착순</option>
              <option value="경매">경매</option>
              <option value="무료나눔">무료나눔</option>
            </select>
          </div>
  
          <!-- 조건에 따른 div 표시 -->
          <hr />
          <div style="margin-top: 20px;">
            <div v-if="product.sellingType === '선착순'">
              <div style="display:flex;align-items: center;">
                <div style="text-align: center;width:80px;">가격</div>
                <input
                  v-model="product.price"
                  id="price"
                  style="margin-left:30px;width:50%"
                  type="number"
                  maxlength="40"
                  required
                  placeholder="가격을 입력해주세요"
                />
                <span>&nbsp;원</span>
              </div>
              <hr />
              <div style="display:flex;align-items: center;">
                <div style="text-align: center;width:80px;">판매기간</div>
                <div style="display:flex; flex-direction: column;margin-left:30px;width:80%">
                  <input v-model="product.openTime" style="width:50%" type="datetime-local" required />
                  <span>&nbsp;~&nbsp;</span>
                  <input v-model="product.closeTime" style="width:50%" type="datetime-local" required />
                </div>
              </div>
  
              <div style="margin-left:110px;margin-top:10px; font-size: 10px; color: gray">
                - 홈페이지 정책상 최대 하루까지 설정가능합니다
              </div>
            </div>
            <div v-else-if="product.sellingType === '경매'">
              <div style="display:flex;align-items: center;">
                <div style="text-align: center;width:80px;">초기입찰가</div>
                <input
                  v-model="product.price"
                  style="margin-left:30px;width:30%"
                  type="text"
                  maxlength="40"
                  required
                  placeholder="가격을 입력하세요"
                />
                <span>&nbsp;원</span>
              </div>
              <div style="display:flex;align-items: center;margin-top:10px;">
                <div style="text-align: center;width:80px;">입찰단위</div>
                <input
                  v-model="product.bidUnit"
                  style="margin-left:30px;width:30%"
                  type="text"
                  maxlength="40"
                  required
                  placeholder="입찰단위를 입력하세요"
                />
                <span>&nbsp;원</span>
              </div>
              <div style="margin-left:110px; margin-top:5px; font-size: 10px; color: gray">
                - 홈페이지 정책상 초기 입찰가의<br />1.5배까지만 낙찰 가능합니다.
              </div>
  
              <hr />
              <div style="display:flex;align-items: center;">
                <div style="text-align: center;width:80px;">판매시작일</div>
                <input
                  v-model="product.openTime"
                  style="margin-right:10px;margin-left:30px;width:40%"
                  type="datetime-local"
                  required
                />
                <!-- <span>&nbsp;~&nbsp;</span>-->
                <!-- <input v-model="product.closeTime" style="width:30%" type="datetime-local" required>  -->
              </div>
              <div style="display:flex;">
                <div style="text-align: center;width:80px;margin-top:10px;">경매시간</div>
                <select
                  v-model="auctionTime"
                  @change="updateAuctionCloseTime"
                  style="margin-right:10px;margin-left:30px;width:40%; margin-top:10px; height:27px;"
                >
                  <option value="1">1시간</option>
                  <option value="2">2시간</option>
                  <option value="3">3시간</option>
                </select>
              </div>
              <div style="margin-left:110px;margin-top:10px; font-size: 10px; color: gray">
                - 홈페이지 정책상 최대 3시간까지 설정가능합니다
              </div>
            </div>
          </div>
          <div v-if="product.sellingType === '무료나눔'">
            <div style="display:flex;align-items: center;">
              <div style="text-align: center;width:80px;">나눔기간</div>
              <input v-model="product.openTime" style="display:none;" type="datetime-local" required />
              <select
                v-model="sharingDays"
                @change="updateCloseTime"
                name="sharingTime"
                style="width:30%; margin-left:30px;height:30px;"
              >
                <option value="1" selected>1일</option>
                <option value="3">3일</option>
                <option value="5">5일</option>
                <option value="7">7일</option>
              </select>
            </div>
          </div>
          <hr />
  
          <h2 style="margin-top:40px;"><b>추가정보</b></h2>
          <hr class="hr_menu" />
          <div style="display:flex;align-items: center;">
            <div style="text-align: center;width:80px;">수량</div>
            <input
              v-model="product.quantity"
              id="quantity"
              style="margin-left:30px;width:30%"
              type="number"
              required
              placeholder="수량을 입력해주세요"
            />
            <span>&nbsp;개</span>
          </div>
          <hr />
  
          <!-- <div
            style="display:flex; justify-content: flex-end; background-color: lightgray; height: 80px; align-items: center; position:sticky; bottom:0; z-index:10"
          >
            <button style="width:100px; height:40px; border:2px solid lightgrey; border-radius:5px; cursor: pointer;">
              임시저장
            </button>
            <button
              type="submit"
              style="margin-left:10px;width:100px; height:40px; border:2px solid orange; border-radius:5px; background-color: yellow; cursor: pointer;"
            >
              등록하기
            </button>
          </div> -->
        <div class="button-container">
          <button class="temp-save-btn">임시저장</button>
          <button type="submit" class="submit-btn">등록하기</button>
        </div>
        </form>
      </div>
      <!-- 광고 영역 -->
      <div
        id="ad-section"
        style="width: 25%; background-color: #f2f2f2; padding: 20px; border: 1px solid #ddd; 
        position: relative; right: -160px; "
      >
        <h3>광고</h3>
        <div style="margin-top: 20px;">
          <img src="@/assets/images/kosa_msa.jpg" alt="광고 이미지" style="width: 100%;" />
          <!-- <p>여기에 광고 내용을 추가하세요</p> -->
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import imageCompression from 'browser-image-compression';
  import { mapGetters } from 'vuex';
  
  export default {
    data() {
      return {
        shippingOption: 'included', //초기상태를 '배송비별도'로 설정
        purchasePrice: '', //즉시구매 가격을 저장하는 변수
        images: [], //이미지 데이터를 저장할 변수
        productName: '', // 입력된 텍스트를 저장하는 데이터
        textLength: 0, //상품명 글자수
        descriptionTextLength: 0,
        sharingDays: '1',
        file: [], // 파일 목록
        auctionTime: 3,
        //상품
        product: {
          title: '',
          uniqueImg: 'https://picsum.photos/200', //dummy로 하나 넣어서 보내보자
          price: 0,
          description: '',
          quantity: 0,
          isUsed: 'T',
          sellingType: '선착순', // 초기값을 '선착순'으로 설정
          isDirect: 'F',
          shippingCost: 0,
          closeTime: null,
          openTime: null,
          bidUnit: 500,
          //'new Date().toISOString()', // 현재 시간을 기본값으로 설정
          memberId: this.$store.getters.getEmail,
        },
      };
    },
    computed: {
      // 현재 시각을 반환하는 computed 속성
      currentDateTime() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day}T${hours}:${minutes}`;
      },
      ...mapGetters(['isAuthenticated']),
    },
    watch: {
      'product.openTime': 'updateCloseTime',
      sharingDays: 'updateCloseTime',
      'product.sellingType': function (newType) {
        if (newType === '무료나눔') {
          this.$set(this.product, 'openTime', this.currentDateTime);
          this.updateCloseTime(); // openTime이 업데이트된 후 closeTime을 계산
        }
      },
      auctionTime(newValue) {
        if (this.product.sellingType === '경매') {
          this.updateAuctionCloseTime();
        }
      },
    },
    mounted() {
      // 컴포넌트가 마운트되었을 때, 초기 값 설정
      if (this.product.sellingType === '무료나눔') {
        this.$set(this.product, 'openTime', this.currentDateTime);
        this.updateCloseTime();
      }
      // if(this.isAuthenticated){
      //     this.memberId
      // }
    },
    methods: {
      //파일 선택창 열기
      triggerFileInput() {
        this.$refs.fileInput.click();
      },
      //파일 선택 후 미리보기
      previewImage(event) {
        const files = event.target.files;
        if (files.length) {
          // this.images = []; // 기존 이미지를 초기화
          Array.from(files).forEach((file) => {
            const reader = new FileReader();
            reader.onload = (e) => {
              this.images.push(e.target.result); // 이미지 데이터를 배열에 추가
            };
            reader.readAsDataURL(file);
          });
          this.file.push(...files); // 파일 정보를 저장
        }
      },
      //파일 업로드
  
      // 텍스트 길이를 업데이트하는 메서드
      updateTextLength() {
        this.textLength = this.product.title.length;
      },
      updateDescriptionTextLength() {
        this.descriptionTextLength = this.product.description.length;
      },
      removeImage(index) {
        this.images.splice(index, 1); // 해당 인덱스의 이미지를 배열에서 제거
        this.file.splice(index, 1);
      },
      // 경매 시 closeTime을 경매 시간에 맞춰 설정하는 메서드
      updateAuctionCloseTime() {
        if (!this.product.openTime) return;
  
        const openTime = new Date(this.product.openTime);
        const hoursToAdd = parseInt(this.auctionTime, 10);
  
        if (isNaN(openTime.getTime()) || isNaN(hoursToAdd)) {
          this.product.closeTime = ''; // 유효하지 않은 입력일 경우 closeTime을 비웁니다.
          return;
        }
  
        // 시간에 경매 시간을 추가합니다.
        openTime.setHours(openTime.getHours() + hoursToAdd);
  
        // 날짜를 "YYYY-MM-DDTHH:MM" 형식으로 변환합니다.
        const year = openTime.getFullYear();
        const month = String(openTime.getMonth() + 1).padStart(2, '0');
        const day = String(openTime.getDate()).padStart(2, '0');
        const hours = String(openTime.getHours()).padStart(2, '0');
        const minutes = String(openTime.getMinutes()).padStart(2, '0');
        this.product.closeTime = `${year}-${month}-${day}T${hours}:${minutes}`;
      },
      //무료나눔에서 closeTime계산
      updateCloseTime() {
        if (!this.product.openTime) return;
  
        const openTime = new Date(this.product.openTime);
        const daysToAdd = parseInt(this.sharingDays, 10);
  
        if (isNaN(openTime.getTime()) || isNaN(daysToAdd)) {
          this.product.closeTime = ''; // 유효하지 않은 입력일 경우 closeTime을 비웁니다.
          return;
        }
  
        // 날짜에 기간을 추가합니다.
        openTime.setDate(openTime.getDate() + daysToAdd);
  
        // 날짜를 "YYYY-MM-DDTHH:MM" 형식으로 변환합니다.
        const year = openTime.getFullYear();
        const month = String(openTime.getMonth() + 1).padStart(2, '0');
        const day = String(openTime.getDate()).padStart(2, '0');
        const hours = String(openTime.getHours()).padStart(2, '0');
        const minutes = String(openTime.getMinutes()).padStart(2, '0');
        this.product.closeTime = `${year}-${month}-${day}T${hours}:${minutes}`;
      },
      //상품등록
      async saveProduct() {
        const formData = new FormData();
        formData.append('title', this.product.title);
        formData.append('price', this.product.price);
        formData.append('description', this.product.description);
        formData.append('quantity', this.product.quantity);
        formData.append('isUsed', this.product.isUsed);
        formData.append('sellingType', this.product.sellingType);
        formData.append('isDirect', this.product.isDirect);
        formData.append('shippingCost', this.product.shippingCost);
        formData.append('closeTime', this.product.closeTime);
        formData.append('openTime', this.product.openTime);
        formData.append('memberId', this.product.memberId);
        formData.append('bidUnit', this.product.bidUnit);
  
        for (let i = 0; i < this.file.length; i++) {
          const compressed = await imageCompression(this.file[i], {
            initialQuality: 0.5,
            fileType: 'image/jpeg',
          });
          formData.append('files', compressed);
        }
        const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
        const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';
        axios
          .post('/api/products', formData, {
            headers: {
              'X-CSRF-TOKEN': csrfToken,
              Authorization: `Bearer ${localStorage.getItem('token')}`, // OAuth 토큰
            },
            withCredentials: true,
          })
          .then((response) => {
            this.$router.push('/');
            alert('상품등록에 성공하셨습니다');
          })
          .catch((error) => {
            console.error('상품 등록 중 오류가 발생했습니다: ', error);
          });
  
        let fileData = new FormData();
  
        // 파일을 'files'라는 키로 formData에 추가
        // for (let i = 0; i < this.$refs.fileInput.files.length; i++) {
        //     fileData.append('files', this.$refs.fileInput.files[i]);
        // }
  
        // // 서버로 파일 전송
        // axios.post('/api/upload-to-google', fileData, {
        //     headers: {
        //         'X-CSRF-TOKEN': csrfToken,
        //         Authorization: `Bearer ${localStorage.getItem("token")}`, // OAuth 토큰
        //         'Content-Type': 'multipart/form-data' // multipart/form-data 설정
        //     },
        //     withCredentials: true
        // })
        // .then(response => {
        //     console.log(response.data);
        //     alert("파일 업로드에 성공하셨습니다");
        // })
        // .catch(error => {
        //     console.error('파일 업로드 중 오류가 발생했습니다: ', error);
        // });
      },
    },
  };
  </script>
  
  <style scoped>
  @font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf'); /* 실제 폰트 파일 경로를 사용하세요 */
  }
  
  body,
  h1,
  h2,
  h3,
  p,
  div,
  span,
  input,
  button,
  textarea,
  select {
    font-family: 'hanna', sans-serif; /* 'hanna' 폰트를 기본 폰트로 설정하고, 대체 폰트로 sans-serif를 설정합니다. */
  }
  
  #content {
    width: 80%;
    margin: auto;
  }
  
  .hr_menu {
    border: 2px solid black;
  }
  
  form {
    width: 100%;
  }
  
  #ad-section {
    width: 25%;
    background-color: #f2f2f2;
    padding: 20px;
    border: 1px solid #ddd;
  }
  .button-container {
  display: flex;
  justify-content: flex-end;
  background-color: white; /* 배경색을 흰색으로 변경 */
  border-top: 2px dashed #ccc; /* 점선으로 구분 */
  padding: 20px; /* 상하 여백 추가 */
  align-items: center;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

.temp-save-btn {
  width: 100px;
  height: 40px;
  border: 2px solid lightgrey;
  border-radius: 5px;
  cursor: pointer;
  background-color: white;
}

.submit-btn {
  margin-left: 10px;
  width: 100px;
  height: 40px;
  border: 2px solid black;
  border-radius: 5px;
  background-color:  wheat;
  cursor: pointer;
}
  </style>
  