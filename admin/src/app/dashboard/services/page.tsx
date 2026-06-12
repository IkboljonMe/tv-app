'use client';
import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { getLabel } from '@/types';
import type { HotelService } from '@/types';
import { Plus, Pencil, Trash2, GripVertical } from 'lucide-react';
import { clsx } from 'clsx';

interface ServiceForm { labelEn: string; labelRu: string; iconUrl: string; deepLink: string; }
const EMPTY: ServiceForm = { labelEn: '', labelRu: '', iconUrl: '', deepLink: '' };

export default function ServicesPage() {
  const qc = useQueryClient();
  const { data: services = [], isLoading } = useQuery({
    queryKey: ['services'],
    queryFn:  api.getServices,
  });

  const [form,      setForm]      = useState<ServiceForm>(EMPTY);
  const [editId,    setEditId]    = useState<string | null>(null);
  const [showForm,  setShowForm]  = useState(false);

  const createMutation = useMutation({
    mutationFn: (data: Partial<HotelService>) => api.createService(data),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['services'] }); reset(); },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<HotelService> }) => api.updateService(id, data),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['services'] }); reset(); },
  });

  const deleteMutation = useMutation({
    mutationFn: api.deleteService,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['services'] }),
  });

  const toggleMutation = useMutation({
    mutationFn: ({ id, available }: { id: string; available: boolean }) =>
      api.updateService(id, { available }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['services'] }),
  });

  function reset() { setForm(EMPTY); setEditId(null); setShowForm(false); }

  function startEdit(svc: HotelService) {
    const label = typeof svc.label === 'object' ? svc.label : { en: svc.label as string };
    setForm({ labelEn: label.en ?? '', labelRu: label.ru ?? '', iconUrl: svc.iconUrl ?? '', deepLink: svc.deepLink ?? '' });
    setEditId(svc.id);
    setShowForm(true);
  }

  function submit(e: React.FormEvent) {
    e.preventDefault();
    const label = { en: form.labelEn, ru: form.labelRu };
    const data: Partial<HotelService> = { label, iconUrl: form.iconUrl || undefined, deepLink: form.deepLink || undefined };
    if (editId) updateMutation.mutate({ id: editId, data });
    else        createMutation.mutate({ ...data, sortOrder: services.length, available: true });
  }

  return (
    <div className="max-w-3xl space-y-5">
      {/* Toolbar */}
      <div className="flex justify-end">
        <button
          onClick={() => { reset(); setShowForm(true); }}
          className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-500 text-white text-sm font-medium rounded-lg"
        >
          <Plus size={16} /> Add Service
        </button>
      </div>

      {/* Form panel */}
      {showForm && (
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <h3 className="font-semibold text-slate-800 mb-4">{editId ? 'Edit Service' : 'New Service'}</h3>
          <form onSubmit={submit} className="grid grid-cols-2 gap-3">
            <FormField label="Name (EN)" value={form.labelEn} onChange={v => setForm(f => ({ ...f, labelEn: v }))} required />
            <FormField label="Name (RU)" value={form.labelRu} onChange={v => setForm(f => ({ ...f, labelRu: v }))} />
            <FormField label="Icon URL"  value={form.iconUrl}  onChange={v => setForm(f => ({ ...f, iconUrl: v }))} className="col-span-2" />
            <FormField label="Deep Link" value={form.deepLink} onChange={v => setForm(f => ({ ...f, deepLink: v }))} className="col-span-2" />
            <div className="col-span-2 flex gap-2 justify-end">
              <button type="button" onClick={reset} className="px-4 py-2 text-sm text-slate-600 hover:bg-slate-100 rounded-lg">Cancel</button>
              <button type="submit" disabled={createMutation.isPending || updateMutation.isPending}
                className="px-4 py-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 text-white text-sm font-medium rounded-lg">
                {editId ? 'Save' : 'Create'}
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Services list */}
      {isLoading ? (
        <Spinner />
      ) : services.length === 0 ? (
        <EmptyState />
      ) : (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="divide-y divide-slate-100">
            {services.map((svc: HotelService) => (
              <div key={svc.id} className="flex items-center gap-3 px-4 py-3 hover:bg-slate-50">
                <GripVertical size={16} className="text-slate-300 cursor-grab" />
                {svc.iconUrl
                  ? <img src={svc.iconUrl} alt="" className="w-8 h-8 object-contain rounded" />
                  : <div className="w-8 h-8 bg-slate-100 rounded" />}
                <div className="flex-1 min-w-0">
                  <p className="font-medium text-slate-800 text-sm">{getLabel(svc.label)}</p>
                  {svc.deepLink && <p className="text-xs text-slate-400 truncate">{svc.deepLink}</p>}
                </div>
                <Toggle
                  checked={svc.available}
                  onChange={v => toggleMutation.mutate({ id: svc.id, available: v })}
                />
                <button onClick={() => startEdit(svc)} className="p-1.5 text-slate-400 hover:text-blue-600 rounded">
                  <Pencil size={15} />
                </button>
                <button onClick={() => { if (confirm('Delete this service?')) deleteMutation.mutate(svc.id); }}
                  className="p-1.5 text-slate-400 hover:text-red-600 rounded">
                  <Trash2 size={15} />
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

function FormField({ label, value, onChange, required, className }: {
  label: string; value: string; onChange: (v: string) => void; required?: boolean; className?: string;
}) {
  return (
    <div className={className}>
      <label className="block text-xs font-medium text-slate-600 mb-1">{label}</label>
      <input
        type="text" value={value} onChange={e => onChange(e.target.value)} required={required}
        className="w-full text-sm px-3 py-2 border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
    </div>
  );
}

function Toggle({ checked, onChange }: { checked: boolean; onChange: (v: boolean) => void }) {
  return (
    <button
      onClick={() => onChange(!checked)}
      className={clsx(
        'relative w-10 h-6 rounded-full transition-colors',
        checked ? 'bg-emerald-500' : 'bg-slate-200',
      )}
    >
      <span className={clsx(
        'absolute top-1 w-4 h-4 bg-white rounded-full shadow transition-transform',
        checked ? 'translate-x-5' : 'translate-x-1',
      )} />
    </button>
  );
}

function Spinner() {
  return <div className="flex justify-center py-10"><div className="w-6 h-6 border-4 border-blue-600 border-t-transparent rounded-full animate-spin"/></div>;
}
function EmptyState() {
  return <div className="text-center py-10 text-slate-400 text-sm">No services yet. Add your first service.</div>;
}
