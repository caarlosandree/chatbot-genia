import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { subscriptionService } from '@/services/api'
import type {
  Subscription,
  CreateSubscriptionInput,
  PlanLimitsResponse,
} from '@/types/subscription'

/**
 * Query keys para assinaturas
 */
export const subscriptionKeys = {
  all: ['subscriptions'] as const,
  lists: () => [...subscriptionKeys.all, 'list'] as const,
  details: () => [...subscriptionKeys.all, 'detail'] as const,
  detail: (id: number) => [...subscriptionKeys.details(), id] as const,
  me: () => [...subscriptionKeys.all, 'me'] as const,
  limits: () => [...subscriptionKeys.all, 'limits'] as const,
}

/**
 * Hook para buscar a assinatura ativa do usuário atual
 */
export function useMySubscription() {
  return useQuery({
    queryKey: subscriptionKeys.me(),
    queryFn: () => subscriptionService.getMySubscription(),
  })
}

/**
 * Hook para buscar uma assinatura por ID
 */
export function useSubscription(id: number) {
  return useQuery({
    queryKey: subscriptionKeys.detail(id),
    queryFn: () => subscriptionService.getById(id),
    enabled: !!id,
  })
}

/**
 * Hook para criar uma assinatura
 */
export function useCreateSubscription() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: CreateSubscriptionInput) =>
      subscriptionService.create(data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: subscriptionKeys.all })
      queryClient.setQueryData(subscriptionKeys.detail(data.id), data)
    },
  })
}

/**
 * Hook para cancelar uma assinatura
 */
export function useCancelSubscription() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: number) => subscriptionService.cancel(id),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: subscriptionKeys.all })
      queryClient.setQueryData(subscriptionKeys.detail(data.id), data)
    },
  })
}

/**
 * Hook para buscar os limites atuais do plano
 */
export function usePlanLimits() {
  return useQuery({
    queryKey: subscriptionKeys.limits(),
    queryFn: () => subscriptionService.getLimits(),
  })
}

