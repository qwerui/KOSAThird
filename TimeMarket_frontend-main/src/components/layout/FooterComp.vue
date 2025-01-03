<template>
  <footer>
    <hr>
    <div class="footer-padding">
      <div>
        <img src="@/assets/images/logo-transparent-png.png">
      </div>
      <p id="company-description">
        (77777) 서울특별시 종로구 창경궁로 254 <br>
        고객센터 : 02-0000-0000 (평일 9시 ~ 18시) <br>
        FAX : 02-000-0000
      </p>
    </div>
    <hr>
    <div class="footer-padding">
      <ul id="footer-policy">
      <li>이용안내</li>
      <li>개인정보처리방침</li>
      <li>저작권정책</li>
    </ul>
    </div>
  </footer>
</template>

<script>
export default {

}
</script>

<style>
footer {
    background-color: #f0f0f0;
    width: 100%;
    height:300px;
    position: relative;
    transform: translateY(-100%);
    font-family: 'hanna';
}

footer img {
  width:auto;
  height:100px;
}

.footer-padding {
  padding-left:5%;
  padding-right:5%;
}

#footer-policy {
  padding-bottom:15px;
}

#footer-policy > li{
  display: inline;
  list-style: none;
  margin-right: 5%;
}

#company-description {
  margin-top: 30px;
  margin-bottom: 30px;
}
</style>

<!--
author : 홍제기
date : 2024-08-21
description : footer
요약 : footer
===========================================================
DATE            AUTHOR             NOTE
—————————————————————————————
2024-08-21         홍제기          최초 생성
-->