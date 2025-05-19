import axios from 'axios';

// 인증 서비스 API 설정
const authApi = axios.create({
  baseURL: 'http://localhost:9090/api/auth',
  withCredentials: true, // 리프레시 토큰 쿠키를 주고받기 위해 필요
});

// 사용자 서비스 API 설정
const userApi = axios.create({
  baseURL: 'http://localhost:9090/api',
  withCredentials: true, // 리프레시 토큰 쿠키를 주고받기 위해 필요
});

// 공통 인터셉터 설정
const setupInterceptors = (api) => {
  api.interceptors.request.use(
    (config) => {
      // 액세스 토큰을 헤더에 추가
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  api.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;
      
      // 토큰 만료 에러 처리
      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;
        
        try {
          // 리프레시 토큰은 HttpOnly 쿠키로 자동 전송됨
          const res = await authApi.post('/auth/refresh');
          const newAccessToken = res.data.accessToken;
          
          if (newAccessToken) {
            // 새로운 액세스 토큰을 localStorage에 저장
            localStorage.setItem('accessToken', newAccessToken);
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
            return api(originalRequest);
          } else {
            throw new Error('새로운 액세스 토큰을 받지 못했습니다.');
          }
        } catch (refreshError) {
          // 리프레시 토큰도 만료된 경우
          localStorage.removeItem('accessToken');
          console.error('토큰 만료 또는 인증 실패. (자동 리다이렉트 비활성화)');
          return Promise.reject(refreshError);
        }
      }
      
      // 기타 에러 처리
      if (error.response) {
        switch (error.response.status) {
          case 400:
            console.error('잘못된 요청입니다.');
            break;
          case 403:
            console.error('403 인증 실패. (자동 리다이렉트 비활성화)');
            localStorage.removeItem('accessToken');
            break;
          case 404:
            console.error('요청한 리소스를 찾을 수 없습니다.');
            break;
          case 500:
            console.error('서버 오류가 발생했습니다.');
            break;
          default:
            console.error('알 수 없는 오류가 발생했습니다.');
        }
      }
      
      return Promise.reject(error);
    }
  );
};

// 각 API에 인터셉터 설정 적용
setupInterceptors(authApi);
setupInterceptors(userApi);

// API 요청 함수들
export const authAPI = {
  // 인증 관련 API
  login: (credentials) => authApi.post('/auth/login', credentials),
  logout: () => authApi.post('/auth/logout'),
  refresh: () => authApi.post('/auth/refresh'),
  
  // 회원가입 관련 API
  requestRegister: (data) => authApi.post('/auth/register/request', data),
  requestRegisterComplete: (userData) => authApi.post('/auth/register', userData),
  
  // 메일 인증 관련 API
  sendVerificationCode: (email) => authApi.post(`/mail/send-verification?email=${encodeURIComponent(email)}`),
  verifyCode: (email, code) => authApi.post(`/mail/verify-code?email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`),
};

export const userAPI = {
  // 프로필 관련 API
  getProfile: () => userApi.get('/users/profile/me'),
  updateName: (newName) => {
    // newName이 객체면 내부 uName을 꺼내서 사용
    const uName = typeof newName === 'object' && newName.uName ? newName.uName : newName;
    return userApi.put('/users/profile/name', { uName });
  },
  
  // 비밀번호 관련 API
  resetPassword: (data) => userApi.post('/users/password/reset', data, { headers: { Authorization: '' } }),
  
  // 계정 상태 관련 API
  deactivateAccount: () => userApi.put('/users/state/deactivate'),
  reactivateAccount: () => userApi.put('/users/state/reactivate'),
  deleteAccount: () => userApi.delete('/users/profile'),
  restoreAccount: (email) => userApi.post('/users/account/restore', null, { params: { uEmail: email } }),
};

export default userApi;