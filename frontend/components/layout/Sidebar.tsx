'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import Image from 'next/image'
import { LayoutDashboard, Users, Settings, Bot, CreditCard } from 'lucide-react'
import { cn } from '@/lib/utils'
import { ScrollArea } from '@/components/ui/scroll-area'
import { useAuth } from '@/hooks/useAuth'
import { Role } from '@/types/auth'

interface MenuItem {
  title: string
  href: string
  icon: React.ComponentType<{ className?: string }>
  roles: Role[]
}

const menuItems: MenuItem[] = [
  {
    title: 'Dashboard',
    href: '/admin', // Será ajustado baseado no role
    icon: LayoutDashboard,
    roles: [Role.ADMIN, Role.CLIENT],
  },
  {
    title: 'Gerenciar Clientes',
    href: '/admin/clients',
    icon: Users,
    roles: [Role.ADMIN],
  },
  {
    title: 'Gerenciar Usuários',
    href: '/admin/users',
    icon: Users,
    roles: [Role.ADMIN],
  },
  {
    title: 'Chatbots',
    href: '/admin/chatbots', // Será ajustado baseado no role
    icon: Bot,
    roles: [Role.ADMIN, Role.CLIENT],
  },
  {
    title: 'Planos',
    href: '/admin/plans', // Será ajustado baseado no role
    icon: CreditCard,
    roles: [Role.ADMIN, Role.CLIENT],
  },
  {
    title: 'Configurações',
    href: '/admin/settings', // Será ajustado baseado no role
    icon: Settings,
    roles: [Role.ADMIN, Role.CLIENT],
  },
]

export function Sidebar() {
  const pathname = usePathname()
  const { user, isAdmin, isClient } = useAuth()

  // Filtra itens de menu baseado no role do usuário
  const visibleMenuItems = menuItems.filter((item) => {
    if (!user) return false
    return item.roles.includes(user.role)
  })

  // Ajusta href baseado no role do usuário
  const getHref = (item: MenuItem) => {
    // Se o item é compartilhado entre ADMIN e CLIENT, ajusta o href
    if (item.roles.includes(Role.ADMIN) && item.roles.includes(Role.CLIENT)) {
      if (isAdmin()) {
        // Se é admin, garante que está em /admin
        if (item.href === '/plans' || item.href.includes('/plans')) {
          return '/admin/plans'
        }
        return item.href.replace('/client', '/admin')
      }
      if (isClient()) {
        // Se é client, garante que está em /client
        if (item.href === '/plans' || item.href.includes('/plans')) {
          return '/client/plans'
        }
        return item.href.replace('/admin', '/client')
      }
    }
    return item.href
  }

  return (
    <div className="flex h-screen w-64 flex-col border-r border-sidebar-border sidebar-bg sidebar-glow relative">
      {/* Logo */}
      <div className="flex h-20 items-center border-b border-sidebar-border px-6 relative z-10">
        <Link href={isAdmin() ? '/admin' : '/client'} className="flex items-center gap-3">
          <Image
            src="/logo.png"
            alt="Chatbot GenIA"
            width={48}
            height={48}
            className="rounded"
          />
          <span className="text-2xl font-bold bg-linear-to-r from-primary via-accent to-primary bg-clip-text text-transparent">
            Chatbot GenIA
          </span>
        </Link>
      </div>

      {/* Menu Items */}
      <ScrollArea className="flex-1 relative z-10">
        <nav className="space-y-1 p-4">
          {visibleMenuItems.map((item) => {
            const Icon = item.icon
            const href = getHref(item)
            const isActive = pathname === href || pathname.startsWith(`${href}/`)

            return (
              <Link
                key={item.href}
                href={href}
                className={cn(
                  'sidebar-menu-item flex items-center gap-3 rounded-lg px-3 py-2 text-base font-medium',
                  isActive && 'sidebar-menu-item-active'
                )}
              >
                <Icon
                  className={cn(
                    'sidebar-menu-icon h-5 w-5 transition-all',
                    isActive ? 'sidebar-menu-icon-active' : 'text-white/70'
                  )}
                />
                <span className={cn('sidebar-menu-text transition-all', isActive ? '' : 'text-white/90')}>
                  {item.title}
                </span>
              </Link>
            )
          })}
        </nav>
      </ScrollArea>
    </div>
  )
}

