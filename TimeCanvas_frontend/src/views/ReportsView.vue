<template>
  <div class="reports-container">
    <h1>报告中心</h1>
    <p class="coming-soon">功能开发中，敬请期待...</p>
    
    <div class="placeholder-container">
      <div class="placeholder-card">
        <div class="placeholder-header"></div>
        <div class="placeholder-content">
          <div class="placeholder-line"></div>
          <div class="placeholder-line"></div>
          <div class="placeholder-line"></div>
          <div class="placeholder-line"></div>
        </div>
      </div>
      
      <div class="placeholder-card">
        <div class="placeholder-header"></div>
        <div class="placeholder-content">
          <div class="placeholder-line"></div>
          <div class="placeholder-line"></div>
          <div class="placeholder-line"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.reports-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

h1 {
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.coming-soon {
  text-align: center;
  font-size: 18px;
  color: #666;
  margin-bottom: 40px;
}

.placeholder-container {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
}

.placeholder-card {
  flex: 1;
  min-width: 300px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  padding-bottom: 20px;
}

.placeholder-header {
  height: 120px;
  background: linear-gradient(to right, #f5f7fa, #c3cfe2);
  margin-bottom: 20px;
}

.placeholder-content {
  padding: 0 20px;
}

.placeholder-line {
  height: 20px;
  background: #eee;
  border-radius: 4px;
  margin-bottom: 15px;
}

.placeholder-line:nth-child(2) {
  width: 80%;
}

.placeholder-line:nth-child(3) {
  width: 60%;
}

.placeholder-line:nth-child(4) {
  width: 70%;
}
</style> 