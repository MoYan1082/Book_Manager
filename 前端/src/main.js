import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'
import "element-ui/lib/theme-chalk/index.css"

// 引入axios配置
import './axios'
// 引入前端路由拦截
import "./permission"

Vue.use(Element)


Vue.config.productionTip = false
Vue.prototype.$axios = axios


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
