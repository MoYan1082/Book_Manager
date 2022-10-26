import Vue from 'vue'
import VueRouter from 'vue-router'

import Index from '../views/index.vue'
import UserManager from '../views/userManager.vue'
import BookManager from '../views/bookManager.vue'
import ErrorPage from '../views/errorPage.vue'
import Login from '../views/login.vue'
import Return from '../views/return.vue'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Index',
    component: Index,
  },
  {
    path: '/usermanager',
    name: 'UserManager',
    component: UserManager,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/bookmanager',
    name: 'BookManager',
    component: BookManager,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  
  {
    path: '/return',
    name: 'Return',
    component: Return,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/*',
    name: 'ErrorPage',
    component: ErrorPage
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
