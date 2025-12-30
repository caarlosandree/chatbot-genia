'use client'

import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { PlanLimits } from './PlanLimits'
import type { Plan } from '@/types/plan'

interface PlanCardProps {
  plan: Plan
  onSelect?: (plan: Plan) => void
  isSelected?: boolean
  isCurrentPlan?: boolean
}

export function PlanCard({
  plan,
  onSelect,
  isSelected = false,
  isCurrentPlan = false,
}: PlanCardProps) {
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(price)
  }

  return (
    <Card
      className={`relative ${isSelected ? 'ring-2 ring-primary' : ''} ${
        isCurrentPlan ? 'border-primary' : ''
      }`}
    >
      {isCurrentPlan && (
        <Badge className="absolute right-4 top-4" variant="default">
          Plano Atual
        </Badge>
      )}
      <CardHeader>
        <CardTitle className="text-2xl">{plan.name}</CardTitle>
        <CardDescription>{plan.description}</CardDescription>
        <div className="mt-4">
          <span className="text-4xl font-bold">{formatPrice(plan.price)}</span>
          <span className="text-muted-foreground text-sm">/mês</span>
        </div>
      </CardHeader>
      <CardContent>
        <PlanLimits plan={plan} />
      </CardContent>
      {onSelect && (
        <CardFooter>
          <Button
            className="w-full"
            variant={isSelected ? 'default' : 'outline'}
            onClick={() => onSelect(plan)}
            disabled={isCurrentPlan}
          >
            {isCurrentPlan
              ? 'Plano Atual'
              : isSelected
                ? 'Selecionado'
                : 'Contratar'}
          </Button>
        </CardFooter>
      )}
    </Card>
  )
}

