<template>
    <div style="margin-left:10%; margin-right:10%;font-family: 'hanna';">
        <hr>
        <h3>판매내역</h3>
        <hr>
        <div class="boxShadow" v-for="(product, index) in sortedProductList" :key="index" >
            <p v-if="loading">Loading...</p>
            <p v-else-if="error">{{ error }}</p>
            <p v-else-if="productList.length===0">판매내역이 없습니다</p>
            <div v-else>
                <div style="display:flex; justify-content: space-between; align-items: center;">
                    <div :style="getTextColor(product)"><b style="font-size:25px;">판매기간: {{formatDate(product.openTime)}} ~ {{formatDate(product.closeTime)}}</b></div>
                    <div style="display:flex; align-items: center;">
                        수량: {{ product.quantity }}
                        <div class="isSoldOut" :style="getBackgroundColor(product)">{{ canSell(product) }}</div>
                    </div>
                </div>
                <div style="display:flex; cursor: pointer;" @click="openModal(product)">
                    <div>  
                        <!-- 이미지 -->
                        <div style="width:100px; height:100px; background-color: gray;">
                            <img :src="'https://storage.googleapis.com/ellie_bucket98/'+product.uniqueImg" style="width:inherit; height:inherit;">
                        </div>
                    </div>
                    <div style="padding-left:10px;width:100% ">
                        <div >{{ getSaleStatus(product) }}</div>
                        <div style="font-size:20px; display:flex; align-items: center;">
                            {{product.title}}
                            <!-- <button v-if="product.buyingDetails.length==0"
                                style="margin:0px!important;background-color: red;color:white; font-size:12px;">
                                판매글 삭제
                            </button> -->
                        </div>
                        <div><b>{{product.price}}원</b></div>
                        <div v-if="product.buyingDetails && product.buyingDetails.length==0">
                            <div style="display:flex;justify-content: space-between;align-items: center;">
                                <span>구매자가 존재하지 않습니다.</span>
                            </div>
                        </div>
                        <div v-if="product.buyingDetails && product.buyingDetails.length>1">
                            상태 : 구매자가 여러명입니다
                        </div>
                        <div v-if="product.buyingDetails && product.buyingDetails.length==1">
                            <div v-for="(detail, index) in product.buyingDetails" :key="index"> 
                                상태 : {{ namingStatus(detail) }}
                            </div>
                        </div> 
                    </div>
                </div>
                <div v-for="(detail, index) in product.buyingDetails" :key="index">
                    <div v-if="detail.state!=0">
                        <div style="text-align:center; margin-top:10px;">
                            <table style="width:100%; border: 1px solid lightgray;">
                                <thead>
                                    <th width="3%">번호</th>
                                    <th width="5%">구매자</th>
                                    <th width="10%">이메일</th>
                                    <th width="15%">구매시간</th>
                                    <th width="20%">배송지</th>
                                    <th width="27%">구매메모</th>
                                    <th width="10%">구매상태</th>
                                    <th width="10%">상태update</th>
                                </thead>
                                <tbody>
                                    <td>{{ detail.pbId }}</td>
                                    <td>{{ detail.memberName}}</td>
                                    <td>{{ detail.email }}</td>
                                    <td>{{formatDate(detail.purchaseTime) }}</td>
                                    <td>{{ detail.address }}</td>
                                    <td>{{ detail.buyingMemo }}</td>
                                    <td>{{ namingStatus(detail) }}</td>
                                    <td v-if="detail.state<4">
                                        <select  v-model="detail.selectedValue">
                                            <option v-if="detail.state<=1" value="2">배송중</option>
                                            <option v-if="detail.state<=2" value="3">배송완료</option>
                                            <option v-if="detail.state<=3" value="4">배송확정</option>
                                        </select><br>
                                        <button  @click="updateProductStatus(detail)">update</button>
                                    </td>
                                    <td v-else>
                                        배송단계종료
                                    </td>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
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
    import axios from 'axios';
    import ProductDetail from "@/components/productDetail/ProductDetail"; // 모달 컴포넌트 임포트
    
    export default {
        data(){
            return{
                productBuyingDetails: [],
                memberEmail : this.$store.getters.getEmail,
                productList:[],
                productId: 0,
                state: 0,
                loading: true,
                error: null,
                showDetails: false, // 상세 정보 표시 여부
                isModalVisible: false, // 모달 표시 여부
                selectedProduct: {}, // 선택된 상품의 데이터
                canBuy: true, // 구매 가능 여부 (상황에 따라 설정)
            }
        },
        components: {
            ProductDetail, // 모달 컴포넌트 등록
        },
        computed: {
            sortedProductList() {
            return this.productList.slice().sort((a, b) => {
                return new Date(b.openTime) - new Date(a.openTime);
            });
            },
        },
        mounted(){
            this.fetchMemberData();
        },
        methods:{
            formatDate(dateString) {
                const options = { year: 'numeric', month: 'long', day: 'numeric', hour: "numeric", minute: "numeric" };
                return new Date(dateString).toLocaleDateString(undefined, options);
            },
            async fetchMemberData(){
                this.loading = true;
                this.error = null;

                try{
                    const memberEmail = this.memberEmail;
                    const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
                    const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';
                    //서버에 요청을 보내고 응답을 처리
                    const productResponse = await axios.post('/api/productList', null, {
                        params: {memberEmail: memberEmail},
                        headers: {
                            'X-CSRF-TOKEN': csrfToken,
                            Authorization: `Bearer ${localStorage.getItem("token")}` // OAuth 토큰
                        },
                        withCredentials: true
                    });

                    // ProductBuying과 product를 가져옴
                    const buyingResponse = await axios.post('/api/productBuyDetails', null, {
                        params: { memberEmail: memberEmail },
                        headers: {
                            'X-CSRF-TOKEN': csrfToken,
                            Authorization: `Bearer ${localStorage.getItem("token")}`
                        },
                        withCredentials: true
                    });
                    
                    //응답이 성공적일 경우
                    if(productResponse.status === 200){
                        this.productList = productResponse.data; // 성공적으로 데이터를 가져온 경우
                    } else {
                        this.error = 'Failed to load product details.';
                    }

                    //productBuyingResponse
                    if (buyingResponse.status === 200) {
                        const buyingData = buyingResponse.data;
                        this.productBuyingDetails = buyingData.map(item => ({
                            productId: item[0], 
                            state: item[1],
                            pbId: item[2],
                            memberName : item[3],
                            purchaseTime : item[4],
                            address: item[5],
                            buyingMemo : item[6], 
                            email: item[7]
                        }));

                        this.productList.forEach(product => {
                        })

                        this.productBuyingDetails.forEach((buyingDetail, index) => {
                        })

                        this.productList.forEach(product => {
                            // Get all buying details that match the product ID
                            const matchingBuyings = this.productBuyingDetails.filter(buying => buying.productId === product.id);
                            
                            // Store all matching buyings in an array
                            product.buyingDetails = matchingBuyings.map(buying => ({
                                state: buying.state,
                                pbId: buying.pbId,
                                memberName: buying.memberName,
                                purchaseTime: buying.purchaseTime,
                                address: buying.address,
                                buyingMemo: buying.buyingMemo,
                                email: buying.email
                            }));

                        });

                    } else {
                        this.error = 'Failed to load product buying details.';
                    }


                }catch(error){
                    if (error.response) {
                        // 서버가 응답을 반환했으나 에러가 발생한 경우
                        if (error.response.status === 404) {
                            this.error = 'Member not found.';   
                        } else {
                            this.error = 'An unexpected error occurred.';
                        }
                    } else {
                        // 서버에 요청을 보내지 못한 경우
                        this.error = 'Failed to communicate with the server.';
                    }
                } finally {
                    this.loading = false;
                }
                
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
            isSaleEnded(closeTime) {
                const now = new Date();
                const endTime = new Date(closeTime);
                return now > endTime;
            },
            getSaleStatus(product) {
                if (this.isSaleEnded(product.closeTime)) {
                    if(product.quantity >0){
                        return '판매불가'
                    }
                    return '판매종료';
                }  else {
                    if(product.quantity >0){
                        return '판매중';
                    }
                    else{
                        return '판매완료'
                    }
                }
            },
            canSell(product){
                if(product.quantity>0){
                    return '재판매가능';
                }else{
                    return '완판';
                }
            },
            getBackgroundColor(product){
                if (product.quantity > 0) {
                    return { backgroundColor: 'blue', color: 'white' }; // 완판 시 파란색 배경
                } else {
                    return { backgroundColor: 'yellow', color: 'black' }; // 판매 가능 시 빨간색 배경
                }
            },
            getTextColor(product){
                const now = new Date(); // 현재 시각
                const openTime = new Date(product.openTime);
                const closeTime = new Date(product.closeTime);

                if (now > closeTime || now < openTime) {
                    // 현재 시각이 closeTime을 지났거나 openTime 이전이면 lightgray
                    return { color: 'lightgray' };
                } else {
                    // 그 외의 경우는 black
                    return { color: 'black' };
                }
            },
            namingStatus(detail){
                if(detail.state==1){
                    return '결제완료';
                }else if(detail.state==2){
                    return '배송중';
                }else if(detail.state==3){
                    return '배송완료';
                }else if(detail.state==4){
                    return '구매확정';
                }else{
                    return '구매자가 없습니다';
                }
            },
            updateProductStatus(detail) {
                // 선택된 값을 서버로 전송하는 함수
                axios.post('/api/updateProductStatusSelected', {
                    pbId: detail.pbId, // 서버로 전송할 데이터 (제품 ID)
                    newState: detail.selectedValue // 사용자가 선택한 새 상태 값
                })
                .then(response => {
                    // 서버 응답에 따라 추가 작업을 수행할 수 있음
                    alert("배송상태 등록이 완료되었습니다");
                })
                .catch(error => {
                    console.error('Error updating product status:', error);
                    // 에러 처리
                });
            },
        }
    }
    </script>
    
    <style scoped>
    @font-face {
        font-family: 'hanna';
        src: url('../../assets/fonts/GodoM.ttf');
    }

    .boxShadow{
        border-radius: 10px; 
        border: 1px solid lightgray; 
        box-shadow:0 4px 16px rgba(0,0,0,0.25);
        padding: 20px;
        margin-bottom:20px;
    }
    .details {
        padding: 10px;
        border-top: 1px solid lightgray;
    }
    
    .slide-fade-enter-active, .slide-fade-leave-active {
        transition: max-height 0.5s ease;
    }
    
    .slide-fade-enter, .slide-fade-leave-to {
        max-height: 0;
        opacity: 0;
    }
    .isSoldOut{
        border-radius: 10px;
        border:1px solid lightgray;
        margin-left:10px;
        padding:5px;
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

    .active .status-icon {
        background-color: #34c759;
        border: 2px solid #34c759;
    }
    table{
        border-collapse: collapse;
        box-shadow: 0 0 10px white;
        border-radius: 10px;
    }
    th,td{
        border: 1px solid wheat;
        border-style: double;
        border-width: unset;
    }
    button{
        border-radius: 20px;
        border-style: revert-layer;
        border-color: wheat;
        background-color: wheat;
        margin-top: 10px;
        margin-bottom: 10px;
    }
    th{
      background-color:  wheat;  

    }
    td{
        background-color: #ffffff;
    }
    </style>
    