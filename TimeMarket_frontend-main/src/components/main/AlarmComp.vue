<template>
  <section id="alarm-body">
    <div>
        <h5>받은 알림</h5>
        <hr>
        <div v-if="items.length == 0">
            <h5>받은 알림이 없습니다.</h5>
        </div>
        <div id="alarm-wrapper">
            <div class="alarm-item" v-for="(item, idx) in items" :key="idx">
                <div class="alarm-content" @click="readAlarm(idx)">
                    <h5>{{ item.title }}</h5>
                    <p class="alarm-time">{{ formatDate(item.time) }}</p>
                </div>
                <button class="btn btn-warning btn-sm" @click="deleteAlarm(idx)">삭제</button>
            </div>
        </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios';
import format from '@/util/format'
import { mapGetters } from 'vuex';

export default {
    data(){
        return {
            items:[]
        }
    },
    methods: {
        readAlarm: function(idx) {
            const type = this.items[idx].type;
            const product = this.items[idx].product;
            this.deleteAlarm(idx);

            switch(type){
                case '구매':
                    // 구매 알림
                    this.$router.push('/my-page/buy-history');
                    break;
                case '판매':
                    // 판매 알림
                    this.$router.push('/my-page/sell-history');
                    break;
                case '오픈':
                    // 오픈 알림
                    this.$parent.openModal(product);
                    break;
                case '낙찰':
                    this.$router.push('/bucket');
                    break;
            }
        },
        deleteAlarm: function(idx){
            axios('/api/alarm/read', {params:{alarm:this.items[idx].id}});
            this.items.splice(idx, 1);
        },
        async fetchAlarms(){
            try{
                const response = await axios('/api/alarm',{
                    params:{
                        user: this.getEmail
                    }
                });
                this.items = response.data;
            }catch(error){
                console.error('Error fetching Alarms : '+error);
            }
        },
        formatDate(date){
            return format.formatDate(date);
        }
    },
    computed: {
        ...mapGetters(['getEmail'])
    },
    created(){
        this.fetchAlarms();
    }
}
</script>

<style>
#alarm-body {
    position:absolute;
    top:180%;
    background-color: white;
    border: solid 2px;
    padding:10px;
    left:50%;
    transform: translateX(-50%);
    width:300px;
    border-radius: 5%;
}

.alarm-item {
    cursor: pointer;
    position: relative;
}

.alarm-item > button {
    position: absolute;
    right:5%;
    top: 0%;
}

.alarm-content {
    width:80%;
}

.alarm-time {
    font-size:0.75em;
}

#alarm-wrapper {
    overflow-y: scroll;
    max-height: 200px;
}
</style>

<!--
author : 홍제기
date : 2024-08-21
description : 알림 페이지
요약 : 알림 목록을 보여준다
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-21         홍제기          최초 생성
-->