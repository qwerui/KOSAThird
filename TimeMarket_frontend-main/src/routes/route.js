import BucketPage from "@/components/Bucket/BucketPage.vue";
import LoginSuccess from "@/components/layout/login-success.vue";
import MainView from "@/components/main/MainView.vue";
import ProductBuyPage from "@/components/Bucket/ProductBuyPage.vue";
import SearchMain from "@/components/search/SearchMain.vue";
import AccountEdit from "@/components/UserMypage/AccountEdit.vue";
import BussinessMember from "@/components/UserMypage/BussinessMember.vue";
import BuyHistory from "@/components/UserMypage/BuyHistory.vue";
import InquireShop from "@/components/UserMypage/InquireShop.vue";
import Mypage from "@/components/UserMypage/Mypage.vue";
import SellHistory from "@/components/UserMypage/SellHistory.vue";
import TimeRecharge from "@/components/UserMypage/TimeRecharge.vue";
import { component } from "vue/types/umd";

const routes = [
    {
        path:'/',
        component: MainView 
    },
    {
        path:'/search/:keyword/:searchType/:page?',
        name:'search',
        component:SearchMain,
        props:true,
    },
    {
        path: '/login-success',
        name: 'LoginSuccess',
        component: LoginSuccess,
    },
    {
        path: '/mypage',
        name: 'Mypage',
        component: Mypage,
        
    },
    {
        path: "/bucket",
        name: "Bucket",
        component: BucketPage,
        
      },
      {
        path: "/bucket/checkout",
        name: "ProductBuy",
        component: ProductBuyPage,
        props: true,
      },
      {
        path:'/account-edit',
        component: AccountEdit
      },
      {
        path: '/sell-history',
        component: SellHistory
     },
     {
        path:'/buy-history',
        component: BuyHistory
     },
      {
        path:'/time-recharge',
        component: TimeRecharge
    },
    {
        path:'/inquire',
        component: InquireShop
    },
    {
        path:'/bussiness-member',
        component: BussinessMember
    },
    
]

export default routes;