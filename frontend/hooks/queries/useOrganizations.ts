import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { organizationService } from '@/services/api'
import type {
  Organization,
  CreateOrganizationInput,
  UpdateOrganizationInput,
} from '@/types/organization'

/**
 * Query keys para organizações
 */
export const organizationKeys = {
  all: ['organizations'] as const,
  lists: () => [...organizationKeys.all, 'list'] as const,
  details: () => [...organizationKeys.all, 'detail'] as const,
  detail: (id: number) => [...organizationKeys.details(), id] as const,
  me: () => [...organizationKeys.all, 'me'] as const,
}

/**
 * Hook para buscar a organização do usuário atual
 */
export function useMyOrganization() {
  return useQuery({
    queryKey: organizationKeys.me(),
    queryFn: () => organizationService.getMyOrganization(),
  })
}

/**
 * Hook para buscar uma organização por ID
 */
export function useOrganization(id: number) {
  return useQuery({
    queryKey: organizationKeys.detail(id),
    queryFn: () => organizationService.getById(id),
    enabled: !!id,
  })
}

/**
 * Hook para criar uma organização
 */
export function useCreateOrganization() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: CreateOrganizationInput) =>
      organizationService.create(data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: organizationKeys.all })
      queryClient.setQueryData(organizationKeys.detail(data.id), data)
    },
  })
}

/**
 * Hook para atualizar uma organização
 */
export function useUpdateOrganization() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number
      data: UpdateOrganizationInput
    }) => organizationService.update(id, data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: organizationKeys.all })
      queryClient.setQueryData(organizationKeys.detail(data.id), data)
    },
  })
}

