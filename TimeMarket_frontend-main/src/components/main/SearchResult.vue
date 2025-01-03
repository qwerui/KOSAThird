<template>
  <section id="search-body">
    <h5>검색 결과</h5>
    <hr>
    <div @click="openModal(item)" v-for="(item, idx) in items" :key="idx">
        <div>
            <h5 class="search-title">{{item.title}}</h5>
            <div class="price-time">
                <p class="search-price">{{item.price}}원</p>
                <p class="search-time">마감시간: {{ formatDate(item.closeTime) }}</p>
            </div>
        </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios';
import format from '@/util/format'

export default {
    data(){
        return {
            searchItems: [],
        }
    },
    props:{
        keyword: String,
    },
    computed: {
        items: function(){
            return this.searchItems.filter((item)=>item.title.includes(this.keyword)).sort().slice(0, 6);
        }
    },
    methods: {
        async fetchSearchItems() {
            try {
                const response = await axios.get('/api/ongoing-all');  // 백엔드 API 호출
                this.searchItems = response.data;
            } catch (error) {
                console.error('Error fetching items:', error);  // 오류 처리
            }
        },
        openModal(item){
            this.$parent.openModal(item);
        },
        formatDate: function(str)
        {
            return format.formatDate(str);
        },
        
    },
    created(){
        this.fetchSearchItems();
    }
}
</script>

<style scoped>
#search-body {
    position:absolute;
    background-color:white;
    top:100%;
    padding:10px;
    border: solid, black, 1px;
    width:100%;
    font-family: 'hanna';
}

.price-time {
    display: flex;
    justify-content: space-between;
}

.search-title {
    overflow:hidden;
    text-overflow:ellipsis;
    white-space:nowrap;
    font-weight: bolder;
}

.search-price{
    font-weight: bold;
}

.search-time {
    font-size: 0.75em;
}
</style>
<!--
author : 홍제기
date : 2024-08-22
description : 검색창 결과
요약 : 검색창의 결과를 보여준다. 해당 키워드의 마감임박 제품을 보여준다.
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-21         홍제기          최초 생성
-->