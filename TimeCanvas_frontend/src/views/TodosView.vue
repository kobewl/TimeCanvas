<template>
  <div class="todos-container">
    <h1>待办事项</h1>
    <p class="coming-soon">功能开发中，敬请期待...</p>
    
    <div class="placeholder-container">
      <div class="todo-card">
        <div class="todo-header">
          <h2>今日待办</h2>
          <span class="todo-badge">3</span>
        </div>
        <ul class="todo-list">
          <li class="todo-item">
            <div class="todo-checkbox"></div>
            <div class="todo-content">
              <div class="todo-text">完成产品设计文档</div>
              <div class="todo-date">截止日期：2024-05-25</div>
            </div>
          </li>
          <li class="todo-item">
            <div class="todo-checkbox"></div>
            <div class="todo-content">
              <div class="todo-text">与客户沟通项目进度</div>
              <div class="todo-date">截止日期：2024-05-20</div>
            </div>
          </li>
          <li class="todo-item">
            <div class="todo-checkbox"></div>
            <div class="todo-content">
              <div class="todo-text">检查并回复邮件</div>
              <div class="todo-date">截止日期：2024-05-19</div>
            </div>
          </li>
        </ul>
      </div>
      
      <div class="todo-card">
        <div class="todo-header">
          <h2>未来待办</h2>
          <span class="todo-badge">2</span>
        </div>
        <ul class="todo-list">
          <li class="todo-item">
            <div class="todo-checkbox"></div>
            <div class="todo-content">
              <div class="todo-text">准备团队会议演示文稿</div>
              <div class="todo-date">截止日期：2024-05-30</div>
            </div>
          </li>
          <li class="todo-item">
            <div class="todo-checkbox"></div>
            <div class="todo-content">
              <div class="todo-text">完成项目阶段性总结</div>
              <div class="todo-date">截止日期：2024-06-05</div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.todos-container {
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

.todo-card {
  flex: 1;
  min-width: 300px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.todo-header {
  padding: 20px;
  background: #f5f7fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.todo-header h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.todo-badge {
  background: #4caf50;
  color: white;
  border-radius: 20px;
  padding: 3px 10px;
  font-size: 14px;
  font-weight: bold;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.todo-item {
  display: flex;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  align-items: center;
}

.todo-item:last-child {
  border-bottom: none;
}

.todo-checkbox {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid #4caf50;
  margin-right: 15px;
  cursor: pointer;
}

.todo-content {
  flex: 1;
}

.todo-text {
  font-size: 16px;
  margin-bottom: 5px;
  color: #333;
}

.todo-date {
  font-size: 12px;
  color: #888;
}
</style> 