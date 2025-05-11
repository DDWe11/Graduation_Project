<template>
  <div class="page-content">
    <table-bar
      :showTop="false"
      @search="search"
      @reset="reset"
      @changeColumn="changeColumn"
      :columns="columns"
    >
      <template #top>
        <el-form :model="searchForm" ref="searchFormRef" label-width="82px">
          <el-row :gutter="20">
            <form-input label="用户名" prop="name" v-model="searchForm.username" />
            <form-input label="姓名" prop="realName" v-model="searchForm.realName" />
            <form-input label="手机号" prop="phone" v-model="searchForm.phone" />
            <form-select
              label="状态"
              prop="status"
              v-model="searchForm.status"
              :options="statusOptions"
            />
          </el-row>
          <el-row :gutter="20">
            <form-select
              label="部门"
              prop="department"
              v-model="searchForm.department"
              :options="departmentOptions"
            />
            <form-select
              label="角色"
              prop="roleCode"
              v-model="searchForm.roleCode"
              :options="roleOptions"
            />
          </el-row>
        </el-form>
      </template>
      <template #bottom>
        <el-button @click="showDialog('add')" v-ripple>添加用户</el-button>
      </template>
    </table-bar>

    <!--    <art-table :data="tableData" selection :currentPage="1" :pageSize="10" :total="50">-->
    <!--    <art-table-->
    <!--      v-model:current-page="currentPage"-->
    <!--      v-model:page-size="pageSize"-->
    <!--      :data="tableData"-->
    <!--      :total="total"-->
    <!--      selection-->
    <!--      :pagination="true"-->
    <!--      @selection-change="handleSelectionChange"-->
    <!--    >-->
    <art-table
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :data="tableData"
      :total="total"
      selection
      :pagination="true"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
      @selection-change="handleSelectionChange"
    >
      <template #default>
        <el-table-column label="用户名" prop="realName" width="200px" v-if="columns[0].show">
          <template #default="scope">
            <div class="user" style="display: flex; align-items: center">
              <img class="avatar" :src="scope.row.gender === 'MALE' ? maleAvatar : femaleAvatar" />
              <div>
                <p class="user-name">{{ scope.row.realName }}</p>
                <p class="email">{{ scope.row.jobNumber }}</p>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="手机号" prop="phone" v-if="columns[1].show" />

        <el-table-column label="性别" prop="gender" sortable v-if="columns[2].show">
          <template #default="scope">
            <div style="margin-left: 10px">
              {{ scope.row.gender === 'MALE' ? '男' : '女' }}
            </div>
          </template>
        </el-table-column>

        <el-table-column label="部门" prop="department" v-if="columns[3].show" />

        <el-table-column
          label="状态"
          prop="status"
          :filters="[
            { text: '未启用', value: '0' },
            { text: '启用', value: '1' }
          ]"
          :filter-method="filterTag"
          filter-placement="bottom-end"
          v-if="columns[4].show"
        >
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.status)">
              {{ buildTagText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="入职日期" prop="entryDate" sortable v-if="columns[5].show" />

        <el-table-column fixed="right" label="操作" width="180px">
          <template #default="scope">
            <button-table type="edit" @click="showDialog('edit', scope.row)" />
            <button-table type="reset" @click="resetPassword(scope.row.userId)" />
            <button-table type="delete" @click="deleteUser(scope.row.userId)" />
          </template>
        </el-table-column>
      </template>
    </art-table>
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加用户' : '编辑用户'"
      width="50%"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" />
        </el-form-item>
        <!--        <el-form-item label="密码" prop="password">-->
        <!--          <el-input v-model="formData.password" type="password" />-->
        <!--        </el-form-item>-->
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="formData.gender">
            <el-option label="男" value="MALE" />
            <el-option label="女" value="FEMALE" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="formData.department">
            <el-option label="安全环保部" value="安全环保部" />
            <el-option label="设备维护部" value="设备维护部" />
            <el-option label="技术部" value="技术部" />
            <el-option label="运营部" value="运营部" />
            <el-option label="行政与后勤部" value="行政与后勤部" />
            <el-option label="监测部" value="监测部" />
          </el-select>
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="formData.position" />
        </el-form-item>
        <el-form-item label="角色" prop="roleCode">
          <el-select v-model="formData.roleCode">
            <el-option label="运行主管" value="SUPERVISOR" />
            <el-option label="操作员" value="OPERATOR" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status">
            <el-option label="启用" :value="1" />
            <el-option label="未启用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>
        <el-form-item label="紧急联系人" prop="emergencyContact">
          <el-input v-model="formData.emergencyContact" />
        </el-form-item>
        <el-form-item label="紧急联系电话" prop="emergencyPhone">
          <el-input v-model="formData.emergencyPhone" />
        </el-form-item>
        <el-form-item label="入职日期" prop="entryDate">
          <el-date-picker
            v-model="formData.entryDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { FormInstance } from 'element-plus'
  import { ElMessageBox, ElMessage } from 'element-plus'
  import type { FormRules } from 'element-plus'
  import api from '@/utils/http'
  import maleAvatar from '@/assets/img/avatar/avatar-male.jpg'
  import femaleAvatar from '@/assets/img/avatar/avatar-female.jpg'

  const dialogType = ref('add')
  const dialogVisible = ref(false)

  const currentPage = ref(1)
  const pageSize = ref(10)
  const total = ref(0)
  const selectedUsers = ref<any[]>([])

  const handleSizeChange = (size: number) => {
    pageSize.value = size
    currentPage.value = 1 // 改变 pageSize 通常会重置页码
    fetchUserList()
  }

  const buildCleanParams = (params: Record<string, any>) => {
    const cleaned: Record<string, any> = {}
    for (const key in params) {
      const value = params[key]
      if (value !== '' && value !== undefined && value !== null) {
        cleaned[key] = value
      }
    }
    return cleaned
  }

  const fetchUserList = async () => {
    try {
      const response = await api.get<UserListResponse>({
        url: '/auth/admin/users',
        params: {
          page: currentPage.value,
          size: pageSize.value
        }
      })
      if (response.code === 200) {
        tableData.value = response.data.users
        total.value = response.data.total

        console.log('分页总数 total:', total.value)
      } else {
        ElMessage.error('获取用户列表失败')
      }
    } catch (error) {
      ElMessage.error('请求出错，请检查后端接口')
      console.error(error)
    }
  }

  const handlePageChange = (page: number) => {
    currentPage.value = page
    fetchUserList()
  }

  const handleSelectionChange = (val: any[]) => {
    selectedUsers.value = val
    ElMessage.info(`已选择 ${val.length} 个用户`)
  }

  const formData = reactive({
    userId: null,
    username: '',
    // password: '',
    status: undefined, // 修改：null → undefined
    realName: '',
    gender: '',
    department: '',
    position: '',
    roleCode: '', // 修改：null → undefined
    phone: '',
    email: '',
    emergencyContact: '',
    emergencyPhone: '',
    entryDate: ''
  })

  // 用户状态选项
  const statusOptions = [
    { label: '全部', value: '' },
    { label: '启用', value: '1' },
    { label: '禁用', value: '0' }
  ]

  // 部门选项
  const departmentOptions = [
    { label: '全部', value: '' },
    { label: '安全环保部', value: '安全环保部' },
    { label: '设备维护部', value: '设备维护部' },
    { label: '技术部', value: '技术部' },
    { label: '运营部', value: '运营部' },
    { label: '行政与后勤部', value: '行政与后勤部' },
    { label: '监测部', value: '监测部' }
  ]

  // 角色选项
  const roleOptions = [
    { label: '全部', value: '' },
    { label: '运行主管', value: 'SUPERVISOR' },
    { label: '操作员', value: 'OPERATOR' }
  ]

  const columns = reactive([
    { name: '用户姓名', show: true },
    { name: '手机号', show: true },
    { name: '性别', show: true },
    { name: '部门', show: true },
    { name: '状态', show: true },
    { name: '入职日期', show: true }
  ])

  const searchFormRef = ref<FormInstance>()
  const searchForm = reactive({
    username: '',
    realName: '',
    phone: '',
    status: '', // string 类型，防止组件报错
    department: '',
    roleCode: ''
  })

  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  const reset = () => {
    resetForm(searchFormRef.value)
    fetchUserListWithParams({}) // 清空筛选条件，请求全部用户
  }
  const tableData = ref<any[]>([])

  interface User {
    userId: number
    username: string
    realName: string
    gender: string
    department: string
    status: number
    jobNumber: string
    phone: string
    email: string
    entryDate: string
  }

  interface UserListResponse {
    code: number
    message: string
    data: {
      total: number
      users: User[]
    }
  }

  // onMounted(fetchUserList)
  onMounted(() => {
    fetchUserList()
  })

  const showDialog = (type: string, row?: any) => {
    dialogVisible.value = true
    dialogType.value = type

    if (type === 'edit' && row) {
      formData.userId = row.userId // 关键点：隐藏但绑定
      formData.username = row.username
      formData.realName = row.realName
      formData.phone = row.phone
      formData.gender = row.gender
      formData.department = row.department
      formData.position = row.position
      formData.roleCode = row.roleCode
      formData.status = row.status
      formData.email = row.email
      formData.emergencyContact = row.emergencyContact
      formData.emergencyPhone = row.emergencyPhone
      formData.entryDate = row.entryDate
    } else {
      formData.userId = null // 新增时不带 ID
      formData.username = ''
      // formData.password = ''
      formData.status = undefined
      formData.realName = ''
      formData.gender = ''
      formData.department = ''
      formData.position = ''
      formData.roleCode = ''
      formData.phone = ''
      formData.email = ''
      formData.emergencyContact = ''
      formData.emergencyPhone = ''
      formData.entryDate = ''
    }
  }

  const deleteUser = async (userId: number) => {
    ElMessageBox.confirm('确定要注销该用户吗？', '注销用户', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    }).then(async () => {
      try {
        await api.delete({ url: `/auth/admin/users/${userId}` })
        ElMessage.success('注销成功')
        // 局部刷新：从 tableData 中移除对应用户
        tableData.value = tableData.value.filter((item) => item.userId !== userId)
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败，请重试')
      }
    })
  }

  const resetPassword = async (userId: number) => {
    ElMessageBox.confirm('确定要重置该用户的密码？', '重置密码', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      try {
        await api.put({ url: `/auth/admin/users/${userId}/password/reset` })
        ElMessage.success('密码重置成功，默认密码为：88888')
      } catch (error) {
        console.error('密码重置失败:', error)
        ElMessage.error('密码重置失败，请重试')
      }
    })
  }
  const search = () => {
    const rawParams = {
      username: searchForm.username,
      realName: searchForm.realName,
      phone: searchForm.phone,
      department: searchForm.department,
      roleCode: searchForm.roleCode,
      status: searchForm.status === '' ? undefined : Number(searchForm.status)
    }

    const params = buildCleanParams(rawParams)

    fetchUserListWithParams(params)
  }
  const fetchUserListWithParams = async (params: Record<string, any>) => {
    try {
      const response = await api.get<UserListResponse>({
        url: '/auth/admin/users',
        params
      })

      if (response.code === 200) {
        tableData.value = response.data.users
        total.value = response.data.total
      } else {
        ElMessage.error('获取用户列表失败')
      }
    } catch (error) {
      ElMessage.error('请求出错，请检查后端接口')
      console.error(error)
    }
  }

  const changeColumn = (list: any) => {
    columns.values = list
  }

  const filterTag = (value: string, row: any) => {
    return String(row.status) === value
  }

  const getTagType = (status: number) => {
    switch (status) {
      case 1:
        return 'success'
      case 0:
        return 'danger'
      default:
        return 'info'
    }
  }

  const buildTagText = (status: number) => {
    if (status === 1) {
      return '启用'
    } else if (status === 0) {
      return '未启用'
    } else {
      return '未知'
    }
  }

  const rules = reactive<FormRules>({
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }],
    realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
    gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
    department: [{ required: true, message: '请选择部门', trigger: 'change' }],
    position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
    roleCode: [{ required: true, message: '请选择角色', trigger: 'change' }],
    entryDate: [{ required: true, message: '请选择入职日期', trigger: 'change' }],
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
    ],
    email: [
      {
        type: 'email',
        message: '请输入合法的邮箱地址',
        trigger: ['blur', 'change']
      }
    ]
  })

  const formRef = ref<FormInstance>()

  //
  const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
      if (valid) {
        const payload = {
          username: formData.username,
          // password: formData.password,
          status: formData.status,
          real_name: formData.realName,
          gender: formData.gender, // 注意：这里直接传 MALE / FEMALE
          department: formData.department,
          position: formData.position,
          roleCode: formData.roleCode,
          phone: formData.phone,
          email: formData.email,
          emergencyContact: formData.emergencyContact,
          emergencyPhone: formData.emergencyPhone,
          entry_date: formData.entryDate
        }

        try {
          if (dialogType.value === 'add') {
            // 添加用户
            await api.post({
              url: '/auth/admin/users',
              params: payload
            })
            ElMessage.success('添加成功')
          } else if (dialogType.value === 'edit') {
            // 修改用户（需要带 userId）
            await api.patch({
              url: '/auth/admin/users/update',
              data: {
                userId: formData.userId,
                ...payload
              },
              headers: {
                'Content-Type': 'application/json'
              }
            })
            ElMessage.success('更新成功')
          }

          dialogVisible.value = false

          // 刷新列表：重新拉取后端数据
          const response = await api.get<UserListResponse>({
            url: '/auth/admin/users'
          })
          if (response.code === 200) {
            tableData.value = response.data.users
          } else {
            ElMessage.error('刷新列表失败，请手动刷新')
          }
        } catch (error) {
          ElMessage.error('操作失败，请重试')
          console.error(error)
        }
      }
    })
  }
</script>

<style lang="scss" scoped>
  .page-content {
    width: 100%;
    height: 100%;

    .user {
      .avatar {
        width: 40px;
        height: 40px;
        border-radius: 6px;
      }

      > div {
        margin-left: 10px;

        .user-name {
          font-weight: 500;
          color: var(--art-text-gray-800);
        }
      }
    }
  }
</style>
