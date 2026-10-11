const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? 'http://localhost:8080'

export interface User {
  id: string
  email: string
  nickname: string
  createdAt: string
}

export interface TokenResponse {
  accessToken: string
  user: User
}

interface ApiErrorBody {
  success: false
  error: { code: string; message: string; details?: Record<string, unknown> }
}

export class ApiError extends Error {
  code: string
  details?: Record<string, unknown>

  constructor(code: string, message: string, details?: Record<string, unknown>) {
    super(message)
    this.code = code
    this.details = details
  }
}

async function readErrorMessage(res: Response): Promise<ApiError> {
  const body = (await res.json().catch(() => null)) as ApiErrorBody | null
  return new ApiError(
    body?.error?.code ?? 'UNKNOWN',
    body?.error?.message ?? `요청이 실패했습니다. (HTTP ${res.status})`,
    body?.error?.details,
  )
}

function readCookie(name: string): string | null {
  const match = document.cookie.match(new RegExp(`(?:^|; )${name}=([^;]*)`))
  return match ? decodeURIComponent(match[1]) : null
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    credentials: 'include',
    headers: { 'Content-Type': 'application/json', ...(init?.headers ?? {}) },
  })
  if (!res.ok) {
    throw await readErrorMessage(res)
  }
  if (res.status === 204) {
    return undefined as T
  }
  return (await res.json()) as T
}

export function primeCsrf(): Promise<void> {
  return request<void>('/api/auth/csrf-token')
}

export function signUp(payload: { email: string; password: string; nickname: string }): Promise<User> {
  return request<User>('/api/users', { method: 'POST', body: JSON.stringify(payload) })
}

export function signIn(payload: { email: string; password: string }): Promise<TokenResponse> {
  return request<TokenResponse>('/api/auth/sign-in', { method: 'POST', body: JSON.stringify(payload) })
}

export function refresh(): Promise<TokenResponse> {
  return request<TokenResponse>('/api/auth/refresh', { method: 'POST' })
}

export function signOut(accessToken: string | null): Promise<void> {
  const csrfToken = readCookie('XSRF-TOKEN')
  return request<void>('/api/auth/sign-out', {
    method: 'POST',
    headers: {
      ...(csrfToken ? { 'X-XSRF-TOKEN': csrfToken } : {}),
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
    },
  })
}

export function me(accessToken: string): Promise<User> {
  return request<User>('/api/users/me', {
    headers: { Authorization: `Bearer ${accessToken}` },
  })
}
