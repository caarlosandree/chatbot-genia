import type { JwtPayload } from '@/types/auth'

/**
 * Decodifica um token JWT sem verificar assinatura (apenas para leitura de payload)
 * @param token Token JWT
 * @returns Payload decodificado ou null se inválido
 */
export function decodeJwt(token: string): JwtPayload | null {
  try {
    const base64Url = token.split('.')[1]
    if (!base64Url) {
      return null
    }

    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        })
        .join('')
    )

    return JSON.parse(jsonPayload) as JwtPayload
  } catch (error) {
    console.error('Erro ao decodificar JWT:', error)
    return null
  }
}

/**
 * Extrai o payload de um token JWT
 * @param token Token JWT
 * @returns Payload ou null se inválido
 */
export function extractPayload(token: string): JwtPayload | null {
  return decodeJwt(token)
}

/**
 * Verifica se um token JWT está expirado
 * @param token Token JWT
 * @returns true se expirado, false caso contrário
 */
export function isTokenExpired(token: string): boolean {
  const payload = decodeJwt(token)
  if (!payload || !payload.exp) {
    return true
  }

  // exp está em segundos, Date.now() retorna milissegundos
  const expirationTime = payload.exp * 1000
  const currentTime = Date.now()

  return currentTime >= expirationTime
}

/**
 * Obtém o tempo restante até a expiração em milissegundos
 * @param token Token JWT
 * @returns Tempo restante em ms ou 0 se expirado/inválido
 */
export function getTokenTimeRemaining(token: string): number {
  const payload = decodeJwt(token)
  if (!payload || !payload.exp) {
    return 0
  }

  const expirationTime = payload.exp * 1000
  const currentTime = Date.now()
  const remaining = expirationTime - currentTime

  return Math.max(0, remaining)
}

