<template>
    <div>
        <p class="delivery-input">배송지 입력</p>
        <p class="address">주소 : </p> 
        <!-- 유저 주소 입력란 받아서 스크립트에 임시저장 -->
         <p class="memo">메모 사항</p>
         <!-- 유저의 기타 메모사항을 받는 입력란받아 스크립트에 임시저장 -->
        <p class="purchase-method">결제 수단 선택</p>
        <!-- "카드결제" or "포인트결제" 선택하는 라디오 버튼 생성  -->
         <button v-if="isCardPurchase" class="card-purchase" href="https://naver.com">카드 결제하기</button>
         <button v-if="!isCardPurchase" class="card-purchase" href="https://google.com">포인트 결제하기</button>
    </div>
</template>

<script>
export default {
    name: "OrderDetail",
    props: {
        productId: {
            type: Number,
            required: true,
        },
    },
    data() {
    return {
        isCardPurchase: true,
        };
    },   

}

</script>