<template>
  <article>
    <h3>{{ title }}</h3>
    <h5>{{ subtitle }}</h5>

    <!-- 일반 리스트 -->
    <div class="no-items" v-if="items.length == 0">
        <h1>{{emptyMessage}}</h1>
    </div>
    <div v-if="items.length > 0">
        <div class="item border border-3 border-dark rounded-3" v-for="(item, idx) in items" :key="idx" @click="openModal(item)">
            <router-link class="vue-link" to="/">
                <div class="item-wrapper">
                    <img :src="'https://storage.googleapis.com/ellie_bucket98/'+item.uniqueImg">
                    <h5>{{item.title}}</h5>
                    <p>{{ item.sellingType }}</p>
                    <p><span class="item-price">{{ item.price }}원</span><span class="item-time">{{ formatDate(item.closeTime) }}</span></p>
                </div>
            </router-link>
        </div>
    </div>
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
import axios from 'axios';
import format from '@/util/format';
import ProductDetail from "@/components/productDetail/ProductDetail"; // 모달 컴포넌트 임포트

export default {
    props:{
        title: String,
        subtitle: String,
        emptyMessage: String,
        api: String,
        canBuy: Boolean
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
    methods:{
        async fetchItems() {
            try {
                const response = await axios.get(this.api);  // 백엔드 API 호출
                this.items = response.data; // API 응답을 items 배열에 저장
            } catch (error) {
                console.error('Error fetching items:', error);  // 오류 처리
            }
        },
        formatDate(str) {
        return format.formatDate(str);
      },
      setItems(items){
        if(items instanceof Array){
            this.items = items;
        }
      },
      openModal(item) {
        this.selectedItem = item;
        this.isModalVisible = true;
        document.querySelector('article').insert = true; // 모달 이외의 요소 비활성화
      },
      closeModal() {
        this.isModalVisible = false;
        document.querySelector('article').insert = false; // 모달 이외의 요소 활성화
      },
    },
    created(){
        if(this.api != undefined && this.api.length > 0){
            this.fetchItems();
        }
    }
}
</script>

<style scoped>

article {
    position: relative;
}

.item {
    display: inline-block;
    width:220px;
    height:300px;
    margin: 15px;
    padding: 10px;
}

.item img {
    width:100%;
    height:180px;
}

.item h5 {
    overflow:hidden;
    text-overflow:ellipsis;
    white-space:nowrap;
}

.item p {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.item-price {
    font-weight: bold;
    font-size: 1em;
}

.item-time {
    font-size: 0.75em;
}

.deadline-list {
    overflow-x: hidden;
    display: flex;
}

#deadline-left {
    z-index: 1;
    position:absolute;
    display: inline;
    top:50%;
    align-items: center;
}

#deadline-right{
    z-index: 1;
    position:absolute;
    top:50%;
    display: inline;
    right: 0%;
}

#deadline-left > img:hover{
    cursor:pointer;
}

#deadline-right > img:hover{
    cursor: pointer;
}

.no-items {
    height: 300px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-overlay {
    z-index: 1000; /* Ensure this is sufficiently high */
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}


</style>

<!--
author : 홍제기
date : 2024-08-20
description : 제품 목록 레이아웃
요약 : api를 prop로 받아 axios로 얻은 제품 목록을 표시
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-20         홍제기          최초 생성
-->