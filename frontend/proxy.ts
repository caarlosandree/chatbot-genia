import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

/**
 * Proxy para interceptar requisições
 * 
 * Nota: A proteção de rotas autenticadas é feita no layout do dashboard.
 * Este proxy pode ser usado para outras funcionalidades futuras (ex: analytics, logging).
 */
export function proxy(request: NextRequest) {
  const { pathname } = request.nextUrl

  // Permite todas as requisições
  // A proteção de rotas é feita no layout do dashboard
  return NextResponse.next()
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
}

