<template>
  <div class="about-container">
    <div class="about-card">
      <h1 class="about-title">关于智绘时光</h1>
      
      <el-divider></el-divider>
      
      <div class="about-content">
        <section class="intro-section">
          <h2>项目简介</h2>
          <p>智绘时光是一个以AI驱动的智能日记管理系统，旨在帮助用户轻松记录生活点滴，高效整理个人事务。</p>
          <p>本项目利用先进的人工智能技术，让一句话就能生成详细的日记内容，自动分析日记生成报告，智能提取待办事项。</p>
        </section>
        
        <section class="features-section">
          <h2>核心功能</h2>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="feature-item">
                <i class="el-icon feature-icon">📝</i>
                <h3>智能日记</h3>
                <p>只需一句话，AI即可生成完整、详细的日记内容</p>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="feature-item">
                <i class="el-icon feature-icon">📊</i>
                <h3>一键报告</h3>
                <p>自动分析日记，生成日报、周报、月报</p>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="feature-item">
                <i class="el-icon feature-icon">✓</i>
                <h3>待办管理</h3>
                <p>AI分析日记内容，自动提取和生成待办事项</p>
              </div>
            </el-col>
          </el-row>
        </section>
        
        <section class="tech-section">
          <h2>技术栈</h2>
          <div class="tech-list">
            <div class="tech-category">
              <h3>前端</h3>
              <ul>
                <li>Vue 3</li>
                <li>Element Plus</li>
                <li>TypeScript</li>
                <li>Vite</li>
              </ul>
            </div>
            <div class="tech-category">
              <h3>后端</h3>
              <ul>
                <li>Spring Boot</li>
                <li>Spring Security</li>
                <li>MySQL</li>
                <li>Redis</li>
              </ul>
            </div>
          </div>
        </section>
        
        <section class="team-section">
          <h2>关于我们</h2>
          <p>我们是一支热爱技术、注重用户体验的团队，致力于用AI技术简化用户的日常记录和管理工作。</p>
          <p>如果您有任何建议或反馈，欢迎与我们联系。</p>
        </section>
      </div>
      
      <el-divider></el-divider>
      
      <div class="about-footer">
        <p>&copy; 2024 智绘时光团队 | 版本 1.0.0</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.about-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 30px;
  margin-bottom: 30px;
}

.about-title {
  text-align: center;
  font-size: 2rem;
  color: #333;
  margin-bottom: 20px;
}

.about-content {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.intro-section p,
.team-section p {
  color: #555;
  line-height: 1.7;
  margin-bottom: 15px;
}

h2 {
  font-size: 1.5rem;
  color: #4caf50;
  margin-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 8px;
}

.feature-item {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  background: #f9f9f9;
  height: 100%;
  transition: transform 0.3s;
}

.feature-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 15px;
  display: block;
}

.feature-item h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #333;
}

.feature-item p {
  color: #666;
  font-size: 0.9rem;
}

.tech-list {
  display: flex;
  gap: 40px;
  flex-wrap: wrap;
}

.tech-category {
  flex: 1;
  min-width: 200px;
}

.tech-category h3 {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 15px;
}

.tech-category ul {
  list-style: none;
  padding: 0;
}

.tech-category li {
  padding: 8px 0;
  border-bottom: 1px dashed #eee;
  color: #555;
}

.tech-category li:last-child {
  border-bottom: none;
}

.about-footer {
  text-align: center;
  color: #888;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .tech-list {
    flex-direction: column;
    gap: 20px;
  }
}
</style>
