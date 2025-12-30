'use client'

import { useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { useAuth } from '@/hooks/useAuth'
import { Role } from '@/types/auth'

export default function PlansPage() {
  const router = useRouter()
  const { user, isLoading, initialize } = useAuth()

  useEffect(() => {
    initialize()
  }, [initialize])

  useEffect(() => {
    if (!isLoading && user) {
      // Redireciona baseado no role
      if (user.role === Role.ADMIN) {
        router.replace('/admin/plans')
      } else {
        router.replace('/client/plans')
      }
    }
  }, [isLoading, user, router])

  // Mostra loading enquanto redireciona
  return (
    <div className="flex h-screen items-center justify-center">
      <div className="text-center">
        <div className="mb-4 h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent mx-auto" />
        <p className="text-muted-foreground">Redirecionando...</p>
      </div>
    </div>
  )
}
