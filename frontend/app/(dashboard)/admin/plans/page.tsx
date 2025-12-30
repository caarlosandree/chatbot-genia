'use client'

import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { toast } from 'sonner'
import { Plus, Pencil, Trash2 } from 'lucide-react'

import { Button } from '@/components/ui/button'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Badge } from '@/components/ui/badge'
import { Switch } from '@/components/ui/switch'
import { Skeleton } from '@/components/ui/skeleton'
import {
  useAllPlans,
  useCreatePlan,
  useUpdatePlan,
  useDeletePlan,
} from '@/hooks/queries/usePlans'
import type { Plan, CreatePlanInput, UpdatePlanInput } from '@/types/plan'

const planSchema = z.object({
  name: z.string().min(1, 'Nome é obrigatório'),
  description: z.string().optional().nullable(),
  price: z.number().min(0, 'Preço deve ser positivo ou zero'),
  maxUsers: z.number().int('Deve ser um número inteiro'),
  maxChatbots: z.number().int('Deve ser um número inteiro'),
  maxPhoneNumbers: z.number().int('Deve ser um número inteiro'),
  active: z.boolean().optional(),
})

type PlanFormData = z.infer<typeof planSchema>

export default function AdminPlansPage() {
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingPlan, setEditingPlan] = useState<Plan | null>(null)
  const { data: plans, isLoading } = useAllPlans()
  const createPlan = useCreatePlan()
  const updatePlan = useUpdatePlan()
  const deletePlan = useDeletePlan()

  const form = useForm<PlanFormData>({
    resolver: zodResolver(planSchema),
    defaultValues: {
      name: '',
      description: '',
      price: 0,
      maxUsers: 0,
      maxChatbots: 0,
      maxPhoneNumbers: 0,
      active: true,
    },
  })

  const handleOpenDialog = (plan?: Plan) => {
    if (plan) {
      setEditingPlan(plan)
      form.reset({
        name: plan.name,
        description: plan.description || '',
        price: plan.price,
        maxUsers: plan.maxUsers,
        maxChatbots: plan.maxChatbots,
        maxPhoneNumbers: plan.maxPhoneNumbers,
        active: plan.active,
      })
    } else {
      setEditingPlan(null)
      form.reset({
        name: '',
        description: '',
        price: 0,
        maxUsers: 0,
        maxChatbots: 0,
        maxPhoneNumbers: 0,
        active: true,
      })
    }
    setDialogOpen(true)
  }

  const handleCloseDialog = () => {
    setDialogOpen(false)
    setEditingPlan(null)
    form.reset()
  }

  const onSubmit = async (data: PlanFormData) => {
    try {
      if (editingPlan) {
        const updateData: UpdatePlanInput = {
          name: data.name,
          description: data.description || null,
          price: data.price,
          maxUsers: data.maxUsers,
          maxChatbots: data.maxChatbots,
          maxPhoneNumbers: data.maxPhoneNumbers,
          active: data.active,
        }
        await updatePlan.mutateAsync({
          id: editingPlan.id,
          data: updateData,
        })
        toast.success('Plano atualizado com sucesso!')
      } else {
        const createData: CreatePlanInput = {
          name: data.name,
          description: data.description || null,
          price: data.price,
          maxUsers: data.maxUsers,
          maxChatbots: data.maxChatbots,
          maxPhoneNumbers: data.maxPhoneNumbers,
          active: data.active ?? true,
        }
        await createPlan.mutateAsync(createData)
        toast.success('Plano criado com sucesso!')
      }
      handleCloseDialog()
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : 'Erro ao salvar plano. Tente novamente.'
      )
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Tem certeza que deseja deletar este plano?')) {
      return
    }

    try {
      await deletePlan.mutateAsync(id)
      toast.success('Plano deletado com sucesso!')
    } catch (error) {
      toast.error(
        error instanceof Error
          ? error.message
          : 'Erro ao deletar plano. Tente novamente.'
      )
    }
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(price)
  }

  const formatLimit = (limit: number) => {
    return limit === -1 ? 'Ilimitado' : limit.toString()
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Gerenciar Planos</h1>
          <p className="text-muted-foreground">
            Crie, edite e gerencie os planos disponíveis
          </p>
        </div>
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogTrigger asChild>
            <Button onClick={() => handleOpenDialog()}>
              <Plus className="mr-2 h-4 w-4" />
              Criar Novo Plano
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>
                {editingPlan ? 'Editar Plano' : 'Criar Novo Plano'}
              </DialogTitle>
              <DialogDescription>
                {editingPlan
                  ? 'Atualize as informações do plano'
                  : 'Preencha os dados para criar um novo plano'}
              </DialogDescription>
            </DialogHeader>
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="space-y-4"
              >
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Nome</FormLabel>
                      <FormControl>
                        <Input placeholder="Ex: Plano Básico" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="description"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Descrição</FormLabel>
                      <FormControl>
                        <Textarea
                          placeholder="Descreva o plano..."
                          {...field}
                          value={field.value || ''}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <div className="grid grid-cols-2 gap-4">
                  <FormField
                    control={form.control}
                    name="price"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Preço (R$)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            step="0.01"
                            min="0"
                            placeholder="0.00"
                            {...field}
                            onChange={(e) =>
                              field.onChange(parseFloat(e.target.value) || 0)
                            }
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="active"
                    render={({ field }) => (
                      <FormItem className="flex flex-col justify-end">
                        <div className="flex items-center space-x-2">
                          <Switch
                            checked={field.value ?? true}
                            onCheckedChange={field.onChange}
                          />
                          <FormLabel className="!mt-0">Ativo</FormLabel>
                        </div>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
                <div className="grid grid-cols-3 gap-4">
                  <FormField
                    control={form.control}
                    name="maxUsers"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Máx. Usuários</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            min="-1"
                            placeholder="-1 para ilimitado"
                            {...field}
                            onChange={(e) =>
                              field.onChange(parseInt(e.target.value) || 0)
                            }
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="maxChatbots"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Máx. Chatbots</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            min="-1"
                            placeholder="-1 para ilimitado"
                            {...field}
                            onChange={(e) =>
                              field.onChange(parseInt(e.target.value) || 0)
                            }
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="maxPhoneNumbers"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Máx. Telefones</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            min="-1"
                            placeholder="-1 para ilimitado"
                            {...field}
                            onChange={(e) =>
                              field.onChange(parseInt(e.target.value) || 0)
                            }
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
                <DialogFooter>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={handleCloseDialog}
                  >
                    Cancelar
                  </Button>
                  <Button
                    type="submit"
                    disabled={
                      createPlan.isPending || updatePlan.isPending
                    }
                  >
                    {createPlan.isPending || updatePlan.isPending
                      ? 'Salvando...'
                      : editingPlan
                        ? 'Atualizar'
                        : 'Criar'}
                  </Button>
                </DialogFooter>
              </form>
            </Form>
          </DialogContent>
        </Dialog>
      </div>

      {isLoading ? (
        <div className="space-y-4">
          {[1, 2, 3].map((i) => (
            <Skeleton key={i} className="h-16 w-full" />
          ))}
        </div>
      ) : (
        <div className="rounded-md border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nome</TableHead>
                <TableHead>Descrição</TableHead>
                <TableHead>Preço</TableHead>
                <TableHead>Usuários</TableHead>
                <TableHead>Chatbots</TableHead>
                <TableHead>Telefones</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Ações</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {plans && plans.length > 0 ? (
                plans.map((plan) => (
                  <TableRow key={plan.id}>
                    <TableCell className="font-medium">{plan.name}</TableCell>
                    <TableCell className="max-w-xs truncate">
                      {plan.description || '-'}
                    </TableCell>
                    <TableCell>{formatPrice(plan.price)}</TableCell>
                    <TableCell>{formatLimit(plan.maxUsers)}</TableCell>
                    <TableCell>{formatLimit(plan.maxChatbots)}</TableCell>
                    <TableCell>{formatLimit(plan.maxPhoneNumbers)}</TableCell>
                    <TableCell>
                      <Badge variant={plan.active ? 'default' : 'secondary'}>
                        {plan.active ? 'Ativo' : 'Inativo'}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleOpenDialog(plan)}
                        >
                          <Pencil className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleDelete(plan.id)}
                          disabled={deletePlan.isPending}
                        >
                          <Trash2 className="h-4 w-4 text-destructive" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={8} className="text-center">
                    Nenhum plano encontrado
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>
      )}
    </div>
  )
}

