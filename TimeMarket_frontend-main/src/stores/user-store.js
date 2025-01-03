import Cookies from 'js-cookie';

export default {
    state() {
        return {
            user_info : {}
        }
    },
    mutations: {
        login(state, payload) {
            state.user_info = payload;
            Cookies.set('jwt_token', payload.token); // JWT 토큰을 쿠키에 저장
        },
        logout(state, payload) {
            state.user_info = null;
            Cookies.remove('jwt_token'); // 로그아웃 시 JWT 토큰을 쿠키에서 제거

        },
        logfail(state, payload) {
            state.user_info = null;
            Cookies.remove('jwt_token'); // 로그인 실패 시 JWT 토큰 제거

        },
    },
    actions: {
        login(context, payload) {
            context.commit('login', payload);
        },
        logout(context, payload) {
            context.commit('logout', payload);
        },
        loginfail(context, payload) {
            context.commit('logfail', payload);
    },
    },
    getters: {
        getEmail(state) {
            return state.user_info?.email || ''; // user_info가 null일 경우 빈 문자열 반환
        },
        getNickname(state){
            return state.user_info?.nickname || ''; // user_info가 null일 경우 빈 문자열 반환
        },
        getWallet(state){
            if(!state.user_info){
                return '';
            }
            return state.user_info.wallet;
        },
        getIntroduce(state){
            return state.user_info?.introduce || ''; // user_info가 null일 경우 빈 문자열 반환
        },
        getTemp(state){
            return state.user_info?.temp || ''; // user_info가 null일 경우 빈 문자열 반환
        },
        getRole(state){
            return state.user_info?.role || ''; // user_info가 null일 경우 빈 문자열 반환
        },
       
        getToken(state){
            return state.user_info?.token || Cookies.get('jwt_token');
        },
        isAuthenticated(state){
            return Boolean(state.user_info?state.user_info.token:false );
        }
    },
}