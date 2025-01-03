<template>
    <div>
    <header id="topnav">
        <router-link to="/" id="logo-box">
            <img id="logo" src="@/assets/images/logo-transparent-png.png">
        </router-link>
        <nav>
            <div class="nav-item" id="search-block">
                <input class="form-control" type="search" placeholder="Search" aria-label="Search" v-model="keyword"
                    @focus="searchFocus = true" @keypress="search">
                <button class="btn btn-light" type="submit" @click="search"><img id="search-icon"
                        src="@/assets/images/search.png"></button>
                <search-result v-show="keyword.length > 0 && searchFocus" :keyword="keyword"></search-result>
            </div>

            <!-- 로그인 상태에 따른 표시 -->
            <div v-if="!isAuthenticated" class="nav-item">

                    <button class="btn btn-dark" @click="showLoginModal = true">
                        로그인/회원가입
                    </button>
                </div>
                <router-link v-if="isAuthenticated" class="vue-link" to="/time-recharge">
                    <div class="nav-item">
                        <img class="icon" src="@/assets/images/time.png">
                        <div class="points" v-if="userInfo.wallet !== undefined">
                        <span id="time-amount"  class="wallet-text">{{ userInfo.wallet.toLocaleString()}}</span>
                        </div>
                    </div>
                </router-link>
                <div class="nav-item" id="bucket-btn" v-if="isAuthenticated">
                    <router-link to="/bucket">
                        <img class="icon" src="@/assets/images/shoopingcart.png"/>
                    </router-link>
                </div>
                <div class="nav-item" id="alarm-block" v-if="isAuthenticated">
                    
                    <img class="icon" :class="{ 'shake': newAlarm }" src="@/assets/images/bell.png" @click="toggleAlarm">
                    <alarm-comp v-if="isAlarmOn"></alarm-comp>
                </div>

                <!-- 사이드바 아이콘을 오른쪽 끝으로 이동 -->
                <div v-if="isAuthenticated" class="nav-item sidebar-icon" @click.stop="toggleSidebar">
                    <img class="icon" src="@/assets/images/sidebar-icon.png" alt="User Menu" />
                </div>

            <!-- 로그인 모달 컴포넌트 -->
            <login-modal :isVisible="showLoginModal" @close="showLoginModal = false" @mypage="handleLoginSuccess"></login-modal>

        </nav>

        <ProductDetail :isVisible="isModalVisible" :canBuy="true" :selectedItem="selectedItem" @close="closeModal" />
    </header>
    <sidebar-menu v-if="isSidebarVisible" @close="toggleSidebar" @logout="logout"></sidebar-menu>
</div>
</template>

<script>
// Your existing imports

import LoginModal from './LoginModal.vue';
import AlarmComp from '../main/AlarmComp.vue';
import SearchResult from '../main/SearchResult.vue';
import ProductDetail from '../productDetail/ProductDetail.vue';
import SidebarMenu from './SidebarMenu.vue';
import axios from 'axios';
import { mapGetters } from 'vuex';
import EventBus from '@/util/eventBus'; // EventBus의 실제 경로로 변경

import BucketPage from '../Bucket/BucketPage.vue';

export default {
    components: {
        SearchResult,
        AlarmComp,
        LoginModal,
        ProductDetail,
        SidebarMenu,
    },
    data() {
        return {
            userInfo: {},
            keyword: '',
            isAlarmOn: false,
            searchFocus: false,
            newAlarm: false,
            evtSource: {},
            isModalVisible: false,
            selectedItem: {},
            showLoginModal: false,
            isSidebarVisible: false,
            isFirstLogin: true
        };
    },
    mounted() {
        this.fetchUserInfo(); // 유저 정보를 가져오는 메소드 호출
        this.csrfToken = this.getCsrfTokenFromCookie(); // CSRF 토큰을 쿠키에서 가져옴
        document.addEventListener('click', this.handleClickOutside); // 클릭 이벤트 리스너 추가
        EventBus.$on('update-wallet', this.fetchUserInfo);  // 'update-wallet' 이벤트를 감지하고 fetchUserInfo 메서드 호출
        // this.$store.dispatch('fetchUserInfo'); // Vuex의 액션을 호출하여 유저 정보를 가져옴
    },
    methods: {
        fetchUserInfo() {
            axios.get('/api/user-info', {
                withCredentials: true  // 쿠키를 포함해서 요청하도록 설정
            })
            .then(response => {
                this.userInfo = response.data; // 서버로부터 받은 유저 정보를 저장
            })
            .catch(error => {
                console.error('Error fetching user info:', error);
            });
            // this.$stroe.dispatch('fetchUserInfo');
        },
        toggleAlarm() {
            this.isAlarmOn = !this.isAlarmOn;
            if (this.isAlarmOn) {
                this.newAlarm = false;
            }
        },
        search(key) {
            if (this.keyword.length == 0) {
                return;
            }

            if (key instanceof KeyboardEvent && key.key != 'Enter') {
                return;
            }
            this.searchFocus = false;
            this.$router.push({ name: 'search', params: { keyword: this.keyword, searchType: 'close', page: 1 } }).catch(() => { });
        },
        handleLoginSuccess() {
            this.$store.dispatch('login', {
                token: this.token,  // 실제로 토큰을 어떻게 받아오는지 확인
                email: this.email,  // 실제 이메일
                nickname: this.nickname // 닉네임
            });
            this.isAuthenticated = true;    // 사용자가 로그인됨을 표시 
            this.showLoginModal = false;
            this.$router.push('/mypage');
        },
        logout() {
            // 로그아웃 로직
            document.cookie = 'jwt_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
            document.cookie = 'XSRF-TOKEN=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
            this.$store.commit('logout');
            this.toggleSidebar(); // 사이드바 닫기
            if (this.$route.path !== '/') {
                this.$router.replace('/');
            } else {
                this.$router.go(0); // 현재 페이지를 새로고침 (강제로 상태 초기화)
            }
        },
    getCsrfTokenFromCookie() {
      const name = 'XSRF-TOKEN=';
      const decodedCookie = decodeURIComponent(document.cookie);
      const ca = decodedCookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return '';
    },
        toggleSidebar() {
            this.isSidebarVisible = !this.isSidebarVisible;
        },
        // handleClickOutside(event) {
        //     // 사이드바가 열려 있고, 사이드바와 사이드바 아이콘 외부를 클릭한 경우에만 사이드바를 닫습니다.
        //     const sidebar = this.$refs.sidebar;
        //     const sidebarIcon = this.$el.querySelector('.sidebar-icon');
        //     if (this.isSidebarVisible && !sidebar.contains(event.target) && !sidebarIcon.contains(event.target)) {
        //         this.toggleSidebar();
        //     }
        // },
        async initAlarmEvent() {
            try{
                this.evtSource = new EventSource(`/api/register-alarm?user=${this.getEmail}`, { withCredentials: true });
            } catch(error){
                console.error('Error create SSE', error);
                return;
            }
            
            const vm = this;

            this.evtSource.onopen = (event)=>{
                if(vm.isFirstLogin){
                    
                    vm.isFirstLogin = false;
                    
                    axios('/api/check-alarm', {params:{
                        user: vm.getEmail
                    }}).then((response)=>{
                        if(response.data > 0){
                            vm.newAlarm = true
                        }
                    }
                    ).catch();

                }
            }

            this.evtSource.addEventListener('connect', function (evt) {
                // 데이터 안정용
            });

            this.evtSource.addEventListener('alarm', function (evt) {
                vm.newAlarm = true;
            });
        },
        openModal(item) {
            this.selectedItem = item;
            this.isModalVisible = true;
            this.searchFocus = false;
        },
        closeModal() {
            this.isModalVisible = false;
        },
    },
    computed: {
        ...mapGetters(['isAuthenticated', 'getEmail', 'getWallet']),
    },
    watch: {
        isAuthenticated(newValue, oldValue) {
            if (newValue == true) {
                this.initAlarmEvent();
            } else {
                this.isFirstLogin=true;
                this.evtSource.close();
            }
        }
    },
    created(){
        if(this.isAuthenticated){
            this.initAlarmEvent();
        }
    },
    beforeDestroy() {
        if (this.evtSource && typeof this.evtSource.close === 'function') {
            this.evtSource.close();
        }
        document.removeEventListener('click', this.handleClickOutside); // 클릭 이벤트 리스너 제거
        EventBus.$off('update-wallet', this.fetchUserInfo);

    }
}
</script>

<style scoped>
@font-face {
    font-family: 'hanna';
    src: url('../../assets/fonts/GodoM.ttf');
}

#topnav {
    background-color: wheat;
    position: fixed;
    display: flex;
    align-items: center;
    width: 100%;
    height: 100px;
    padding-left: 10px;
    padding-right: 10px;
    z-index: 100;
    font-family: 'hanna';
}

#topnav #logo-box {
    height: 100%;
    font-family: 'hanna';
}

#topnav #logo {
    height: 100%;
    font-family: 'hanna';
}

#topnav>nav {
    width: 80%;
    height: 50%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-right: 40px;
    font-family: 'hanna';
}

#time-amount {
    font-size: 1.5em;
    font-family: 'hanna';
}

#search-block {
    position: relative;
    margin-left: 10%;
    width: 50%;
    display: flex;
    font-family: 'hanna';
}

.nav-item {
    display: flex;
    align-items: center;
    font-family: 'hanna';
}

.nav-item:hover {
    background-color: #e9e9e9;
    font-family: 'hanna';
}

#search-icon {
    height: 25px;
    font-family: 'hanna';
}

#topnav .icon {
    height: 35px;
    margin-right: 5%;
    font-family: 'hanna';
}

#alarm-block {
    position: relative;
    right: -30px;
    font-family: 'hanna';
}

#bucket-btn{
    position: relative;
    right: -50px;
}

.shake {
    animation: shake 0.82s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
    animation-iteration-count: infinite;
    transform: translate3d(0, 0, 0);
    font-family: 'hanna';
}

.wallet-text {
  margin-left: 10px; /* 왼쪽 여백을 추가하여 간격 조정 */
}

@keyframes shake {

    10%,
    90% {
        transform: translate3d(-1px, 0, 0);
    }

    20%,
    80% {
        transform: translate3d(2px, 0, 0);
    }

    30%,
    50%,
    70% {
        transform: translate3d(-4px, 0, 0);
    }

    40%,
    60% {
        transform: translate3d(4px, 0, 0);
    }
}
</style>