<template>
    <article>
      <h3>{{ title }}</h3>
      <h5>{{ subtitle }}</h5>
      <div class="no-items" v-if="items.length == 0">
        <h1>{{ emptyMessage }}</h1>
      </div>
      <swiper-container slides-per-view="4" navigation="true" space-between="15">
            <swiper-slide class="item border border-1 border-dark" v-for="(item, idx) in items" :key="idx">
                <router-link class="vue-link" to="/">
                    <div class="item-wrapper">
                        <div class="item-image-wrapper" @click="openModal(item)">
                            <img :src="'https://storage.googleapis.com/ellie_bucket98/'+item.uniqueImg">
                        </div>
                        
                        <h5>{{item.title}}</h5>
                        <p>{{ item.sellingType }}</p>
                        <p><span class="item-price">{{ item.price }}원</span><span class="item-time">{{ formatDate(item.closeTime) }}</span></p>
                    </div>
                </router-link>
            </swiper-slide>
        </swiper-container>
  
      <!-- 모달 컴포넌트 -->
      <ProductDetail 
        :isVisible="isModalVisible" 
        :canBuy="canBuy" 
        :selectedItem="selectedItem"
        @close="closeModal"
      />
    </article>
  </template>
  
  <script>
  import axios from "axios";
  import format from "@/util/format";
  import ProductDetail from "@/components/productDetail/ProductDetail"; // 모달 컴포넌트 임포트
  
  export default {
    props: {
      title: String,
      subtitle: String,
      emptyMessage: String,
      api: String,
      canBuy: Boolean,
    },
    data() {
      return {
        items: [],
        isModalVisible: false, // 모달 가시성 제어
        selectedItem: {}, // 선택된 아이템 데이터
      };
    },
    components: {
      ProductDetail,
    },
    methods: {
      async fetchItems() {
        try {
          const response = await axios.get(this.api); // 백엔드 API 호출
          this.items = response.data; // API 응답을 items 배열에 저장
        } catch (error) {
          console.error("Error fetching items:", error); // 오류 처리
        }
      },
      formatDate(str) {
        return format.formatDate(str);
      },
      openModal(item) {
        this.selectedItem = item;
        this.isModalVisible = true;
        document.querySelector('article').inert = true; // 모달 이외의 요소 비활성화
      },
      closeModal() {
        this.isModalVisible = false;
        document.querySelector('article').inert = false; // 모달 이외의 요소 활성화
      },
    },
    created() {
      this.fetchItems();
    },
  };
  </script>

<style scoped>
@font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf');
}

article {
    position: relative;
    font-family: 'hanna';
}

.item {
    background-color: white;
    display: inline-block;
    padding: 10px;
    font-family: 'hanna';
    border-radius: 10px;
}

.item-image-wrapper{
    width:100%;
    aspect-ratio: 1/1;
    position: relative;
    font-family: 'hanna';
}

.item:hover{
    background-color:#e9e9e9;
    font-family: 'hanna';
}
.item img {
    width:100%;
    height:100%;
    top:0;
    left:0;
    font-family: 'hanna';
}

.item h5 {
    overflow:hidden;
    text-overflow:ellipsis;
    white-space:nowrap;
    font-family: 'hanna';
}

.item p {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-family: 'hanna';
}

.item-price {
    font-weight: bold;
    font-size: 1em;
    font-family: 'hanna';
}

.item-time {
    font-size: 0.75em;
    font-family: 'hanna';
}

.deadline-list {
    overflow-x: hidden;
    display: flex;
    font-family: 'hanna';
}

#deadline-left {
    z-index: 1;
    position:absolute;
    display: inline;
    top:50%;
    align-items: center;
    font-family: 'hanna';
}

#deadline-right{
    z-index: 1;
    position:absolute;
    top:50%;
    display: inline;
    right: 0%;
    font-family: 'hanna';
}

#deadline-left > img:hover{
    cursor:pointer;
}

#deadline-right > img:hover{
    cursor: pointer;
}


</style>
<!--
author : 홍제기
date : 2024-08-20
description : 스크롤 제품 목록
요약 : 스크롤이 가능한 제품 목록을 보여준다.
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-20         홍제기          최초 생성
2024-08-22         홍제기          스크롤 모듈 변경
-->