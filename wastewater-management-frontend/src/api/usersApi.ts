import request from '@/utils/http'
import { BaseResult } from '@/types/axios'
import { LoginParams } from './model/loginModel'
import { useUserStore } from '@/store/modules/user'

export class UserService {
  static async login(params: LoginParams) {
    const response = await request.post<BaseResult>({
      url: '/api/auth/login',
      params
    })

    if (response.code === 200 && response.data.token) {
      const userStore = useUserStore()

      // 保存 token 和用户信息到 Pinia
      userStore.setUserInfo({
        token: response.data.token,
        username: response.data.username,
        realName: response.data.realName,
        roleCode: response.data.roleCode,
        loginTime: response.data.loginTime
      })

      userStore.setLoginStatus(true)
      userStore.saveUserData()
    }

    return response // 返回后端响应
  }
}
