import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { planService } from '@/services/api'
import type { Plan, CreatePlanInput, UpdatePlanInput } from '@/types/plan'

/**
 * Query keys para planos
 */
export const planKeys = {
  all: ['plans'] as const,
  lists: () => [...planKeys.all, 'list'] as const,
  list: (filters: string) => [...planKeys.lists(), { filters }] as const,
  details: () => [...planKeys.all, 'detail'] as const,
  detail: (id: number) => [...planKeys.details(), id] as const,
}

/**
 * Hook para listar todos os planos ativos
 */
export function usePlans() {
  return useQuery({
    queryKey: planKeys.lists(),
    queryFn: () => planService.getAll(),
  })
}

/**
 * Hook para buscar um plano por ID
 */
export function usePlan(id: number) {
  return useQuery({
    queryKey: planKeys.detail(id),
    queryFn: () => planService.getById(id),
    enabled: !!id,
  })
}

/**
 * Hook para listar todos os planos, incluindo inativos (apenas ADMIN)
 */
export function useAllPlans() {
  return useQuery({
    queryKey: [...planKeys.all, 'all'],
    queryFn: () => planService.getAllPlans(),
  })
}

/**
 * Hook para criar um plano
 */
export function useCreatePlan() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: CreatePlanInput) => planService.create(data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: planKeys.all })
      queryClient.setQueryData(planKeys.detail(data.id), data)
    },
  })
}

/**
 * Hook para atualizar um plano
 */
export function useUpdatePlan() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdatePlanInput }) =>
      planService.update(id, data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: planKeys.all })
      queryClient.setQueryData(planKeys.detail(data.id), data)
    },
  })
}

/**
 * Hook para deletar um plano
 */
export function useDeletePlan() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: number) => planService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: planKeys.all })
    },
  })
}

