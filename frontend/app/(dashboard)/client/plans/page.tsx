'use client'

import { PlanCard } from '@/components/plans/PlanCard'
import { SubscriptionStatus } from '@/components/plans/SubscriptionStatus'
import { usePlans } from '@/hooks/queries/usePlans'
import { useMySubscription } from '@/hooks/queries/useSubscriptions'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import { Badge } from '@/components/ui/badge'
import type { Plan } from '@/types/plan'

export default function ClientPlansPage() {
  const { data: plans, isLoading: plansLoading } = usePlans()
  const { data: subscription, isLoading: subscriptionLoading } =
    useMySubscription()

  const isLoading = plansLoading || subscriptionLoading

  // Filtra planos disponíveis para upgrade (planos com preço maior que o atual ou planos diferentes)
  const availablePlans = plans?.filter((plan) => {
    if (!subscription || subscription.status !== 'ACTIVE') {
      return true // Se não tem assinatura ativa, mostra todos
    }
    // Mostra planos diferentes do atual
    return plan.id !== subscription.plan.id
  })

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Planos</h1>
        <p className="text-muted-foreground">
          Visualize seu plano atual e os planos disponíveis para upgrade
        </p>
      </div>

      {/* Plano Atual */}
      {subscription && subscription.status === 'ACTIVE' && (
        <div className="space-y-4">
          <div className="flex items-center gap-2">
            <h2 className="text-xl font-semibold">Seu Plano Atual</h2>
            <Badge variant="default">Ativo</Badge>
          </div>
          <Card className="border-primary">
            <CardHeader>
              <CardTitle className="flex items-center justify-between">
                <span>{subscription.plan.name}</span>
                <Badge>Plano Atual</Badge>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <SubscriptionStatus subscription={subscription} />
            </CardContent>
          </Card>
        </div>
      )}

      {/* Planos Disponíveis */}
      <div className="space-y-4">
        <h2 className="text-xl font-semibold">Planos Disponíveis</h2>
        {isLoading ? (
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {[1, 2, 3].map((i) => (
              <Card key={i}>
                <CardHeader>
                  <Skeleton className="h-6 w-32" />
                  <Skeleton className="mt-2 h-4 w-48" />
                </CardHeader>
                <CardContent>
                  <Skeleton className="h-32 w-full" />
                </CardContent>
              </Card>
            ))}
          </div>
        ) : (
          <>
            {availablePlans && availablePlans.length > 0 ? (
              <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                {availablePlans.map((plan) => (
                  <PlanCard
                    key={plan.id}
                    plan={plan}
                    isCurrentPlan={
                      subscription?.plan.id === plan.id &&
                      subscription?.status === 'ACTIVE'
                    }
                  />
                ))}
              </div>
            ) : (
              <Card>
                <CardContent className="py-8 text-center text-muted-foreground">
                  Nenhum plano disponível para upgrade no momento
                </CardContent>
              </Card>
            )}
          </>
        )}
      </div>

      {/* Mensagem se não tiver assinatura */}
      {!subscription && !subscriptionLoading && (
        <Card>
          <CardContent className="py-8 text-center">
            <p className="text-muted-foreground">
              Você ainda não possui uma assinatura ativa. Entre em contato com
              o suporte para contratar um plano.
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}

