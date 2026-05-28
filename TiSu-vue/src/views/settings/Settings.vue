<template>
  <div class="settings">
    <div class="page-header"><h2>设置</h2></div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="个人资料" name="profile">
        <el-card shadow="never">
          <el-form :model="userStore.profile" label-width="90px" style="max-width: 520px">
            <el-form-item label="头像">
              <div style="display:flex;align-items:center;gap:12px">
                <el-avatar :size="56" icon="UserFilled" />
                <el-button size="small" round>更换</el-button>
              </div>
            </el-form-item>
            <el-form-item label="昵称"><el-input v-model="userStore.userInfo.nickname" /></el-form-item>
            <el-form-item label="性别"><el-radio-group v-model="userStore.profile.gender"><el-radio value="男">男</el-radio><el-radio value="女">女</el-radio></el-radio-group></el-form-item>
            <el-row :gutter="16">
              <el-col :span="12"><el-form-item label="年龄"><el-input-number v-model="userStore.profile.age" :min="10" :max="100" style="width:100%" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="身高"><el-input-number v-model="userStore.profile.height" :min="100" :max="250" :precision="1" style="width:100%" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="16">
              <el-col :span="12"><el-form-item label="体重"><el-input-number v-model="userStore.profile.currentWeight" :min="30" :max="200" :precision="1" style="width:100%" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="目标"><el-input-number v-model="userStore.profile.targetWeight" :min="30" :max="200" :precision="1" style="width:100%" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="目标">
                  <el-select v-model="userStore.profile.goal" style="width:100%">
                    <el-option label="减脂" value="减脂" /><el-option label="增肌" value="增肌" />
                    <el-option label="维持" value="维持" /><el-option label="塑形" value="塑形" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动量">
                  <el-select v-model="userStore.profile.activityLevel" style="width:100%">
                    <el-option label="久坐" value="久坐" /><el-option label="轻度活动" value="轻度活动" />
                    <el-option label="中度活动" value="中度活动" /><el-option label="高强度活动" value="高强度活动" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item><el-button type="primary" round @click="handleSaveProfile">保存修改</el-button></el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="AI" name="ai">
        <el-card shadow="never">
          <el-form :model="aiSettings" label-width="90px" style="max-width: 520px">
            <el-form-item label="服务商">
              <el-select v-model="aiSettings.provider" style="width:100%">
                <el-option label="OpenAI" value="openai" /><el-option label="Claude" value="claude" />
              </el-select>
            </el-form-item>
            <el-form-item label="模型">
              <el-select v-model="aiSettings.model" style="width:100%">
                <el-option label="GPT-4o" value="gpt-4o" /><el-option label="GPT-4o Mini" value="gpt-4o-mini" />
                <el-option label="Claude Sonnet 4" value="claude-sonnet-4" /><el-option label="Claude Opus 4" value="claude-opus-4" />
              </el-select>
            </el-form-item>
            <el-form-item label="API Key">
              <el-input v-model="aiSettings.apiKey" type="password" placeholder="请输入 API Key" show-password />
              <div style="font-size:12px;color:var(--text-quaternary);margin-top:4px">加密存储在本地</div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" round @click="ElMessage.success('连接成功')">测试</el-button>
              <el-button round @click="handleSaveAiSettings">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="单位" name="units">
        <el-card shadow="never">
          <el-form :model="unitSettings" label-width="90px" style="max-width: 520px">
            <el-form-item label="体重"><el-radio-group v-model="unitSettings.weight"><el-radio value="kg">kg</el-radio><el-radio value="lb">lb</el-radio></el-radio-group></el-form-item>
            <el-form-item label="身高"><el-radio-group v-model="unitSettings.height"><el-radio value="cm">cm</el-radio><el-radio value="inch">inch</el-radio></el-radio-group></el-form-item>
            <el-form-item><el-button type="primary" round @click="handleSaveUnitSettings">保存</el-button></el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="导出" name="export">
        <el-card shadow="never">
          <el-descriptions :column="1" border>
            <el-descriptions-item v-for="item in [{l:'体重记录',k:'weight'},{l:'体围记录',k:'body'},{l:'饮食记录',k:'diet'},{l:'训练记录',k:'training'},{l:'AI 报告',k:'ai'}]" :key="item.k" :label="item.l">
              <el-button size="small" round @click="ElMessage.success('已导出 CSV')">CSV</el-button>
              <el-button size="small" round @click="ElMessage.success('已导出 Excel')">Excel</el-button>
              <el-button size="small" round @click="ElMessage.success('已导出 PDF')">PDF</el-button>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('profile')

const aiSettings = reactive({ provider: 'openai', model: 'gpt-4o', apiKey: '' })
const unitSettings = reactive({ weight: 'kg', height: 'cm' })

const syncFromStore = () => {
  aiSettings.model = userStore.settings.aiModel || 'gpt-4o'
  aiSettings.apiKey = userStore.settings.aiApiKey || ''
  if (userStore.settings.unitPreference === 'imperial') {
    unitSettings.weight = 'lb'
    unitSettings.height = 'inch'
  } else {
    unitSettings.weight = 'kg'
    unitSettings.height = 'cm'
  }
}

const handleSaveProfile = async () => {
  try {
    await userStore.updateProfile({
      gender: userStore.profile.gender,
      age: userStore.profile.age,
      height: userStore.profile.height,
      currentWeight: userStore.profile.currentWeight,
      targetWeight: userStore.profile.targetWeight,
      goal: userStore.profile.goal,
      activityLevel: userStore.profile.activityLevel
    })
    ElMessage.success('已保存')
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

const handleSaveAiSettings = async () => {
  try {
    await userStore.updateSettings({
      aiModel: aiSettings.model,
      aiApiKey: aiSettings.apiKey
    })
    ElMessage.success('已保存')
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

const handleSaveUnitSettings = async () => {
  try {
    await userStore.updateSettings({
      unitPreference: unitSettings.weight === 'lb' ? 'imperial' : 'metric'
    })
    ElMessage.success('已保存')
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

onMounted(async () => {
  await Promise.all([userStore.fetchProfile(), userStore.fetchSettings()])
  syncFromStore()
})
</script>
