import { createApp } from 'vue'
import App from './App.vue'
import router from './router'  // Assuming router is set up

const app = createApp(App)
app.use(router)
app.mount('#app') 