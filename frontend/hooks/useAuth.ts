'use client'

import { useAuthStore } from '@/stores/authStore'
import type { Role } from '@/types/auth'

/**
 * Hook para gerenciar autenticação
 */
export function useAuth() {
  const {
    user,
    tokens,
    isAuthenticated,
    isLoading,
    login,
    logout,
    setUser,
    setTokens,
    initialize,
  } = useAuthStore()

  /**
   * Verifica se o usuário é ADMIN
   */
  const isAdmin = () => {
    return user?.role === 'ADMIN'
  }

  /**
   * Verifica se o usuário é CLIENT
   */
  const isClient = () => {
    return user?.role === 'CLIENT'
  }

  /**
   * Verifica se o usuário tem um role específico
   */
  const hasRole = (role: Role) => {
    return user?.role === role
  }

  return {
    user,
    tokens,
    isAuthenticated,
    isLoading,
    login,
    logout,
    setUser,
    setTokens,
    initialize,
    isAdmin,
    isClient,
    hasRole,
  }
}

