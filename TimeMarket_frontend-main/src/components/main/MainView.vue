<template>
  <div id="content">
    <article id="main-ad">
      <swiper-container navigation="true" loop="true" autoplay-delay="5000" autoplay-disable-on-interaction="false" speed="1000">
        <swiper-slide v-for="(item, idx) in ads" :key="idx">
          <router-link to="/">
            <img class="ad-img" :src="'/images/'+item.source">
          </router-link>
        </swiper-slide>
      </swiper-container>
    </article>

    <scroll-list title="마감 임박" subtitle="다음 기회는 없다!" emptyMessage="상품이 준비중입니다." api="/api/lastdeals-all" :canBuy="true"></scroll-list>

    <hr>
    <scroll-list title="오픈 예정" subtitle="빨리 사는 사람이 가져간다!" emptyMessage="상품이 준비중입니다." api="/api/upcoming-all" :canBuy="false"></scroll-list>
    <hr>

    <goods-list title="거래 중" subtitle="지금이 기회다!" emptyMessage="구매상품이 없습니다." api="/api/ongoing-all" :canBuy="true"></goods-list>
  </div>
</template>

<script>
import GoodsList from '../layout/GoodsList.vue'
import ScrollList from '../layout/ScrollList.vue';

export default {
  data(){
    return {
      ads: [{source:'Cafe.png'},{source:'kommunity.png'},{source:'pizza.png'}],
      message: ""
    }
  },
  components: { GoodsList,
    ScrollList,
  },
}
</script>

<style scoped>
#content {
  padding: 0% 20% 0% 20%;
}

#main-ad {
  min-width: 1000px;
  max-width: 1000px;
  height: 400px;
}

.ad-img {
  width:100%;
  height: 400px;
}
</style>

<!--
author : 홍제기
date : 2024-08-20
description : 메인 페이지
요약 : 광고와 추천 제품을 보여주는 메인 페이지
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-20         홍제기          최초 생성
-->