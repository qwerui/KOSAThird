<template>
    <div class="joined-auction">
        <p class="description-title">경매 현황
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            입찰 인상 폭: {{  bidunit  }}원</p>
        <div class="detail-description">
            <p class="selected-description" v-for="msg in messages" :key="msg">{{ msg }}</p>
        </div>
        <div class="join-button">
            <p class="current-price-info">현재 가격 : {{ currentPrice }}</p>
            <p class="current-price-info">현재 낙찰 예정자 : {{ highestBidder }}</p>
            <button class="join-btn" @click="joinAuction">입찰하기</button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: "ProductDetail",
    props: {
        productId: {
            type: Number,
            required: true,
        },
        startPrice: {
            type: Number,
            required: true,
        },
        bidUnit: {
            type: Number,
            required: true,
        },
        joinMember: {
            type: String,
            required: true,
        },
        closeTime: {
            type: String, // 날짜와 시간을 문자열로 받을 것으로 가정합니다 (ISO 8601 포맷)
            required: true,
        }
        
    },    
    data() {
        return {
            currentPrice: this.startPrice - this.bidUnit,
            currentBidder: this.joinMember,
            highestBidder: '아직 없음.',
            websocketClient: null,
            messages: ['경매가 시작되었습니다.'],
            bidunit: this.bidUnit,
            closeDateTime: new Date(this.closeTime), // closeTime을 Date 객체로 변환
        };
    },

    methods: {
        getCookie(name) {
            const value = `; ${document.cookie}`;
            const parts = value.split(`; ${name}=`);
            if (parts.length === 2) return parts.pop().split(';').shift();
        },
        // 경매 현황을 가져오는 함수
        fetchAuctionData() {
            axios.get(`/api/${this.productId}`)
                .then(response => {
                    const auctions = response.data;
                    
                    this.messages = auctions.map(auction => 
                        `${auction.price}원을 ${auction.userEmail}님이 제시하셨습니다. ${new Date(auction.join_time).toLocaleTimeString()}`
                    );
                    if (auctions.length > 0) {
                        this.currentPrice = auctions[0].price || this.startPrice; // 기본값 설정
                        this.highestBidder = auctions[0].userEmail;
                    }
                })
                .catch(error => {
                    console.error("경매 데이터를 가져오는 중 오류가 발생했습니다:", error);
                });
        },

        joinAuction() { 
            const currentPrice = Number(this.currentPrice);
            const bidunit = Number(this.bidunit);
            const newPrice = currentPrice + bidunit;
            const csrfTokenElement = document.querySelector('meta[name="csrf-token"]');
            const csrfToken = csrfTokenElement ? csrfTokenElement.getAttribute('content') : '';
            const currentUser = this.$store.getters.getEmail;

            if (isNaN(currentPrice) || isNaN(bidunit)) {
                console.error("가격 계산에 오류가 발생했습니다: currentPrice 또는 bidunit이 숫자가 아닙니다.");
                return;
            }
            if(this.currentBidder == null || this.currentBidder == ''){
                alert('로그인한 회원만 경매에 참여가 가능합니다. 로그인을 먼저 진행해 주세요.');
            } else {
                const now = new Date(); // 현재 시간을 가져옵니다.
                if(now >= this.closeDateTime){
                    alert('마감된 경매 상품입니다.');
                } else {
                    if (this.currentBidder == this.highestBidder) {
                        alert('이미 당신이 최고가 낙찰 예정자 입니다. 다른 유저가 입찰하면 다시 시도해주세요.');
                    } else {
                        const auctionDTO = {
                            userEmail: this.currentBidder,
                            price: newPrice,
                        };

                        axios.post(`/api/${this.productId}`, auctionDTO, {
                            headers: {
                                'X-CSRF-TOKEN': csrfToken,
                                Authorization: `Bearer ${localStorage.getItem("token")}` // OAuth 토큰
                            },
                            withCredentials: true
                        })
                        .then(response => {
                            const auction = response.data;
                            this.currentPrice = auction.price;
                            this.highestBidder = auction.userEmail;
                        })
                        .catch(error => {
                            console.error("입찰 데이터를 서버에 저장하는 중 오류가 발생했습니다:", error);
                        });
                    }
                }
            }
        }
    },

    mounted() {
        // 컴포넌트가 마운트되었을 때 경매 데이터를 가져옴
        this.fetchAuctionData();

        // 웹소켓 연결 설정
        this.websocketClient = new WebSocket(`ws://localhost:8083/ws/auction/${this.productId}`);

        // 웹소켓 메시지 수신 시 호출되는 핸들러
        this.websocketClient.onmessage = (event) => {
            const auction = JSON.parse(event.data);

            // 서버에서 받은 데이터를 사용해 경매 현황을 업데이트
            this.messages.unshift(`${auction.price}원을 ${auction.userEmail}님이 제시하셨습니다. ${new Date(auction.join_time).toLocaleTimeString()}`);
            this.currentPrice = auction.price;  
            this.highestBidder = auction.userEmail;
        };

        // 웹소켓 연결 종료 시 호출되는 핸들러
        this.websocketClient.onclose = () => {
        };

        // 웹소켓 오류 발생 시 호출되는 핸들러
        this.websocketClient.onerror = (error) => {
            console.error('WebSocket error occurred:', error);
        };
    }
}
</script>





<style scoped>
@font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf');
}
.detail-description {
  height: 200px; /* 원하는 최대 높이 설정 */
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
.current-price-info{
    font-weight: bold;
    font-size: 15px;
    position: center;
    font-family: 'hanna';
}
.bidunit{
    font-size: 15px;
    font-family: 'hanna';
}
.join-btn{
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    position: unset;
    min-inline-size: -webkit-fill-available;
    width: 105px;
    font-size: 15px;
    color: white;
    background-color: darksalmon;
    font-family: 'hanna';
}
</style>