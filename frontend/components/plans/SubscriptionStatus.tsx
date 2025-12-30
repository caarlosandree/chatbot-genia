'use client'

import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { SubscriptionStatus as Status } from '@/types/subscription'
import type { Subscription } from '@/types/subscription'
// Formatação de data simples sem dependência externa

interface SubscriptionStatusProps {
  subscription: Subscription
  className?: string
}

export function SubscriptionStatus({
  subscription,
  className,
}: SubscriptionStatusProps) {
  const getStatusBadge = (status: Status) => {
    switch (status) {
      case Status.ACTIVE:
        return <Badge variant="default">Ativa</Badge>
      case Status.INACTIVE:
        return <Badge variant="secondary">Inativa</Badge>
      case Status.EXPIRED:
        return <Badge variant="destructive">Expirada</Badge>
      case Status.CANCELLED:
        return <Badge variant="outline">Cancelada</Badge>
      default:
        return <Badge variant="outline">{status}</Badge>
    }
  }

  const formatDate = (date: string) => {
    const d = new Date(date)
    const months = [
      'janeiro',
      'fevereiro',
      'março',
      'abril',
      'maio',
      'junho',
      'julho',
      'agosto',
      'setembro',
      'outubro',
      'novembro',
      'dezembro',
    ]
    return `${d.getDate()} de ${months[d.getMonth()]} de ${d.getFullYear()}`
  }

  return (
    <Card className={className}>
      <CardHeader>
        <CardTitle>Assinatura Atual</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div>
          <p className="text-muted-foreground text-sm">Plano</p>
          <p className="font-semibold">{subscription.plan.name}</p>
        </div>
        <div>
          <p className="text-muted-foreground text-sm">Status</p>
          <div className="mt-1">{getStatusBadge(subscription.status)}</div>
        </div>
        <div>
          <p className="text-muted-foreground text-sm">Data de Início</p>
          <p className="font-semibold">{formatDate(subscription.startDate)}</p>
        </div>
        {subscription.endDate && (
          <div>
            <p className="text-muted-foreground text-sm">Data de Término</p>
            <p className="font-semibold">{formatDate(subscription.endDate)}</p>
          </div>
        )}
      </CardContent>
    </Card>
  )
}

