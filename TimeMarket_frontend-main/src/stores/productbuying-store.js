const store ={
    state(){
        return {
            productBuying: {},
        }
    },
    mutations:{
        setProductBuying(state, payload){
            state.productBuying = payload;
        }
    },
    actions:{
        updateProductBuying({commit}, productBuying){
            commit('setProductBuying', productBuying);
        }
    },
    getters:{
        getProductBuying: state => state.productBuying
    }
}

export default store;