<template>
  <div id="sidebar" class="sidebar">
    <!-- <div class="sidebar"> -->
      <ul class="menu">
        <button class="close-btn" @click="$emit('close')">X</button>
        <!-- <div class="sidebar-btn"> <router-link to="/productForm" @click="$emit('close')">제품 판매하기</router-link> </div> -->
        <li><router-link to="/productForm"  @click.native="$emit('close')">제품 판매하기</router-link></li>
        <li><router-link to="/sell-history"  @click.native="$emit('close')">판매내역</router-link></li>
        <li><router-link to="/buy-history"  @click.native="$emit('close')">구매내역</router-link></li>
        <li><router-link to="/time-recharge"  @click.native="$emit('close')">타임충전</router-link></li>
        <li><router-link to="/account-edit"  @click.native="$emit('close')">회원정보수정</router-link></li>
        <li><router-link to="/inquire"  @click.native="$emit('close')">문의하기</router-link></li>
        <li><router-link to="/inquire"  @click.native="$emit('logout')">로그아웃</router-link></li>

      </ul>
    <!-- </div> -->

  </div>
</template>

<script>
export default {
  name: 'SidebarMenu',
};
</script>

<style scoped>

@font-face {
  font-family: 'hanna';
  src: url('@/assets/fonts/GodoM.ttf');
}


.sidebar {
  font-family: 'hanna';
    position: fixed;
    top: 100px;
    right: 0;
    width: 250px;
    /* height: 90%; */
    background-color: wheat;
    border-style: solid;
    /* border-radius: 20px; */
    border-color: rgb(249, 203, 146);
    padding: 20px;
    z-index: 200;
    color: rgb(255, 255, 255);
    display: flex;
    flex-direction: column;
    border-bottom-left-radius: 20px;
    border-bottom-right-radius: 29px;
}


.close-btn {
  font-family: 'hanna';
  align-self: flex-end;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: white;
}

.sidebar-btn {
  font-family: 'hanna';
  border-style: dashed;
  border-color: white;
  text-align: center;
  margin: 10px 0;
}
.sidebar a,
.sidebar button {
  font-family: 'hanna';
  color: white;
  text-align: center;
  text-decoration: none;
  
  cursor: pointer;
  background: none;
  border: none;
  font-size: 16px;
  margin-top: 10px;

}


.menu {
  font-family: 'hanna';
  list-style: none;
  padding: 0;
  width: 100%;
}

.menu li {
  font-family: 'hanna';
  margin-bottom: 10px;
  text-align: center;
}

.menu li a {
  font-family: 'hanna';
  text-decoration: none;
  color: #ffffff;
  font-size: 18px;
  display: block;
  padding: 10px;
  background-color: #495057;
  border-radius: 5px;
}

.menu li a:hover {
  font-family: 'hanna';
  background-color: #adb5bd;
  color: #343a40;
}

.content {
  font-family: 'hanna';
  flex: 1;
  padding: 20px;
  background-color: #ffffff;
  overflow-y: auto;
}
</style>
