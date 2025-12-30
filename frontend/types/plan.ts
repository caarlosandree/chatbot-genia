/**
 * Plano disponível no sistema
 */
export interface Plan {
  id: number
  name: string
  description?: string | null
  price: number
  maxUsers: number
  maxChatbots: number
  maxPhoneNumbers: number
  active: boolean
  createdAt: string
  updatedAt: string
}

/**
 * Request para criar plano
 */
export interface CreatePlanInput {
  name: string
  description?: string | null
  price: number
  maxUsers: number
  maxChatbots: number
  maxPhoneNumbers: number
  active?: boolean
}

/**
 * Request para atualizar plano
 */
export interface UpdatePlanInput {
  name?: string
  description?: string | null
  price?: number
  maxUsers?: number
  maxChatbots?: number
  maxPhoneNumbers?: number
  active?: boolean
}

/**
 * Limites de um plano
 */
export interface PlanLimits {
  maxUsers: number
  maxChatbots: number
  maxPhoneNumbers: number
}

