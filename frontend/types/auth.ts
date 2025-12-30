/**
 * Roles do sistema
 */
export enum Role {
  ADMIN = 'ADMIN',
  CLIENT = 'CLIENT',
}

/**
 * Usuário do sistema
 */
export interface User {
  id: number
  email: string
  name: string
  phone?: string | null
  role: Role
  active: boolean
  lastLogin?: string | null
  createdAt: string
  updatedAt: string
}

/**
 * Request para login
 */
export interface LoginRequest {
  email: string
  password: string
}

/**
 * Response de autenticação do backend
 */
export interface JwtResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  expiresIn: number
}

/**
 * Tokens de autenticação armazenados
 */
export interface AuthTokens {
  accessToken: string
  refreshToken: string
}

/**
 * Payload do JWT (decodificado)
 */
export interface JwtPayload {
  sub: string // email do usuário
  role: Role
  iat: number
  exp: number
}

