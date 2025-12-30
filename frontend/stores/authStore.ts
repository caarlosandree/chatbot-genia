'use client'

import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User, AuthTokens } from '@/types/auth'
import { extractPayload } from '@/utils/jwt'
import { authService } from '@/services/api'

interface AuthState {
  user: User | null
  tokens: AuthTokens | null
  isAuthenticated: boolean
  isLoading: boolean

  // Actions
  setTokens: (tokens: AuthTokens) => void
  setUser: (user: User) => void
  login: (tokens: AuthTokens) => Promise<void>
  logout: () => Promise<void>
  initialize: () => Promise<void>
}

/**
 * Extrai dados do usuário do token JWT
 */
function extractUserFromToken(accessToken: string): User | null {
  const payload = extractPayload(accessToken)
  if (!payload) {
    return null
  }

  // O payload JWT contém sub (email) e role
  // Para obter dados completos do usuário, precisaríamos de uma chamada à API
  // Por enquanto, construímos um usuário básico a partir do token
  return {
    id: 0, // Será atualizado quando buscarmos do backend
    email: payload.sub,
    name: payload.sub.split('@')[0], // Nome temporário baseado no email
    role: payload.role,
    active: true,
    createdAt: new Date(payload.iat * 1000).toISOString(),
    updatedAt: new Date().toISOString(),
  }
}

/**
 * Store de autenticação usando Zustand com persistência
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      tokens: null,
      isAuthenticated: false,
      isLoading: true,

      setTokens: (tokens) => {
        const user = tokens.accessToken
          ? extractUserFromToken(tokens.accessToken)
          : null

        set({
          tokens,
          user,
          isAuthenticated: !!tokens.accessToken && !!user,
        })
      },

      setUser: (user) => {
        set({ user, isAuthenticated: !!user })
      },

      login: async (tokens) => {
        const user = extractUserFromToken(tokens.accessToken)

        set({
          tokens,
          user,
          isAuthenticated: true,
          isLoading: false,
        })
      },

      logout: async () => {
        await authService.logout()
        set({
          user: null,
          tokens: null,
          isAuthenticated: false,
          isLoading: false,
        })
      },

      initialize: async () => {
        const storedTokens = authService.getStoredTokens()

        if (storedTokens) {
          const user = extractUserFromToken(storedTokens.accessToken)
          set({
            tokens: storedTokens,
            user,
            isAuthenticated: !!user,
            isLoading: false,
          })
        } else {
          set({
            tokens: null,
            user: null,
            isAuthenticated: false,
            isLoading: false,
          })
        }
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        tokens: state.tokens,
        // Não persistimos o user completo, apenas os tokens
        // O user será reconstruído do token na inicialização
      }),
    }
  )
)

