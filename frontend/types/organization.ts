/**
 * Organização/empresa do sistema
 */
export interface Organization {
  id: number
  name: string
  document: string
  email: string
  phone?: string | null
  active: boolean
  createdAt: string
  updatedAt: string
}

/**
 * Request para criar organização
 */
export interface CreateOrganizationInput {
  name: string
  document: string
  email: string
  phone?: string | null
}

/**
 * Request para atualizar organização
 */
export interface UpdateOrganizationInput {
  name?: string
  email?: string
  phone?: string | null
}

