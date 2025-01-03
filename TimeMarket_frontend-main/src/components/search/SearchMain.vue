<template>
  <section id="search-body">
    <ul class="nav nav-tabs">
      <li class="nav-item">
        <router-link :class="{'active': searchType=='close'}" class="nav-link" :to="{params:{keyword: keyword, searchType:'close', page:1}}">마감임박</router-link>
      </li>
      <li class="nav-item">
        <router-link :class="{'active': searchType=='open'}" class="nav-link" :to="{params:{keyword: keyword, searchType:'open', page:1}}">오픈예정</router-link>
      </li>
    </ul>
    <goods-list ref="list"></goods-list>
    <div id="search-pagination">
      <ul class="pagination">
        <li class="page-item" :class="{'disabled':page==1}">
          <router-link :to="{params:{keyword: keyword, searchType:searchType, page:Number(page)-1}}" class="page-link" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </router-link>
        </li>
        <li class="page-item" v-for="(item, idx) in pageRange" :key="idx">
          <router-link :class="{'active': item==page}" class="page-link" :to="{params:{keyword: keyword, searchType:searchType, page:item}}">{{ item }}</router-link>
        </li>
        <li class="page-item" :class="{'disabled':page==lastPage}">
          <router-link class="page-link" :to="{params:{keyword: keyword, searchType:searchType, page:Number(page)+1}}" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </router-link>
        </li>
      </ul>
    </div>
  </section>
</template>

<script>
import GoodsList from '../layout/GoodsList.vue'
import axios from 'axios';
export default {
    props:{
        keyword:String,
        searchType:String,
        page: Number
    },
    components:{
        GoodsList
    },
    data(){
      return {
        lastPage:30,
        items:[]
      }
    },
    computed:{
      pageRange:function(){
        if(this.lastPage <= 5){
          return [...Array(this.lastPage).keys()].map(page=>page+1);
        } else if(this.page <= 2) {
          return [...Array(5).keys()].map(page=>page+1);
        } else if(this.lastPage-this.page < 2) {
          return [...Array(5).keys()].map(page=>page+this.lastPage-4);
        } else {
          return [...Array(5).keys()].map(page=>page+this.page-2);
        }
      }
    },
    created(){
      this.fetchSearchItems(this.page, this.searchType, this.keyword);
    },
    beforeRouteUpdate(to, from, next){
      this.fetchSearchItems(to.params['page'], to.params['searchType'], to.params['keyword']);
      next();
    },
    methods: {
      async fetchSearchItems(page, searchType, keyword) {
        try{
          const response = await axios.get('/api/search',{
            params:{
              page: page,
              searchType: searchType,
              keyword: keyword
            }
          });
          this.lastPage = response.data.totalPages;
          this.$refs.list.setItems(response.data.content);
        }catch(error){
          console.error("Error fetching items:", error); 
        }
      }
    }
}
</script>

<style scoped>

#search-body {
    padding: 1% 20% 0% 20%;
}

#search-pagination {
  display: flex;
  justify-content: center;
}

</style>
<!--
author : 홍제기
date : 2024-08-22
description : 검색 결과 내용
요약 : 검색 결과의 내용을 보여준다. 마감임박, 오픈예정에 따른 결과 출력
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-21         홍제기          최초 생성
-->