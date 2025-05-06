import request from '@/utils/http'
import { BaseResult } from '@/types/axios'
import { LoginParams } from './model/loginModel'

export class UserService {
  static async login(params: LoginParams) {
    const response = await request.post<BaseResult>({
      url: 'http://localhost:8080/api/auth/login',
      params
    })
    // 登录成功后保存 Token
    if (response.code === 200 && response.data.token) {
      const token = response.data.token // 获取 Token
      localStorage.setItem('token', token) // 保存 Token 到 LocalStorage
    }

    return response // 返回后端响应
  }
}
