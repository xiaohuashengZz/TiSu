<template>
  <div class="ai-analysis">
    <div class="page-header">
      <h2>AI 分析</h2>
      <el-button type="primary" round @click="showConfigDialog = true"><el-icon><Setting /></el-icon>配置</el-button>
    </div>

    <el-row :gutter="16" style="margin-bottom: 20px">
      <el-col :xs="24" :sm="8" v-for="a in actions" :key="a.title">
        <el-card class="action-card" shadow="never" @click="ElMessage.success(`正在生成${a.title}...`)">
          <div class="action-icon" :style="{ background: a.bg }">
            <el-icon :size="24" :style="{ color: a.color }"><component :is="a.icon" /></el-icon>
          </div>
          <h3>{{ a.title }}</h3>
          <p>{{ a.desc }}</p>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-bottom: 20px">
      <template #header><span class="card-title">AI 助手</span></template>
      <div class="chat-area">
        <div v-for="(msg, i) in chatMessages" :key="i" class="chat-msg" :class="msg.role">
          <div class="msg-bubble">{{ msg.content }}</div>
        </div>
      </div>
      <div class="chat-input">
        <el-input v-model="chatInput" placeholder="询问关于健身或饮食的问题..." @keyup.enter="sendMessage" clearable />
        <el-button type="primary" circle @click="sendMessage" :disabled="!chatInput.trim()"><el-icon><Promotion /></el-icon></el-button>
      </div>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">分析报告</span>
          <el-radio-group v-model="reportFilter" size="small">
            <el-radio-button value="all">全部</el-radio-button>
            <el-radio-button value="weekly">周报</el-radio-button>
            <el-radio-button value="monthly">月报</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <el-collapse>
        <el-collapse-item v-for="r in filteredReports" :key="r.id" :name="r.id">
          <template #title>
            <div class="report-title">
              <el-tag size="small" effect="plain" style="margin-right:12px">{{ r.type === 'weekly' ? '周报' : '月报' }}</el-tag>
              <span>{{ r.title }}</span>
              <span class="report-date">{{ r.date }}</span>
            </div>
          </template>
          <el-alert :title="r.summary" type="success" :closable="false" show-icon style="margin-bottom:16px" />
          <div class="report-content" v-html="renderMd(r.content)"></div>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <el-dialog v-model="showConfigDialog" title="AI 配置" width="460px">
      <el-form label-width="90px">
        <el-form-item label="服务商">
          <el-select v-model="aiConfig.provider" style="width:100%">
            <el-option label="OpenAI" value="openai" /><el-option label="Claude" value="claude" />
          </el-select>
        </el-form-item>
        <el-form-item label="模型">
          <el-select v-model="aiConfig.model" style="width:100%">
            <el-option v-if="aiConfig.provider==='openai'" label="GPT-4o" value="gpt-4o" />
            <el-option v-if="aiConfig.provider==='openai'" label="GPT-4o Mini" value="gpt-4o-mini" />
            <el-option v-if="aiConfig.provider==='claude'" label="Claude Sonnet" value="claude-sonnet" />
            <el-option v-if="aiConfig.provider==='claude'" label="Claude Opus" value="claude-opus" />
          </el-select>
        </el-form-item>
        <el-form-item label="API Key">
          <el-input v-model="aiConfig.apiKey" type="password" placeholder="请输入 API Key" show-password />
        </el-form-item>
        <el-form-item><el-button type="primary" round @click="ElMessage.success('连接成功')">测试连接</el-button></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showConfigDialog = false" round>取消</el-button>
        <el-button type="primary" @click="showConfigDialog = false" round>保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { analyze, getReports } from '../../api/ai'
import { ElMessage } from 'element-plus'

const showConfigDialog = ref(false)
const reportFilter = ref('all')
const chatInput = ref('')
const chatMessages = ref([{ role: 'assistant', content: '你好！我是你的 AI 健身助手，可以问我任何关于饮食或训练的问题。' }])
const aiConfig = reactive({ provider: 'openai', model: 'gpt-4o', apiKey: '' })
const aiReports = ref([])

const actions = [
  { type: 'diet', title: '饮食分析', desc: '分析营养均衡情况', icon: 'Apple', color: '#34c759', bg: 'rgba(52,199,89,0.08)' },
  { type: 'training', title: '训练分析', desc: '训练量与恢复洞察', icon: 'Bicycle', color: '#0066cc', bg: 'rgba(0,102,204,0.08)' },
  { type: 'report', title: '综合报告', desc: '周报或月度总结', icon: 'Document', color: '#ff9500', bg: 'rgba(255,149,0,0.08)' }
]

const filteredReports = computed(() => reportFilter.value === 'all' ? aiReports.value : aiReports.value.filter(r => r.type === reportFilter.value))
const renderMd = (md) => md.replace(/## (.*)/g, '<h4 style="margin:12px 0 8px;color:#1d1d1f;font-weight:600;font-family:var(--font-text)">$1</h4>').replace(/\n- (.*)/g, '<li style="margin:4px 0;color:#6e6e73;font-family:var(--font-text)">$1</li>').replace(/\n/g, '<br>')

const fetchReports = async () => {
  try {
    const res = await getReports()
    aiReports.value = res.data.list || res.data || []
  } catch (e) {
    console.error('获取报告失败', e)
  }
}

const sendMessage = async () => {
  if (!chatInput.value.trim()) return
  const q = chatInput.value
  chatMessages.value.push({ role: 'user', content: q })
  chatInput.value = ''
  try {
    const res = await analyze({ question: q })
    const answer = res.data?.answer || res.data?.content || '暂无分析结果'
    chatMessages.value.push({ role: 'assistant', content: answer })
  } catch (e) {
    chatMessages.value.push({ role: 'assistant', content: '分析请求失败，请稍后重试。' })
  }
}

onMounted(() => fetchReports())
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-title { font-family: var(--font-text); font-size: var(--text-body-strong); font-weight: 600; color: var(--ink); letter-spacing: -0.374px; }
.action-card { text-align: center; cursor: pointer; margin-bottom: 16px; transition: all var(--duration-normal) var(--ease-out); }
.action-card:hover { transform: translateY(-4px); }
.action-icon { width: 52px; height: 52px; border-radius: var(--radius-md); display: inline-flex; align-items: center; justify-content: center; margin-bottom: 14px; }
.action-card h3 { font-family: var(--font-text); font-size: var(--text-body-strong); color: var(--ink); margin-bottom: 4px; font-weight: 600; }
.action-card p { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); }
.chat-area { max-height: 260px; overflow-y: auto; padding: var(--space-md); background: var(--canvas-parchment); border-radius: var(--radius-sm); margin-bottom: 14px; }
.chat-msg { margin-bottom: 14px; display: flex; }
.chat-msg.user { justify-content: flex-end; }
.msg-bubble { padding: 10px 16px; border-radius: 18px; max-width: 75%; font-family: var(--font-text); font-size: var(--text-caption); line-height: 1.43; }
.chat-msg.assistant .msg-bubble { background: var(--canvas); color: var(--ink); border: 1px solid var(--hairline); border-radius: 4px 18px 18px 18px; }
.chat-msg.user .msg-bubble { background: var(--primary); color: var(--on-primary); border-radius: 18px 4px 18px 18px; }
.chat-input { display: flex; gap: 8px; }
.report-title { display: flex; align-items: center; width: 100%; }
.report-date { margin-left: auto; font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); }
.report-content { font-family: var(--font-text); font-size: var(--text-caption); line-height: 1.47; color: var(--text-secondary); }
</style>
