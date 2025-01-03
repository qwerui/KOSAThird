<template>
    <div>
      <!-- Main image display -->
      <swiper-container
        :style="{
          '--swiper-navigation-color': '#fff',
          '--swiper-pagination-color': '#fff',
        }"
        :spaceBetween="10"
        :navigation="true"
        :modules="modules"
        class="mySwiper2"
        ref="mainSwiper"
      >
        <swiper-slide
          v-for="(image, index) in images"
          :key="index"
          @swiperslideChange="handleSlideChange"
        >
          <img :src="image" style="height:400px"/>
        </swiper-slide>
      </swiper-container>
  
      <!-- Thumbnails -->
      <div class="thumbnails">
        <img
          v-for="(image, index) in images"
          :key="index"
          :src="image"
          @click="updateActiveIndex(index)"
          :class="{ active: index === activeIndex }"
          class="thumbnail"
        />
      </div>
  
      <!-- Modal
      <div v-if="showModal" class="modal" @click="closeModal">
        <div class="modal-content" @click.stop>
          <img :src="selectedImage" class="modal-image" />
        </div>
      </div> -->
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue';
  import { Navigation, Thumbs } from 'swiper';
  import 'swiper/swiper-bundle.css';
  import axios from 'axios';
  
  export default {
    name: 'ImageView',
    props: {
      productId: {
        type: Number,
        required: true,
      },
    },

    setup(props) {
      const activeIndex = ref(0);
      const showModal = ref(false);
      const selectedImage = ref('');
      const mainSwiper = ref(null);
      const images = ref([]);
  
      const fetchImages = async () => {
        try {
          const response = await axios.get(`/api/images-all/${props.productId}`);
          images.value = response.data.map(item => `https://storage.googleapis.com/ellie_bucket98/${item.imageUrl}`);
        } catch (error) {
          console.error('Error fetching images:', error);
        }
      };
  
      const updateActiveIndex = (index) => {
        activeIndex.value = index;
        if (mainSwiper.value) {
          mainSwiper.value.swiper.slideTo(index);
        }
      };
  
      const handleSlideChange = (e) => {
        if (mainSwiper.value) {
          activeIndex.value = mainSwiper.value.swiper.activeIndex.value;
        }
      };
  
      const openModal = (image) => {
        selectedImage.value = image;
        showModal.value = true;
      };
  
      const closeModal = () => {
        showModal.value = false;
      };
  
      onMounted(() => {
        fetchImages();
      });
  
      return {
        activeIndex,
        images,
        updateActiveIndex,
        modules: [Navigation, Thumbs],
        mainSwiper,
        showModal,
        handleSlideChange,
        selectedImage,
        openModal,
        closeModal,
      };
    },
  };
  </script>
  
<style>  
  #app {
    height: 100%;
  }
  html,
  body {
    position: relative;
    height: 100%;
  }
  
  body {
    background: #eee;
    font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
    font-size: 14px;
    color: #000;
    margin: 0;
    padding: 0;
  }
  
  .swiper {
    width: 100%;
    height: 100%;
  }
  
  .swiper-slide {
    text-align: center;
    font-size: 18px;
    background: #fff;
  
    /* Center slide text vertically */
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .swiper-slide img {
    display: block;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  body {
    background: #000;
    color: #000;
  }
  
  .swiper {
    width: 100%;
    height: 300px;
    margin-left: auto;
    margin-right: auto;
  }
  
  .swiper-slide {
    background-size: cover;
    background-position: center;
  }
  
  .mySwiper2 {
    height: 80%;
    width: 100%;
  }
  
  .mySwiper {
    height: 20%;
    box-sizing: border-box;
    padding: 10px 0;
  }
  
  .mySwiper .swiper-slide {
    width: 25%;
    height: 100%;
    opacity: 0.4;
  }
  
  .mySwiper .swiper-slide-thumb-active {
    opacity: 1;
  }
  
  .swiper-slide img {
    display: block;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  img {
    vertical-align: middle;
    width: -webkit-fill-available;
    position: center;
  }

  .thumbnails {
  display: flex;
  overflow-x: scroll;
  margin-top: 10px;
}

.thumbnail {
  width: 100px; /* Set a fixed width for thumbnails */
  margin-right: 10px;
  cursor: pointer;
}

.thumbnail.active {
  border: 2px solid #007bff; /* Highlight active thumbnail */
}



/* Modal styles
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  position: relative;
  background: #fff;
  padding: 20px;
}

.modal-image {
  max-width: 100%;
  max-height: 80vh;
}

.modal-content::after {
  content: '';
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
} */
</style>

