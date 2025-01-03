<template>
<div id="content">
    <div class="main">
        <div class="feeds">
            <div class="leftmenu" >
                <b style="font-size:35px;">배송지</b>
                <div style="height:1px; background-color: gray;margin-bottom:10px;"></div>
                <div style="display:flex; margin-bottom:10px;">
                    <input type="text" v-model="postcode" placeholder="우편번호">
                    <!-- 스크립트 로딩 완료 후에만 버튼을 활성화합니다 -->
                    <input type="button" @click="execDaumPostcode" value="우편번호 찾기" :disabled="!isScriptLoaded"><br>
                </div>
                <div style="display:flex; flex-direction: column;">
                    <div style="display:flex; margin-bottom:5px;">
                        <input style="width:70%; margin-right:10px;" type="text" v-model="jibunAddress" placeholder="지번주소">
                        <input type="text" v-model="detailAddress" placeholder="상세주소">
                    </div>
                    <input type="text" v-model="extraAddress" placeholder="추가 주소를 입력하세요">
                </div>
            </div>

            <div class="leftmenu">
                <b style="font-size:35px;">주문상품</b>
                <div style="height:1px; background-color: gray;margin-bottom:10px;"></div>
                
                <!-- Iterate over selectedProducts -->
                <div v-for="(product, index) in selectedProducts" :key="index" style="display:flex; margin-bottom:20px;">
                
                <!-- Product Image -->
                <div style="width:100px;height:100px; background-color: lightblue;">
                    <img :src="'https://storage.googleapis.com/ellie_bucket98/'+product.uniqueImg" style="width:inherit; height:inherit" alt="">
                </div>
                
                <!-- Product Details -->
                <div style="width:80%; align-content:center;margin-left:20px;">
                    <div style="display:flex;justify-content:space-between;align-items: baseline;">
                    <div style="font-size:18px">{{ product.title }}</div>
                    
                    </div>
                    <div>배송비: {{ product.shippingCost }}원</div>
                    <div>가격: {{ product.price }} 원</div>
                    <div class="productMemo">
                        
                        <textarea 
                            v-model="productBuying[index].buyingMemo"
                            cols="30" 
                            rows="5" 
                            style="width:100%; text-align:left; 
                            resize:none" 
                            placeholder="택배 배송이 아닐 시 (직거래/기프티콘거래)에 남기실 메모를 적어주세요.
예시 ) 카카오톡 아이디 : kakaotalkId0825 / 직거래 장소 협의는 이메일로 부탁드립니다

택배 배송시, 배송 주소지를 정확히 기입하시고, 필요한 메모를 여기에 남겨주세요.
예시) 초인종을 울려주세요."></textarea>
                    </div>
                    
                </div>
                
                </div>
            </div>
            <div class="leftmenu">
                <b style="font-size:35px;">TM 포인트•머니</b>
                <div style="height:1px; background-color: gray;margin-bottom:10px;"></div>
                <div style="display:flex; justify-content: space-between;margin:20px 0px;">
                    <div>보유</div>
                    <div>{{wallet}} 원</div>
                </div>
                <div style="display:flex; justify-content: space-between;">
                    <div>사용</div>
                    <div style="display:flex;">
                        <div><input type="number" v-model="pointsToUse" /><span style="margin-right:10px;">&nbsp;원</span></div>
                        <button @click="useFullAmount" style="border-radius: 10px;">전액사용</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="right">
            <b>결제상세</b>
            <div class="rightInBox">
                <div>총 결제</div>
                <div>{{amount}} 원</div>
            </div>
            <br>
            <b>포인트 혜택</b>
            <div class="rightInBox">
                <div>구매 적립</div>
                <!-- <div>{{ (amount)*0.01 }} 포인트</div> -->
                <div>{{ (totalCost) * 0.01 }} 포인트</div> <!-- totalCost를 사용 -->

            </div>
        </div>
    </div>

    <div style="display:flex;
                justify-content: space-between;
                background-color: white;
                height: 80px;
                align-items: center;
                position:sticky;
                bottom:0;
                padding-left:5%;
                padding-right:5%;
                box-shadow: 0px -4px 16px 0px rgba(0,0,0,.1);
                z-index:10 ">
        <div>약관 및 주문 내용을 확인하였으며, 정보 제공 등에 동의합니다.</div>
        <button type="submit" @click="submitProductBuying"
            style="margin-left:10px;width:30%; height:40px; border:2px solid orange; 
            border-radius:5px; background-color: yellow; cursor: pointer; ">
                <b src="https://payapplite.com/l/SaR8yH">결제하기</b>
        </button>
    </div>

</div>
</template>

<script>
import axios from 'axios';
import EventBus from '@/util/eventBus'; // EventBus의 실제 경로로 변경


export default {
    props: {
        totalCost: {
            type: Number,
            required: true,
        },
        selectedProducts: {
            type: Array,
            required: true,
            default: () => []  // 기본 값으로 빈 배열 설정

        },
    },
    data() {
        return {
            memberEmail: this.$store.getters.getEmail,
            postcode: '',
            jibunAddress: '',
            detailAddress: '',
            extraAddress: '',
            guideText: '',
            isScriptLoaded: false,
            product: {},
            wallet: 0,
            imageSrc: '',
            amount: this.totalCost,
            productBuying: [],
            pointsToUse: 0, // pointsToUse 초기화

            
        };
    },
    created() {
        this.fetchMemberWallet();
        // this.totalCost = totalCost;
    },
    mounted() {
        this.loadDaumPostcodeScript();
        this.setProducts();
    },
    computed: {
        
        getFullAddress(){
            return this.jibunAddress + " " + this.detailAddress + " " + this.extraAddress;
        },
    },
    methods: {
        setProducts() {
            const e = this.memberEmail;
            this.productBuying = []; // 초기화
            if (!this.selectedProducts || !Array.isArray(this.selectedProducts)) {
                console.error('selectedProducts 배열이 유효하지 않습니다:', this.selectedProducts);
                return;
            }
            for (const sp of this.selectedProducts) {
                const product = {
                    quantity: 1,
                    purchaseTime: null,
                    purchasePrice: sp.price + sp.shippingCost,
                    email: e,
                    productId: sp.id,
                    buyingMemo: '', // buyingMemo 초기화
                    address: '',
                };
                this.productBuying.push(product);
            }
        },
        async fetchMemberWallet(){
            try{
                const memberEmail = this.memberEmail;
                const response = await axios.get(`/api/bucket/wallet/${memberEmail}`);
                if(response.status === 200){
                    this.wallet = response.data;
                } else {
                    this.wallet = 0;
                }
            } catch (error) {console.error('error')}
            

        },
        // useFullAmount(){
        //     if(this.wallet > this.amount) {
        //         this.wallet -= this.amount;
        //         this.toamounttalCost = 0;
        //     } else {
        //         this.amount = this.wallet;
        //     }
        // },
       
        async useFullAmount() {
            // 사용자가 가진 포인트를 전액 사용하도록 설정
            // if (this.wallet > this.amount) {
            //     this.amount = 0;
            //     this.wallet -= this.amount;
            // } else {
            //     this.amount -= this.wallet;
            //     this.wallet = 0;
            // }
            if (this.wallet > this.amount) {
                this.pointsToUse = this.amount; // 결제 금액만큼 포인트 사용
                this.wallet -= this.amount;
                this.amount = 0;
            } else {
                this.pointsToUse = this.wallet; // 가지고 있는 포인트만큼 사용
                this.amount -= this.wallet;
                this.wallet = 0;
            }

            // 서버에 포인트 사용 요청
            try {
                const response = await axios.post(
                    `/api/usePoints?memberEmail=${this.memberEmail}&pointsToUse=${this.pointsToUse}`, // 사용자가 실제로 사용할 포인트를 서버로 전송
                    null,
                    {
                        headers: {
                            'X-CSRF-TOKEN': this.csrfToken,
                            Authorization: `Bearer ${localStorage.getItem("token")}`,
                        },
                        withCredentials: true,
                    }
                );

                if (response.status === 200) {
                    EventBus.$emit('update-wallet');
                } else {
                    console.error('포인트 사용 실패');
                    alert('포인트 사용에 실패했습니다.');
                }
            } catch (error) {
                console.error('Error using points:', error);
                alert('포인트 사용 중 오류가 발생했습니다.');
            }
        },
        loadDaumPostcodeScript() {
            const script = document.createElement('script');
            script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
            script.onload = () => {
                this.isScriptLoaded = true;
            };
            script.onerror = () => {
                console.error("Daum Postcode 스크립트를 로드하는 데 실패했습니다.");
            };
            document.head.appendChild(script);
        },
        execDaumPostcode() {
            if (window.daum && window.daum.Postcode) {
                new window.daum.Postcode({
                    oncomplete: (data) => {
                        this.postcode = data.zonecode;
                        this.roadAddress = data.roadAddress;
                        this.jibunAddress = data.jibunAddress;
                        this.detailAddress = data.buildingName;
                        this.extraAddress = data.extraAddress;
                    }
                }).open();
            } else {
                console.error("Daum Postcode 스크립트가 로드되지 않았습니다.");
            }
        },


        // async submitProductBuying() {
        //     // 팝업 창 띄우기
        //     const popup = window.open(
        //         'https://payapplite.com/l/SaR8yH', // 결제 URL
        //         'payapplite-popup',
        //         'width=800,height=600' // 팝업 창 크기 설정
        //     );

        //     // 팝업이 닫히는 것을 감지
        //     const popupInterval = setInterval(async () => {
        //         if (popup.closed) {
        //             clearInterval(popupInterval); // interval 중지

        //             // 팝업이 닫혔을 때 데이터베이스에 요청
        //             let success = true;
        //             for (let p of this.productBuying) {
        //                 p.address = this.getFullAddress; // 이 값을 제대로 설정했는지 확인
        //                 p.purchaseTime = new Date();

        //                 const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
        //                 const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';

        //                 try {
        //                     const response = await axios.post('/api/submitProductBuy', p, {
        //                         headers: {
        //                             'X-CSRF-TOKEN': csrfToken,
        //                             'Authorization': `Bearer ${localStorage.getItem("token")}`,
        //                         },
        //                         withCredentials: true
        //                     });
        //                 } catch (error) {
        //                     success = false;
        //                 } 
        //             }

        //             // 요청이 성공적으로 완료되었는지 확인
        //             if (success) {
        //                 alert('결제가 완료되었습니다.');
        //                 this.$router.push('/buy-history'); // 결제 후 이동
        //             } else {
        //                 alert('결제중 오류가 발생하였습니다. 다시 시도해 주세요.');
        //             }
        //         }
        //     }, 500); // 0.5초마다 팝업이 닫혔는지 확인
// }

        async submitProductBuying() {
        // 팝업 창 띄우기
        const popup = window.open(
            'https://payapplite.com/l/SaR8yH', 
            'payapplite-popup',
            'width=800,height=600' 
        );

        // 팝업이 닫히는 것을 감지
        const popupInterval = setInterval(async () => {
            if (popup.closed) {
                clearInterval(popupInterval); // interval 중지

                let success = true;
                for (let p of this.productBuying) {
                    p.address = this.getFullAddress; 
                    p.purchaseTime = new Date();

                    const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
                    const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';

                    try {
                        const response = await axios.post('/api/submitProductBuy', p, {
                            headers: {
                                'X-CSRF-TOKEN': csrfToken,
                                'Authorization': `Bearer ${localStorage.getItem("token")}`,
                            },
                            withCredentials: true
                        });
                    } catch (error) {
                        success = false;
                    } 
                }

                // 포인트 소모 후 처리
                if (this.wallet > 0) {
                    await this.useFullAmount();
                }

                if (success) {
                    alert('결제가 완료되었습니다.');
                    this.$router.push('/buy-history');
                } else {
                    alert('결제중 오류가 발생하였습니다. 다시 시도해 주세요.');
                }
            }
        }, 500);
    }
}
};
</script>


<style scoped>
.main{
    display: flex;
    justify-content: center;
    background-color: rgb(232, 232, 232);
    margin-left:10%;
    margin-right:10%;
}
.feeds{
    width: 90%;
    background-color: rgb(232, 232, 232);
    height:1300px;
    padding-top:30px;
    padding-left: 2.5%;
    padding-right:2.5%;

}
.right{
    position: sticky;
    top: 84px;
    width: 100%;
    max-width: 300px;
    height: 250px;
    background-color: rgb(232, 232, 232);
    padding-left:10px;
    padding-right:10px;
    padding-top: 30px;
}
.rightInBox{
    padding: 5px;
    background-color: white; 
    border-radius: 10px;
    display:flex; 
    justify-content: space-between; 
    height:50px; 
    text-align: center; 
    align-items: center; 
}
@media(max-width:1200px){
    .main{
        flex-direction: column;
    }
    .right{
        width:90%;
        max-width:100%;
    }
}
.leftmenu{
    background-color: white;padding:5%; border-radius:10px;margin-bottom:20px;
}
</style>