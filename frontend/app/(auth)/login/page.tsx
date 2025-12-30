'use client'

import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import Image from 'next/image'
import Link from 'next/link'
import { useRouter } from 'next/navigation'
import { useState } from 'react'
import { toast } from 'sonner'
import { Eye, EyeOff, Mail, Lock } from 'lucide-react'

import { Button } from '@/components/ui/button'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Checkbox } from '@/components/ui/checkbox'
import { loginSchema, type LoginFormData } from '@/schemas/auth'
import { authService } from '@/services/api'
import { useAuth } from '@/hooks/useAuth'
import { Role } from '@/types/auth'

export default function LoginPage() {
  const router = useRouter()
  const { login } = useAuth()
  const [isLoading, setIsLoading] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const [rememberMe, setRememberMe] = useState(false)

  const form = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  })

  const onSubmit = async (data: LoginFormData) => {
    setIsLoading(true)

    try {
      const response = await authService.login(data)

      // Faz login no store
      await login({
        accessToken: response.accessToken,
        refreshToken: response.refreshToken,
      })

      // Extrai o role do token para redirecionar corretamente
      const tokenPayload = JSON.parse(
        atob(response.accessToken.split('.')[1])
      ) as { role: Role }

      // Redireciona baseado no role
      if (tokenPayload.role === Role.ADMIN) {
        router.push('/admin')
      } else {
        router.push('/client')
      }

      toast.success('Login realizado com sucesso!')
    } catch (error) {
      const errorMessage =
        error instanceof Error ? error.message : 'Erro ao fazer login'
      toast.error(errorMessage)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="flex w-full h-screen login-bg text-white overflow-hidden">
      {/* SEÇÃO DA IMAGEM (70%) */}
      <div className="hidden lg:flex w-[70%] relative bg-black items-center justify-center overflow-hidden">
        {/* Imagem de Fundo */}
        <Image
          src="/loginpage.jpg?v=2"
          alt="ChatBot GenIA Background"
          fill
          className="object-cover opacity-60 hover:scale-105 transition-transform duration-[20s]"
          priority
          unoptimized
        />

        {/* Overlay Gradiente */}
        <div className="absolute inset-0 bg-gradient-to-r from-slate-900 via-transparent to-transparent" />
        <div className="absolute inset-0 bg-gradient-to-t from-slate-900 via-transparent to-transparent" />

        {/* Conteúdo Sobre a Imagem */}
        <div className="relative z-10 p-12 text-left w-full h-full flex flex-col justify-end pb-24">
          <div className="max-w-2xl opacity-0 animate-[fadeIn_0.5s_ease-in-out_0.2s_forwards]">
            {/* Logo Conceitual */}
            <div className="flex items-center gap-3 mb-6">
              <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-cyan-400 to-purple-600 flex items-center justify-center shadow-lg shadow-purple-500/30">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-7 w-7 text-white"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                  />
                </svg>
              </div>
              <h1 className="text-3xl font-bold tracking-tight text-white">
                ChatBot GenIA
              </h1>
            </div>

            <h2 className="text-5xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 to-purple-400 mb-4 drop-shadow-sm">
              Inteligência que evolui com você.
            </h2>
            <p className="text-slate-300 text-lg leading-relaxed max-w-lg">
              Acesse a nova geração de assistência virtual. O GenIA processa
              dados complexos e entrega simplicidade em milissegundos.
            </p>
          </div>
        </div>
      </div>

      {/* SEÇÃO DE LOGIN (30%) */}
      <div className="w-full lg:w-[30%] login-bg border-l border-sidebar-border shadow-2xl flex flex-col justify-center px-8 md:px-12 relative z-20">
        <div className="w-full max-w-md mx-auto opacity-0 animate-[fadeIn_0.5s_ease-in-out_0.1s_forwards]">
          {/* Cabeçalho */}
          <div className="mb-10 text-center lg:text-left">
            <div className="lg:hidden flex justify-center mb-4">
              <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-cyan-400 to-purple-600 flex items-center justify-center">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6 text-white"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"
                  />
                </svg>
              </div>
            </div>
            <h2 className="text-3xl font-bold text-white mb-2">
              Bem-vindo de volta
            </h2>
            <p className="text-slate-400">
              Insira suas credenciais para acessar o painel.
            </p>
          </div>

          {/* Formulário */}
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="space-y-5"
            >
              {/* Input Email */}
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem className="space-y-1">
                    <FormLabel className="text-sm font-medium text-slate-300 ml-1">
                      E-mail
                    </FormLabel>
                    <FormControl>
                      <div className="relative group">
                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                          <Mail className="h-5 w-5 text-slate-500 group-focus-within:text-purple-400 transition-colors" />
                        </div>
                        <Input
                          type="email"
                          placeholder="nome@exemplo.com"
                          disabled={isLoading}
                          className="pl-10 pr-3 py-3 border-slate-700 rounded-lg bg-slate-800 text-slate-100 placeholder-slate-500 focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all shadow-sm"
                          {...field}
                        />
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* Input Senha */}
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem className="space-y-1">
                    <div className="flex justify-between items-center ml-1">
                      <FormLabel className="text-sm font-medium text-slate-300">
                        Senha
                      </FormLabel>
                    </div>
                    <FormControl>
                      <div className="relative group">
                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                          <Lock className="h-5 w-5 text-slate-500 group-focus-within:text-purple-400 transition-colors" />
                        </div>
                        <Input
                          type={showPassword ? 'text' : 'password'}
                          placeholder="••••••••"
                          disabled={isLoading}
                          className="pl-10 pr-10 py-3 border-slate-700 rounded-lg bg-slate-800 text-slate-100 placeholder-slate-500 focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all shadow-sm"
                          {...field}
                        />
                        <button
                          type="button"
                          onClick={() => setShowPassword(!showPassword)}
                          className="absolute inset-y-0 right-0 pr-3 flex items-center cursor-pointer"
                          tabIndex={-1}
                        >
                          {showPassword ? (
                            <EyeOff className="h-5 w-5 text-slate-500 hover:text-slate-300 transition-colors" />
                          ) : (
                            <Eye className="h-5 w-5 text-slate-500 hover:text-slate-300 transition-colors" />
                          )}
                        </button>
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* Lembrar de mim e Esqueceu a senha */}
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <Checkbox
                    id="remember_me"
                    checked={rememberMe}
                    onCheckedChange={(checked) =>
                      setRememberMe(checked === true)
                    }
                    className="border-gray-600 bg-slate-800 data-[state=checked]:bg-purple-600 data-[state=checked]:border-purple-600"
                  />
                  <label
                    htmlFor="remember_me"
                    className="text-sm text-slate-400 cursor-pointer select-none"
                  >
                    Lembrar de mim
                  </label>
                </div>
                <Link
                  href="/forgot-password"
                  className="text-sm font-medium text-purple-400 hover:text-purple-300 transition-colors focus:outline-none"
                >
                  Esqueceu a senha?
                </Link>
              </div>

              {/* Botão de Submit */}
              <Button
                type="submit"
                disabled={isLoading}
                className="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-lg text-sm font-semibold text-white bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 focus:ring-offset-slate-900 transform active:scale-95 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? (
                  <>
                    <svg
                      className="animate-spin -ml-1 mr-3 h-5 w-5 text-white inline"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        strokeWidth="4"
                      />
                      <path
                        className="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      />
                    </svg>
                    Processando...
                  </>
                ) : (
                  'Entrar no Sistema'
                )}
              </Button>
            </form>
          </Form>

          {/* Footer */}
          <div className="mt-8">
            <p className="text-center text-xs text-slate-500">
              &copy; 2025 GenIA Systems Inc. Todos os direitos reservados.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

