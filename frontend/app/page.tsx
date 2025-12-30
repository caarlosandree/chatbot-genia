'use client'

import { useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { useAuth } from '@/hooks/useAuth'
import { Role } from '@/types/auth'

export default function Home() {
  const router = useRouter()
  const { isAuthenticated, isLoading, user, initialize } = useAuth()

  useEffect(() => {
    initialize()
  }, [initialize])

  useEffect(() => {
    if (!isLoading) {
      if (isAuthenticated && user) {
        // Redireciona para o dashboard baseado no role
        if (user.role === Role.ADMIN) {
          router.push('/admin')
        } else {
          router.push('/client')
        }
      } else {
        // Redireciona para login se não autenticado
        router.push('/login')
      }
    }
  }, [isLoading, isAuthenticated, user, router])

  // Mostra loading enquanto verifica autenticação
  return (
    <div className="flex min-h-screen items-center justify-center">
      <div className="text-center">
        <div className="mb-4 h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent mx-auto" />
        <p className="text-muted-foreground">Carregando...</p>
      </div>
    </div>
  )
}
