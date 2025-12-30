import type { Organization } from './organization'
import type { Plan } from './plan'

/**
 * Status de uma assinatura
 */
export enum SubscriptionStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED',
}

/**
 * Assinatura de plano por uma organização
 */
export interface Subscription {
  id: number
  organization: Organization
  plan: Plan
  status: SubscriptionStatus
  startDate: string
  endDate?: string | null
  createdAt: string
  updatedAt: string
}

/**
 * Request para criar assinatura
 */
export interface CreateSubscriptionInput {
  organizationId: number
  planId: number
  startDate: string
  endDate?: string | null
}

/**
 * Resposta com limites atuais do plano
 */
export interface PlanLimitsResponse {
  maxUsers: number
  maxChatbots: number
  maxPhoneNumbers: number
  currentUsers: number
  currentChatbots: number
  currentPhoneNumbers: number
}

