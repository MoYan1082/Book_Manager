<template>
  <div>
    <el-menu :default-active="this.$route.path" router mode="horizontal" class="index-header">
      <el-menu-item v-for="(item, i) in navList" :key="i" :index="item.name">
        {{ item.navItem }}
      </el-menu-item>
      <el-menu-item
        ><span v-show="this.hasLogin">{{ username }}</span></el-menu-item
      >
      <el-menu-item
        ><span v-show="this.hasLogin"
          ><el-link @click="logout">退出</el-link></span
        ></el-menu-item
      >
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hasLogin: false,
      username: "",
      navList: [{ name: "/", navItem: "首页" }],
    };
  },
  created() {
    if (this.$store.getters.getUser != null && this.$store.getters.getUser != "") {
      if(this.$store.getters.getUser.username != null){
        this.username = this.$store.getters.getUser.username;
      }
      this.hasLogin = true
      if (this.$store.getters.getUser.admine != null && this.$store.getters.getUser.admine == true) {
        this.navList.push({ name: "/usermanager", navItem: "用户管理" });
        this.navList.push({ name: "/bookmanager", navItem: "图书管理" });
      }
      this.navList.push({ name: "/return", navItem: "还书" });
    } else {
      this.navList.push({ name: "/login", navItem: "登录" });
    }
    
  },
  methods: {
    logout() {
      const _this = this;
      _this.$axios.get("/logout", {
        headers: {
          Authorization: localStorage.getItem("token"),
        },
      });
      _this.$store.commit("REMOVE_INFO");
      _this.$router.push("/login");
    },
  },
};
</script>

<style>
.index-header{
  padding: 0px;
}
</style>