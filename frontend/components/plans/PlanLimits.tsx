'use client'

import { Check, Infinity } from 'lucide-react'
import type { Plan } from '@/types/plan'

interface PlanLimitsProps {
  plan: Plan
  className?: string
}

export function PlanLimits({ plan, className }: PlanLimitsProps) {
  const isUnlimited = (value: number) => value === -1

  return (
    <div className={className}>
      <ul className="space-y-3">
        <li className="flex items-center gap-2">
          <Check className="size-4 text-primary" />
          <span className="text-sm">
            {isUnlimited(plan.maxUsers) ? (
              <span className="flex items-center gap-1">
                <Infinity className="size-4" />
                Usuários ilimitados
              </span>
            ) : (
              `${plan.maxUsers} usuário${plan.maxUsers > 1 ? 's' : ''}`
            )}
          </span>
        </li>
        <li className="flex items-center gap-2">
          <Check className="size-4 text-primary" />
          <span className="text-sm">
            {isUnlimited(plan.maxChatbots) ? (
              <span className="flex items-center gap-1">
                <Infinity className="size-4" />
                Chatbots ilimitados
              </span>
            ) : (
              `${plan.maxChatbots} chatbot${plan.maxChatbots > 1 ? 's' : ''}`
            )}
          </span>
        </li>
        <li className="flex items-center gap-2">
          <Check className="size-4 text-primary" />
          <span className="text-sm">
            {isUnlimited(plan.maxPhoneNumbers) ? (
              <span className="flex items-center gap-1">
                <Infinity className="size-4" />
                Números ilimitados
              </span>
            ) : (
              `${plan.maxPhoneNumbers} número${plan.maxPhoneNumbers > 1 ? 's' : ''} de telefone`
            )}
          </span>
        </li>
      </ul>
    </div>
  )
}

