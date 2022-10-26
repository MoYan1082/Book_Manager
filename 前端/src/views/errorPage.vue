<template>
  <div>
    <Header></Header>
    <div>
      <el-image :src="src" class="a"></el-image>
      <h1 class="b">页面出错了哦(っ °Д °;)っ</h1>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
export default {
  components: { Header },
  data() {
    return {
      src: "2.jpg"
    }
  },
  methods: {},
};
</script>

<style>
.a{
  margin-left: 360px;
}
.b{
  margin-top: -150px;
}
</style>