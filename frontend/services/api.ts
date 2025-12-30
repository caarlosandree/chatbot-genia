import type {
  LoginRequest,
  JwtResponse,
  AuthTokens,
} from '@/types/auth'
import type {
  Plan,
  CreatePlanInput,
  UpdatePlanInput,
} from '@/types/plan'
import type {
  Organization,
  CreateOrganizationInput,
  UpdateOrganizationInput,
} from '@/types/organization'
import type {
  Subscription,
  CreateSubscriptionInput,
  PlanLimitsResponse,
} from '@/types/subscription'
import { isTokenExpired } from '@/utils/jwt'

/**
 * URL base da API backend
 * Pode ser configurada via variável de ambiente NEXT_PUBLIC_API_URL
 */
const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

/**
 * Storage keys para tokens
 */
const STORAGE_KEYS = {
  ACCESS_TOKEN: 'auth_access_token',
  REFRESH_TOKEN: 'auth_refresh_token',
} as const

/**
 * Obtém os tokens do localStorage
 */
function getStoredTokens(): AuthTokens | null {
  if (typeof window === 'undefined') {
    return null
  }

  const accessToken = localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN)
  const refreshToken = localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN)

  if (!accessToken || !refreshToken) {
    return null
  }

  return { accessToken, refreshToken }
}

/**
 * Armazena tokens no localStorage
 */
function setStoredTokens(tokens: AuthTokens): void {
  if (typeof window === 'undefined') {
    return
  }

  localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, tokens.accessToken)
  localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, tokens.refreshToken)
}

/**
 * Remove tokens do localStorage
 */
function clearStoredTokens(): void {
  if (typeof window === 'undefined') {
    return
  }

  localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN)
  localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN)
}

/**
 * Flag para evitar múltiplas chamadas de refresh simultâneas
 */
let isRefreshing = false
let refreshPromise: Promise<AuthTokens | null> | null = null

/**
 * Realiza refresh do token
 */
async function refreshToken(
  refreshTokenValue: string
): Promise<AuthTokens | null> {
  try {
    const response = await fetch(`${API_BASE_URL}/api/v1/auth/refresh`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ refreshToken: refreshTokenValue }),
    })

    if (!response.ok) {
      return null
    }

    const data: JwtResponse = await response.json()
    const tokens: AuthTokens = {
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
    }

    setStoredTokens(tokens)
    return tokens
  } catch (error) {
    console.error('Erro ao renovar token:', error)
    return null
  }
}

/**
 * Faz requisição HTTP com interceptors para adicionar token e refresh automático
 */
async function fetchWithAuth(
  url: string,
  options: RequestInit = {}
): Promise<Response> {
  const tokens = getStoredTokens()
  const accessToken = tokens?.accessToken

  // Adiciona token de autorização se existir
  const headers = new Headers(options.headers)
  if (accessToken && !isTokenExpired(accessToken)) {
    headers.set('Authorization', `Bearer ${accessToken}`)
  }

  // Faz a requisição
  let response = await fetch(url, {
    ...options,
    headers,
  })

  // Se recebeu 401 e temos refresh token, tenta renovar
  if (response.status === 401 && tokens?.refreshToken) {
    // Evita múltiplas chamadas de refresh simultâneas
    if (!isRefreshing) {
      isRefreshing = true
      refreshPromise = refreshToken(tokens.refreshToken)
    }

    const newTokens = await refreshPromise
    isRefreshing = false
    refreshPromise = null

    if (newTokens) {
      // Refaz a requisição com o novo token
      headers.set('Authorization', `Bearer ${newTokens.accessToken}`)
      response = await fetch(url, {
        ...options,
        headers,
      })
    } else {
      // Refresh falhou, limpa tokens
      clearStoredTokens()
      throw new Error('Sessão expirada. Faça login novamente.')
    }
  }

  return response
}

/**
 * Cliente HTTP base para requisições à API
 */
export const api = {
  /**
   * Faz uma requisição GET
   */
  async get<T>(endpoint: string): Promise<T> {
    const response = await fetchWithAuth(`${API_BASE_URL}${endpoint}`)

    if (!response.ok) {
      throw new Error(`Erro na requisição: ${response.statusText}`)
    }

    return response.json()
  },

  /**
   * Faz uma requisição POST
   */
  async post<T>(endpoint: string, data?: unknown): Promise<T> {
    const response = await fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: data ? JSON.stringify(data) : undefined,
    })

    if (!response.ok) {
      throw new Error(`Erro na requisição: ${response.statusText}`)
    }

    return response.json()
  },

  /**
   * Faz uma requisição PUT
   */
  async put<T>(endpoint: string, data?: unknown): Promise<T> {
    const response = await fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: data ? JSON.stringify(data) : undefined,
    })

    if (!response.ok) {
      throw new Error(`Erro na requisição: ${response.statusText}`)
    }

    return response.json()
  },

  /**
   * Faz uma requisição DELETE
   */
  async delete<T>(endpoint: string): Promise<T> {
    const response = await fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      throw new Error(`Erro na requisição: ${response.statusText}`)
    }

    return response.json()
  },
}

/**
 * Serviços de autenticação
 */
export const authService = {
  /**
   * Realiza login
   */
  async login(credentials: LoginRequest): Promise<JwtResponse> {
    const response = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    })

    if (!response.ok) {
      const error = await response.text()
      throw new Error(error || 'Erro ao fazer login')
    }

    const data: JwtResponse = await response.json()

    // Armazena tokens
    setStoredTokens({
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
    })

    return data
  },

  /**
   * Realiza logout
   */
  async logout(): Promise<void> {
    const tokens = getStoredTokens()

    if (tokens?.refreshToken) {
      try {
        await fetch(`${API_BASE_URL}/api/v1/auth/logout`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ refreshToken: tokens.refreshToken }),
        })
      } catch (error) {
        console.error('Erro ao fazer logout no servidor:', error)
      }
    }

    // Limpa tokens localmente de qualquer forma
    clearStoredTokens()
  },

  /**
   * Obtém tokens armazenados
   */
  getStoredTokens,

  /**
   * Remove tokens armazenados
   */
  clearStoredTokens,
}

/**
 * Serviços de planos
 */
export const planService = {
  /**
   * Lista todos os planos ativos
   */
  async getAll(): Promise<Plan[]> {
    return api.get<Plan[]>('/api/v1/plans')
  },

  /**
   * Lista todos os planos, incluindo inativos (apenas ADMIN)
   */
  async getAllPlans(): Promise<Plan[]> {
    return api.get<Plan[]>('/api/v1/plans/all')
  },

  /**
   * Busca um plano por ID
   */
  async getById(id: number): Promise<Plan> {
    return api.get<Plan>(`/api/v1/plans/${id}`)
  },

  /**
   * Cria um novo plano (apenas ADMIN)
   */
  async create(data: CreatePlanInput): Promise<Plan> {
    return api.post<Plan>('/api/v1/plans', data)
  },

  /**
   * Atualiza um plano existente (apenas ADMIN)
   */
  async update(id: number, data: UpdatePlanInput): Promise<Plan> {
    return api.put<Plan>(`/api/v1/plans/${id}`, data)
  },

  /**
   * Deleta um plano (apenas ADMIN)
   */
  async delete(id: number): Promise<void> {
    return api.delete<void>(`/api/v1/plans/${id}`)
  },
}

/**
 * Serviços de organizações
 */
export const organizationService = {
  /**
   * Cria uma nova organização
   */
  async create(data: CreateOrganizationInput): Promise<Organization> {
    return api.post<Organization>('/api/v1/organizations', data)
  },

  /**
   * Busca uma organização por ID
   */
  async getById(id: number): Promise<Organization> {
    return api.get<Organization>(`/api/v1/organizations/${id}`)
  },

  /**
   * Retorna a organização do usuário atual
   */
  async getMyOrganization(): Promise<Organization> {
    return api.get<Organization>('/api/v1/organizations/me')
  },

  /**
   * Atualiza uma organização
   */
  async update(
    id: number,
    data: UpdateOrganizationInput
  ): Promise<Organization> {
    return api.put<Organization>(`/api/v1/organizations/${id}`, data)
  },
}

/**
 * Serviços de assinaturas
 */
export const subscriptionService = {
  /**
   * Cria uma nova assinatura
   */
  async create(data: CreateSubscriptionInput): Promise<Subscription> {
    return api.post<Subscription>('/api/v1/subscriptions', data)
  },

  /**
   * Retorna a assinatura ativa do usuário atual
   */
  async getMySubscription(): Promise<Subscription> {
    return api.get<Subscription>('/api/v1/subscriptions/me')
  },

  /**
   * Busca uma assinatura por ID
   */
  async getById(id: number): Promise<Subscription> {
    return api.get<Subscription>(`/api/v1/subscriptions/${id}`)
  },

  /**
   * Cancela uma assinatura
   */
  async cancel(id: number): Promise<Subscription> {
    return api.put<Subscription>(`/api/v1/subscriptions/${id}/cancel`, {})
  },

  /**
   * Retorna os limites atuais do plano do usuário
   */
  async getLimits(): Promise<PlanLimitsResponse> {
    return api.get<PlanLimitsResponse>('/api/v1/subscriptions/limits')
  },
}

